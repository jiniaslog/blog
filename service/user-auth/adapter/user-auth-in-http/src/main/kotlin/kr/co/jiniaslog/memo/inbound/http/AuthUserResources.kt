package kr.co.jiniaslog.memo.inbound.http

import kr.co.jiniaslog.user.application.usecase.IGetOAuthRedirectionUrl
import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.application.usecase.ISignInOAuthUser
import kr.co.jiniaslog.user.application.usecase.UseCasesUserAuthFacade
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.token.AuthorizationCode
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/auth")
class AuthUserResources(
    private val usecases: UseCasesUserAuthFacade,
) {
    @GetMapping("/{provider}/url")
    fun getRedirectUrl(
        @PathVariable provider: String,
    ): ResponseEntity<RedirectUrlResponse> {
        val info =
            usecases.handle(
                IGetOAuthRedirectionUrl.Command(
                    provider = Provider.valueOf(provider),
                ),
            )

        return ResponseEntity.ok()
            .body(RedirectUrlResponse(info.url.value))
    }

    @PostMapping("/{provider}/login")
    fun login(
        @PathVariable provider: String,
        @RequestBody request: OAuthLoginRequest,
    ): ResponseEntity<LoginResponse> {
        val info =
            usecases.handle(
                ISignInOAuthUser.Command(
                    provider = Provider.valueOf(provider),
                    code = AuthorizationCode(request.code),
                ),
            )

        val response =
            LoginResponse(
                nickName = info.nickName.value,
                email = info.email.value,
                roles = info.roles.map { it.toString() }.toSet(),
                picUrl = info.picUrl?.value,
            )

        val accessCookie =
            ResponseCookie.from("jiniaslog_access", info.accessToken.value)
                .domain("localhost")
                .path("/api")
                .httpOnly(true)
                .maxAge(60 * 60)
                .secure(true)
                .sameSite("None")
                .build()

        val refreshCookie =
            ResponseCookie.from("jiniaslog_refresh", info.refreshToken.value)
                .domain("localhost")
                .path("/api/v1/auth/refresh")
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 7)
                .secure(true)
                .sameSite("None")
                .build()

        return ResponseEntity.ok()
            .header(SET_COOKIE, accessCookie.toString())
            .header(SET_COOKIE, refreshCookie.toString())
            .body(response)
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue("jiniaslog_refresh") refreshToken: String,
    ): ResponseEntity<EmptyResponse> {
        val info =
            usecases.handle(
                IRefreshToken.Command(
                    refreshToken = RefreshToken(refreshToken),
                ),
            )

        val accessCookie =
            ResponseCookie.from("jiniaslog_access", info.accessToken.value)
                .domain("localhost")
                .path("/api")
                .httpOnly(true)
                .maxAge(60 * 60)
                .secure(true)
                .sameSite("None")
                .build()

        // rotate refresh token
        val refreshCookie =
            ResponseCookie.from("jiniaslog_refresh", info.refreshToken.value)
                .domain("localhost")
                .path("/api/v1/auth/refresh")
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 7)
                .secure(true)
                .sameSite("None")
                .build()

        return ResponseEntity.ok()
            .header(SET_COOKIE, accessCookie.toString())
            .header(SET_COOKIE, refreshCookie.toString())
            .body(EmptyResponse())
    }
}
