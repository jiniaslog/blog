package kr.co.jiniaslog.folder.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderName
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.shared.CustomBehaviorSpec

class FolderTests : CustomBehaviorSpec() {
    init {
        Given("유효한 폴더 생성 조건이 주어지면") {
            val authorId = AuthorId(1)
            When("폴더를 생성하면") {
                val newFolder = Folder.init(authorId)
                Then("폴더가 생성된다.") {
                    newFolder shouldNotBe null
                    newFolder.id shouldNotBe null
                    newFolder.parent shouldBe null
                    newFolder.name shouldBe FolderName.UNTITLED
                }
            }
            And("특정 폴더 하위이면") {
                val parentFolder = Folder.init(authorId)
                When("폴더를 생성하면") {
                    val newFolder = Folder.init(authorId, parentFolder.id)
                    Then("폴더가 생성된다.") {
                        newFolder shouldNotBe null
                        newFolder.id shouldNotBe null
                        newFolder.name shouldBe FolderName.UNTITLED
                        newFolder.parent shouldBe parentFolder.id
                    }
                }
            }
        }

        Given("두 폴더가 존재하고 상하관계가 존재하면") {
            val authorId = AuthorId(1)
            val parentFolder = Folder.init(authorId)
            val childFolder = Folder.init(authorId)
            childFolder.changeParent(parentFolder)
            When("순환 참조를 하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<IllegalArgumentException> {
                        parentFolder.changeParent(childFolder)
                    }
                }
            }
        }
    }
}
