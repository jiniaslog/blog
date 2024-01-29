package kr.co.jiniaslog.user.application.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class ServiceAuthenticationToken(
    principal: Any?,
    details: Any?,
    authorities: Collection<GrantedAuthority?>?,
) : UsernamePasswordAuthenticationToken(principal, null, authorities) {
    init {
        setDetails(details)
    }
}
