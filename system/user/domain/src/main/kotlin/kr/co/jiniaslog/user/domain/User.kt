package kr.co.jiniaslog.user.domain

// stub
class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val roles: Set<String>,
)
