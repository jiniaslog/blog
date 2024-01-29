package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.user.domain.auth.provider.Provider
import org.springframework.stereotype.Component

@Component
class ProviderResolver(
    private val providerAdapters: List<ProviderAdapter>,
) {
    fun isSupported(provider: Provider): Boolean {
        return providerAdapters.any { it.provider == provider }
    }

    fun resolve(provider: Provider): ProviderAdapter {
        require(isSupported(provider)) { "not supported provider. provider: $provider" }
        return providerAdapters.find { it.provider == provider }!!
    }
}
