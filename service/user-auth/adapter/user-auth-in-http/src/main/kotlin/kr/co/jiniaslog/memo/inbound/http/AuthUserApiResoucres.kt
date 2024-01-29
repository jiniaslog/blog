package kr.co.jiniaslog.memo.inbound.http

data class RedirectUrlResponse(
    val url: String,
)

data class OAuthLoginRequest(
    val code: String,
)

data class LoginResponse(
    val nickName: String,
    val email: String,
    val roles: Set<String>,
    val picUrl: String?,
)

class EmptyResponse
