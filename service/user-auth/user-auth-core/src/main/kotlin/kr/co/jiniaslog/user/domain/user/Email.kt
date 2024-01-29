package kr.co.jiniaslog.user.domain.user

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class Email(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) { "이메일은 빈 값이 될 수 없습니다." }
        require(value.length <= 50) { "이메일은 50자를 넘을 수 없습니다." }
        require(value.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$"))) {
            "이메일 형식이 올바르지 않습니다."
        }
    }
}
