package kr.co.jiniaslog.user.application.security

class UserPrincipal(
    val userId: Long,
    val roles: Set<String>,
)
