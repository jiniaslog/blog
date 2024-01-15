package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.domain.user.UserId

interface AuthUserLockManager {
    fun lock(
        userId: UserId,
        timeOutSeconds: Int = 10,
        block: () -> IRefreshToken.Info,
    ): IRefreshToken.Info

    fun hasLock(userId: UserId): Boolean
}