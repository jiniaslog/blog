package kr.co.jiniaslog.user.domain.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class EmailTests {
    @Test
    fun `빈 이메일은 생성될 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            Email("")
        }
    }

    @Test
    fun `이메일은 @를 포함해야 한다`() {
        shouldThrow<IllegalArgumentException> {
            Email("jinia")
        }
    }

    @Test
    fun `이메일은 @ 뒤에 도메인을 포함해야 한다`() {
        shouldThrow<IllegalArgumentException> {
            Email("jinia@")
        }
    }

    @Test
    fun `이메일은 @ 앞에 아이디를 포함해야 한다`() {
        shouldThrow<IllegalArgumentException> {
            Email("@gmail.com")
        }
    }

    @Test
    fun `이메일은 @ 뒤에 도메인을 포함해야 한다 2`() {
        shouldThrow<IllegalArgumentException> {
            Email("jinia@gmail")
        }
    }

    @Test
    fun `50를 초과하는 이메일 길이는 생성될 수 없다`() {
        shouldThrow<IllegalArgumentException> {
            Email(" ".repeat(50) + "@gmail.com")
        }.message shouldBe "이메일은 50자를 넘을 수 없습니다."
    }

    @Test
    fun `유효한 이메일은 생성될 수 있다`() {
        // given
        val email = "jinia@gmail.com"

        // when
        val sut = Email(email)

        // then
        sut.value shouldBe email
    }
}
