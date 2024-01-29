package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.user.UserId

interface TokenStore {
    fun save(
        userId: UserId,
        accessToken: AccessToken,
        refreshToken: RefreshToken,
    )

    /**
     * Find by user id
     *
     * @param userId
     * @return Triple<엑세스 토큰, 이전 리프레시토큰?, 신규 리프레시 토큰> or null
     */
    fun findByUserId(userId: UserId): AuthTokenInfo?

    fun delete(userId: UserId)
}

data class AuthTokenInfo(
    val accessToken: AccessToken,
    val oldRefreshToken: RefreshToken?,
    val newRefreshToken: RefreshToken,
)
