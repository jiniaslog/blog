package kr.co.jiniaslog.user.domain.auth.token

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class RefreshTokenTests {
    @Test
    fun `refreshToken은 empty string이 아니여야한다`() {
        shouldThrow<IllegalArgumentException> {
            RefreshToken("")
        }
    }
}
