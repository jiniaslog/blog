package kr.co.jiniaslog.shared.core.domain.vo

@JvmInline
value class Url(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) { "Url must not be blank" }
        require(value.length <= 2083) { "Url must not be longer than 2083 characters" }
    }
}
