package kr.co.jiniaslog.memo.domain.memo

import io.kotest.assertions.throwables.shouldThrow
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import org.junit.jupiter.api.Test

class MemoTitleTests {
    @Test
    fun `제목이 0글자면 실패한다`() {
        shouldThrow<IllegalArgumentException> {
            MemoTitle("")
        }
    }

    @Test
    fun `제목이 100글자를 초과하면 실패한다`() {
        shouldThrow<IllegalArgumentException> {
            MemoTitle("a".repeat(101))
        }
    }

    @Test
    fun `제목이 100글자 이하면 생성된다`() {
        // given
        val title = "a".repeat(100)
        // when
        val memoTitle = MemoTitle(title)
        // then
        assert(memoTitle.value == title)
        val valueClass: ValueObject = memoTitle
        valueClass.validate()
    }
}
