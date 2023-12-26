package kr.co.jiniaslog.memo.domain

import io.kotest.matchers.shouldBe
import kr.co.jiniaslog.shared.CustomBehaviorSpec

internal class MemoTests : CustomBehaviorSpec() {
    init {
        // context: 새로운 메모 작성시
        Given("유효한 메모 Id와 메모 컨텐츠가 준비되면") {
            val id = MemoId(1L)
            val content = MemoContent("유효한 메모 컨텐츠")
            When("메모를 작성하면") {
                val newMemo = Memo.newOne(id, content)
                Then("메모가 작성된다") {
                    newMemo.content shouldBe content
                }
            }
        }
    }
}
