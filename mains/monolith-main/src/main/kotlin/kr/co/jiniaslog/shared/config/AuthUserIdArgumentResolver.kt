package kr.co.jiniaslog.shared.config

import kr.co.jiniaslog.memo.adapter.inbound.http.AuthUserId
import kr.co.jiniaslog.user.application.security.UserPrincipal
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthUserIdArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(AuthUserId::class.java) != null
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is UserPrincipal) {
            val userDetails = authentication.principal as UserPrincipal
            return userDetails.userId
        }
        return null
    }
}
