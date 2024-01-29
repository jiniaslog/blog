package kr.co.jiniaslog.user.domain.user

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class UserId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value > 0) { "UserId must be greater than 0" }
    }
}
