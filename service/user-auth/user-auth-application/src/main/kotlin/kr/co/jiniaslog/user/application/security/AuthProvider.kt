package kr.co.jiniaslog.user.application.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class AuthProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication.principal is UserPrincipal) {
            return getAccountAuthentication(authentication)
        }
        return null
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(clazz)
    }

    private fun getAccountAuthentication(authentication: Authentication): Authentication {
        val principal = authentication.principal as UserPrincipal
        val authorities: Set<SimpleGrantedAuthority> =
            principal.roles.stream()
                .map { s -> SimpleGrantedAuthority("ROLE_$s") }
                .collect(Collectors.toSet())
        return ServiceAuthenticationToken(principal, null, authorities)
    }
}
