package kr.co.jiniaslog.user.adapter.out.mysql.auth

import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.user.UserId
import kotlin.jvm.optionals.getOrNull

@PersistenceAdapter
class TokenStoreAdapter(
    private val tokenRepository: TokenRepository,
) : TokenStore {
    override fun save(
        userId: UserId,
        accessToken: AccessToken,
        refreshToken: RefreshToken,
    ) {
        val token = tokenRepository.findById(userId.value).getOrNull()
        if (token == null) {
            tokenRepository.save(TokenJpaEntity(userId.value, accessToken.value, refreshToken.value, refreshToken.value, null, null))
        } else {
            token.accessToken = accessToken.value
            token.oldRefreshToken = token.newRefreshToken
            token.newRefreshToken = refreshToken.value
            tokenRepository.save(token)
        }
    }

    override fun findByAuthTokens(userId: UserId): Triple<AccessToken, RefreshToken, RefreshToken>? {
        return tokenRepository.findById(userId.value).getOrNull()?.let {
            Triple(
                AccessToken(it.accessToken),
                RefreshToken(it.oldRefreshToken),
                RefreshToken(it.newRefreshToken),
            )
        }
    }
}
