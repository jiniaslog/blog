package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.domain.user.UserId
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Component
class AuthUserLockManagerImpl : AuthUserLockManager {
    private val locks = ConcurrentHashMap<UserId, ReentrantLock>()

    override fun lock(
        userId: UserId,
        timeOutSeconds: Int,
        block: () -> IRefreshToken.Info,
        forIdempotencyFallback: () -> IRefreshToken.Info,
    ): IRefreshToken.Info {
        val lock = locks.computeIfAbsent(userId) { ReentrantLock() }
        val isLocked = lock.tryLock()
        if (isLocked) {
            try {
                return block()
            } finally {
                lock.unlock()
            }
        }
        val isLockedForIdempotency = lock.tryLock(timeOutSeconds.toLong(), TimeUnit.SECONDS)
        if (isLockedForIdempotency) {
            try {
                return forIdempotencyFallback()
            } finally {
                lock.unlock()
            }
        }
        throw IllegalStateException("lock timeout. userId: $userId")
    }
}
