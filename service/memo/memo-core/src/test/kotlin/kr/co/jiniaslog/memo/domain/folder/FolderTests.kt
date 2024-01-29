package kr.co.jiniaslog.memo.domain.folder

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.shared.CustomBehaviorSpec

class FolderTests : CustomBehaviorSpec() {
    init {
        /**
         * init Folder
         */
        Given("유효한 폴더 생성 조건이 주어지고") {
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
            And("특정 폴더 하위이고") {
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

        /**
         * change parent
         */
        Given("폴더와 부모폴더가 존재하고") {
            val authorId = AuthorId(1)
            val parentFolder = Folder.init(authorId)
            And("하위 폴더가 존재하고") {
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
            And("자기 자신을 부모로 설정하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<IllegalArgumentException> {
                        parentFolder.changeParent(parentFolder)
                    }
                }
            }
        }
        Given("유효힌 폴더와 부모폴더가 존재하고") {
            val authorId = AuthorId(1)
            val parentFolder = Folder.init(authorId)
            And("하위 폴더가 존재하고") {
                val childFolder = Folder.init(authorId)
                childFolder.changeParent(parentFolder)
                When("부모를 변경하면") {
                    val newParentFolder = Folder.init(authorId)
                    childFolder.changeParent(newParentFolder)
                    Then("부모가 변경된다.") {
                        childFolder.parent shouldBe newParentFolder.id
                    }
                }
            }
        }

        /**
         * change name
         */
        Given("유효한 폴더가 하나 존재하고") {
            val authorId = AuthorId(1)
            val folder = Folder.init(authorId)
            When("폴더 이름을 변경하면") {
                val newFolderName = FolderName("newFolderName")
                folder.changeName(newFolderName)
                Then("폴더 이름이 변경된다.") {
                    folder.name shouldBe newFolderName
                }
            }
        }

        /**
         * from
         */
        Given("유효한 폴더 데이터가 전부 존재하고") {
            val authorId = AuthorId(1)
            val folderId = FolderId(1)
            val folderName = FolderName("folderName")
            val parentFolderId = FolderId(2)

            When("폴더를 from으로 생성하면") {
                val folder =
                    Folder.from(
                        id = folderId,
                        name = folderName,
                        authorId = authorId,
                        parent = parentFolderId,
                        createdAt = null,
                        updatedAt = null,
                    )
                Then("폴더가 생성된다.") {
                    folder.id shouldBe folderId
                    folder.name shouldBe folderName
                    folder.authorId shouldBe authorId
                    folder.parent shouldBe parentFolderId
                }
            }
        }
    }
}
