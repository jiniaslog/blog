package kr.co.jiniaslog.user.adapter.out.google

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.co.jiniaslog.shared.core.domain.vo.Url

data class GoogleAccessTokenRequest(
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: Url,
    val grantType: String = "authorization_code",
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleAccessTokenResponse(
    val accessToken: String?,
    val expiresIn: Long?,
    val scope: String?,
    val tokenType: String?,
    val idToken: String?,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleUserInfo(
    val email: String?,
    val name: String?,
    val picture: String?,
)
