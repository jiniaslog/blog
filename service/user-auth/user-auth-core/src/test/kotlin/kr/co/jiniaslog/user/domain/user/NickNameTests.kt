package kr.co.jiniaslog.user.domain.user

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class NickNameTests {
    @Test
    fun `빈 닉네임은 생성될 수 없다`() {
        // given
        val nickName = ""

        // when, then
        shouldThrow<IllegalArgumentException> {
            NickName(nickName)
        }
    }

    @Test
    fun `유효한 닉네임은 정상적으로 생성된다`() {
        // given
        val nickName = "12345678901"

        // when
        val sut = NickName(nickName)

        // then
        assert(true)
    }
}
