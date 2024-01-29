package kr.co.jiniaslog.user.domain.auth.token

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class AuthorizationCode(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) { "AuthorizationCode must not be blank" }
    }
}
