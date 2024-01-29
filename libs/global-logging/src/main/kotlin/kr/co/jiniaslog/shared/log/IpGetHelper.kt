package kr.co.jiniaslog.shared.log

import jakarta.servlet.http.HttpServletRequest

object IpGetHelper {
    fun getClientIP(request: HttpServletRequest): String? {
        val headers =
            listOf(
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
            )

        headers.forEach { header ->
            request.getHeader(header)?.let { return it }
        }

        return request.remoteAddr
    }
}
