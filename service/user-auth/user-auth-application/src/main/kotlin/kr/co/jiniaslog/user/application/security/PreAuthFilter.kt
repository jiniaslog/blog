package kr.co.jiniaslog.user.application.security

import jakarta.servlet.http.HttpServletRequest
import kr.co.jiniaslog.user.domain.auth.token.AccessToken
import kr.co.jiniaslog.user.domain.auth.token.AuthToken
import kr.co.jiniaslog.user.domain.auth.token.TokenManger
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class PreAuthFilter(
    private val jwtTokenManager: TokenManger,
    authenticationProvider: AuthProvider,
) : AbstractPreAuthenticatedProcessingFilter() {
    init {
        setAuthenticationManager(ProviderManager(authenticationProvider))
    }

    override fun getPreAuthenticatedPrincipal(httpServletRequest: HttpServletRequest): Any? {
        val token = resolveAccessToken(httpServletRequest)
        if (!isValidTokenFormat(token)) {
            return null
        }

        if (!jwtTokenManager.validateToken(token!!)) {
            return null
        }

        val userId = jwtTokenManager.extractUserId(token).value
        val rolesSet: Set<String> =
            jwtTokenManager.getRole(token).stream()
                .map { it.name }
                .collect(Collectors.toSet())
        return UserPrincipal(userId, rolesSet)
    }

    private fun resolveAccessToken(req: HttpServletRequest): AccessToken? {
        val token =
            try {
                req.cookies.find { it.name == "jiniaslog_access" }
            } catch (e: NullPointerException) {
                null
            }
        return token?.value?.let { AccessToken(it) }
    }

    private fun isValidTokenFormat(token: AuthToken?): Boolean {
        return token != null
    }

    override fun getPreAuthenticatedCredentials(httpServletRequest: HttpServletRequest?): Any {
        return ""
    }

    override fun principalChanged(
        request: HttpServletRequest?,
        currentAuthentication: Authentication?,
    ): Boolean {
        return super.principalChanged(request, currentAuthentication)
    }
}
