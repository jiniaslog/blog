package kr.co.jiniaslog.user.application.usecase

import kr.co.jiniaslog.shared.core.domain.vo.Url
import kr.co.jiniaslog.user.domain.auth.provider.Provider

interface IGetOAuthRedirectionUrl {
    fun handle(command: Command): Info

    data class Command(
        val provider: Provider,
    )

    data class Info(
        val url: Url,
    )
}
