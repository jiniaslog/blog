package kr.co.jiniaslog.user.domain.auth.token

@JvmInline
value class AccessToken(override val value: String) : AuthToken {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) { "AccessToken must not be blank" }
    }
}
