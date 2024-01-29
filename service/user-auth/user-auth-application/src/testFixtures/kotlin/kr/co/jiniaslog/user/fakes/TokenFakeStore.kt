package kr.co.jiniaslog.user.fakes

import kr.co.jiniaslog.user.application.infra.AuthTokenInfo
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.user.UserId
import java.time.LocalDateTime

class TokenFakeStore : TokenStore {
    private val tokens = mutableMapOf<UserId, Pair<AuthTokenInfo, LocalDateTime>>()

    override fun save(
        userId: UserId,
        accessToken: AccessToken,
        refreshToken: RefreshToken,
    ) {
        if (tokens.containsKey(userId)) {
            tokens[userId] = AuthTokenInfo(
                accessToken = accessToken,
                oldRefreshToken = tokens[userId]?.first?.newRefreshToken,
                newRefreshToken = refreshToken,
            ) to LocalDateTime.now()
        } else {
            tokens[userId] = AuthTokenInfo(
                accessToken = accessToken,
                oldRefreshToken = null,
                newRefreshToken = refreshToken,
            ) to LocalDateTime.now()
        }
    }

    override fun findByUserId(userId: UserId): AuthTokenInfo? {
        return tokens[userId]?.let {
            if (LocalDateTime.now() < it.second.plusSeconds(5)) {
                it.first
            } else {
                it.first.copy(oldRefreshToken = null)
            }
        }
    }

    override fun delete(userId: UserId) {
        tokens.remove(userId)
    }
}
