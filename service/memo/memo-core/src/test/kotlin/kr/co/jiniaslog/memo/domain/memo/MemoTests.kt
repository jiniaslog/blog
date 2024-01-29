package kr.co.jiniaslog.memo.domain.memo

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.shared.CustomBehaviorSpec

internal class MemoTests : CustomBehaviorSpec() {
    init {
        // context: 새로운 메모 작성시
        Given("새로운 메모 작성시 작성자 id만 주어지고") {
            val authorId = AuthorId(1)
            When("메모 초기화를 하면") {
                val sutMemo = Memo.init(authorId = authorId, parentFolderId = null)
                Then("메모가 초기화된다") {
                    sutMemo.id shouldNotBe null
                    sutMemo.id.value shouldNotBe 0
                    sutMemo.authorId shouldBe authorId
                }
                Then("persist 되지 않은상태여야한다") {
                    sutMemo.isPersisted shouldBe false
                }
            }
        }

        // context: 메모 업데이트
        Given("유효한 메모가 주어지고") {
            val sutMemo = Memo.init(authorId = AuthorId(1), parentFolderId = null)
            And("업데이트할 제목이 주어지고") {
                val title = MemoTitle("title")
                When("메모 업데이트를 하면") {
                    sutMemo.update(
                        title = title,
                    )
                    Then("메모의 제목이 변경된다") {
                        sutMemo.title shouldBe title
                    }
                }
            }
            And("업데이트할 내용이 주어지고") {
                val content = MemoContent("content")
                When("메모 업데이트를 하면") {
                    sutMemo.update(
                        content = content,
                    )
                    Then("메모의 내용이 변경된다") {
                        sutMemo.content shouldBe content
                    }
                }
            }
        }

        // from
        Given("유효한 메모 데이터가 주어지고") {
            val id = MemoId(1)
            val authorId = AuthorId(1)
            val title = MemoTitle("title")
            val content = MemoContent("content")
            val reference = mutableSetOf<MemoReference>()
            val parentFolderId = FolderId(1)
            val createdAt = null
            val updatedAt = null
            When("메모를 생성하면") {
                val sutMemo =
                    Memo.from(
                        id = id,
                        authorId = authorId,
                        title = title,
                        content = content,
                        reference = reference,
                        parentFolderId = parentFolderId,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                    )
                Then("메모가 생성된다") {
                    sutMemo.id shouldBe id
                    sutMemo.authorId shouldBe authorId
                    sutMemo.title shouldBe title
                    sutMemo.content shouldBe content
                    sutMemo.references shouldBe reference
                    sutMemo.parentFolderId shouldBe parentFolderId
                    sutMemo.createdAt shouldBe createdAt
                    sutMemo.updatedAt shouldBe updatedAt
                }
            }
        }
    }
}
