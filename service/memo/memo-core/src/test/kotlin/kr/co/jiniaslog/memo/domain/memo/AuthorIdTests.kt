package kr.co.jiniaslog.memo.domain.memo

import io.kotest.assertions.throwables.shouldThrow
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AuthorIdTests {
    @Test
    fun `양수의 ID로 작성자 ID를 생성하면 생성된다`() {
        // given
        val authorId = 1L
        // when
        val author = AuthorId(authorId)
        // then
        author.validate()
        val valueClass: ValueObject = author
        valueClass.validate()
        assert(author.value == authorId)
    }

    @ParameterizedTest(name = "{index}번 테스트: args = {0}")
    @ValueSource(longs = [0, -1])
    fun `유효하지 않은 숫자로 작성자 Id를 생성하면 실패한다`(authorId: Long) {
        shouldThrow<IllegalArgumentException> {
            AuthorId(authorId)
        }
    }

    @Test
    fun `getValue()를 호출하면 ID를 반환한다`() {
        // given
        val authorId = 1L
        val author = AuthorId(authorId)
        // when
        val value = author.value
        // then
        assert(value == authorId)
    }
}
