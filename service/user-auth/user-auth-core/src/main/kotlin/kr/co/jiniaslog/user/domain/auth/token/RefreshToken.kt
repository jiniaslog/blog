package kr.co.jiniaslog.user.domain.auth.token

@JvmInline
value class RefreshToken(override val value: String) : AuthToken {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) { "Refresh must not be blank" }
    }
}
