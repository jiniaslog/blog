package kr.co.jiniaslog.user.domain.auth.token

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class AccessTokenTests {
    @Test
    fun `accessToken은 empty string이 아니어야 한다`() {
        shouldThrow<IllegalArgumentException> {
            AccessToken("")
        }
    }
}
