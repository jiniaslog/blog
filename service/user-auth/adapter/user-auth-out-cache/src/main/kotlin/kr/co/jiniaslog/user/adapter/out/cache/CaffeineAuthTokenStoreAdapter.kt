package kr.co.jiniaslog.user.adapter.out.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.user.UserId
import java.util.concurrent.TimeUnit

@PersistenceAdapter
internal class CaffeineAuthTokenStoreAdapter : TokenStore {
    private val cache: Cache<String, Pair<AccessToken, RefreshToken>> = Caffeine.newBuilder().build()
    private val tempCache: Cache<String, RefreshToken> =
        Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build()

    override fun save(
        userId: UserId,
        accessToken: AccessToken,
        refreshToken: RefreshToken,
    ) {
        val key = userId.value.toString()
        cache.getIfPresent(key)?.let {
            tempCache.put(key, it.second)
        }
        cache.put(key, Pair(accessToken, refreshToken))
    }

    override fun findByAuthTokens(userId: UserId): Triple<AccessToken, RefreshToken?, RefreshToken>? {
        val key = userId.value.toString()
        val authTokens = cache.getIfPresent(key)
        val tempToken = tempCache.getIfPresent(key)

        return if (authTokens != null) {
            Triple(authTokens.first, tempToken, authTokens.second)
        } else {
            null
        }
    }
}
