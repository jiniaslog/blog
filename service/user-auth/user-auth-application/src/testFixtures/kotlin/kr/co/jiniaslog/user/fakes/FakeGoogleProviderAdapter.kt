package kr.co.jiniaslog.user.fakes

import kr.co.jiniaslog.shared.core.domain.vo.Url
import kr.co.jiniaslog.user.application.infra.ProviderAdapter
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.provider.ProviderUserInfo
import kr.co.jiniaslog.user.domain.auth.token.AuthorizationCode
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.NickName

open class FakeGoogleProviderAdapter : ProviderAdapter {
    override val provider = Provider.GOOGLE

    override fun getUserInfo(code: AuthorizationCode): ProviderUserInfo {
        return ProviderUserInfo(
            provider = provider,
            nickName = NickName("testUser"),
            email = Email("testUser@google.com"),
            picture = null,
        )
    }

    override fun getLoginUrl(): Url {
        return Url("https://google.com/login")
    }
}
