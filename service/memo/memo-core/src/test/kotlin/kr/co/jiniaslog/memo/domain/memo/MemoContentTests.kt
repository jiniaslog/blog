package kr.co.jiniaslog.memo.domain.memo

import org.junit.jupiter.api.Test

class MemoContentTests {
    @Test
    fun `유효한 메모 내용으로 메모 내용을 생성하면 생성된다`() {
        // given
        val content = "메모 내용"
        // when
        val memoContent = MemoContent(content)
        // then
        assert(memoContent.value == content)
    }
}
