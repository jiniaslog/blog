package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.shared.core.domain.vo.Url
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.provider.ProviderUserInfo
import kr.co.jiniaslog.user.domain.auth.token.AuthorizationCode

interface ProviderAdapter {
    val provider: Provider

    fun getUserInfo(code: AuthorizationCode): ProviderUserInfo

    fun getLoginUrl(): Url
}
