package kr.co.jiniaslog.user.application.usecase

import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken

interface IRefreshToken {
    fun handle(command: Command): Info

    data class Command(
        val refreshToken: RefreshToken,
    )

    data class Info(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken,
    )
}
