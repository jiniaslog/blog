package kr.co.jiniaslog.user.domain.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UserIdTests {
    @Test
    fun `유저 아이디는 0보다 커야한다`() {
        // given
        val id = -1L

        // when, then
        shouldThrow<IllegalArgumentException> {
            UserId(id)
        }
    }

    @Test
    fun `유효한 유저 아이디는 정상적으로 생성된다`() {
        // given
        val id = 1L

        // when
        val sut = UserId(id)

        // then
        sut.value shouldBe id
    }
}
