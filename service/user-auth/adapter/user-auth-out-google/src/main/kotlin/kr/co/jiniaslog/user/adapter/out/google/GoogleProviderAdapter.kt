package kr.co.jiniaslog.user.adapter.out.google

import kr.co.jiniaslog.shared.core.domain.vo.Url
import kr.co.jiniaslog.user.application.infra.ProviderAdapter
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.provider.ProviderUserInfo
import kr.co.jiniaslog.user.domain.auth.token.AuthorizationCode
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.NickName
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

private val log = KotlinLogging.logger { }

@Component
class GoogleProviderAdapter(
    @Value("\${oauth.google.redirect-url}")
    redirectUrl: String,
    @Value("\${oauth.google.token-url}")
    tokenUrl: String,
    @Value("\${oauth.google.user-info-url}")
    userInfoUrl: String,
    @Value("\${oauth.google.client-secret}")
    clientSecret: String,
    @Value("\${oauth.google.client-id}")
    clientId: String,
) : ProviderAdapter {
    override val provider: Provider = Provider.GOOGLE
    private val redirectUrl: Url = Url(redirectUrl)
    private val client: RestClient = RestClient.create()
    private val tokenUrl: Url = Url(tokenUrl)
    private val userInfoUrl: Url = Url(userInfoUrl)
    private val clientSecret: String = clientSecret
    private val clientId: String = clientId

    override fun getLoginUrl(): Url {
        return Url(
            """https://accounts.google.com/o/oauth2/v2/auth
                |?client_id=$clientId
                |&redirect_uri=${redirectUrl.value}
                |&response_type=code
                |&scope=email profile"""
                .trimMargin(),
        )
    }

    override fun getUserInfo(code: AuthorizationCode): ProviderUserInfo {
        val response = requestAccessToken(code)
        val userInfo = requestUserInfo(response)

        return ProviderUserInfo(
            nickName = userInfo.name?.let { NickName(userInfo.name) } ?: NickName.UNKNOWN,
            email = Email(userInfo.email!!),
            picture = userInfo.picture?.let { Url(userInfo.picture) },
            provider = Provider.GOOGLE,
        )
    }

    private fun requestAccessToken(code: AuthorizationCode): GoogleAccessTokenResponse {
        val request =
            GoogleAccessTokenRequest(
                code = code.value,
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUrl,
            )
        log.info { "request: $request" }
        val response =
            client.post()
                .uri(tokenUrl.value)
                .body(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(GoogleAccessTokenResponse::class.java)

        requireNotNull(response) { "구글 토큰 응답이 없습니다" }
        return response
    }

    private fun requestUserInfo(response: GoogleAccessTokenResponse): GoogleUserInfo {
        val header = "Bearer ${response.accessToken}"

        val userInfo =
            client.get()
                .uri(userInfoUrl.value)
                .header("Authorization", header)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(GoogleUserInfo::class.java)

        requireNotNull(userInfo) { "구글 유저 정보가 없습니다" }
        requireNotNull(userInfo.email) { "구글 이메일이 없습니다" }
        return userInfo
    }
}
