package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.domain.user.UserId

interface AuthUserLockManager {
    fun lock(
        userId: UserId,
        timeOutSeconds: Int = 0,
        block: () -> IRefreshToken.Info,
        forIdempotencyFallback: () -> IRefreshToken.Info,
    ): IRefreshToken.Info
}
