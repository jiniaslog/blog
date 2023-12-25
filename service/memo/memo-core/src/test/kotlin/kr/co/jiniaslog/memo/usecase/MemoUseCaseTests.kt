package kr.co.jiniaslog.memo.usecase

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderRepository
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.domain.memo.Memo
import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoRepository
import kr.co.jiniaslog.memo.domain.memo.MemoTitle
import kr.co.jiniaslog.memo.fakes.FakeFolderRepository
import kr.co.jiniaslog.memo.fakes.FakeMemoRepository
import kr.co.jiniaslog.memo.usecase.impl.UseCasesMemoInteractor
import kr.co.jiniaslog.shared.CustomBehaviorSpec

internal class MemoUseCaseTests : CustomBehaviorSpec() {
    private var memoRepository: MemoRepository = FakeMemoRepository()
    private var folderRepository: FolderRepository = FakeFolderRepository()
    private val sut: UseCasesMemoFacade =
        UseCasesMemoInteractor(
            memoRepository = memoRepository,
            folderRepository = folderRepository,
        )

    init {
        /**
         * I Init Memo
         */
        Given("새 메모 작성을 위한 유효한 입력값이 주어지고") {
            val command = prepareValidMemoInitializationCommand()
            When("새 메모를 초기화하는 요청을 처리하면") {
                val info = sut.handle(command)
                Then("새로운 메모가 정상적으로 생성되어야 한다.") {
                    info.id shouldNotBe null
                    val retrievedMemo = memoRepository.findById(info.id) shouldNotBe null
                    retrievedMemo!!.id shouldBe info.id
                }
            }
        }

        Given("새 메모 작성을 위한 입력값이 제목만 주어지고") {
            val command =
                IInitMemo.Command(
                    authorId = AuthorId(1),
                    title = MemoTitle("title"),
                )
            When("새 메모를 초기화하는 요청을 처리하면") {
                val info = sut.handle(command)
                Then("새로운 메모가 정상적으로 생성되어야 한다.") {
                    info.id shouldNotBe null
                    memoRepository.findById(info.id) shouldNotBe null
                }
            }
        }

        /**
         * I Update Memo
         */
        Given("유효한 메모가 주어지고") {
            val memo =
                Memo.init(
                    authorId = AuthorId(1),
                )
            memoRepository.save(memo)

            And("업데이트할 제목이 주어지고") {
                val command =
                    IUpdateMemo.Command.UpdateForm(
                        memoId = memo.id,
                        title = MemoTitle("title"),
                    )
                When("메모 업데이트를 하면") {
                    sut.handle(command)
                    Then("메모의 제목이 변경된다") {
                        val foundMemo = memoRepository.findById(memo.id)!!
                        foundMemo.title shouldBe MemoTitle("title")
                        foundMemo.content shouldBe MemoContent("")
                    }
                }
            }
            And("업데이트할 내용이 주어지고") {
                val command =
                    IUpdateMemo.Command.UpdateForm(
                        memoId = memo.id,
                        content = MemoContent("content"),
                    )
                When("메모 업데이트를 하면") {
                    sut.handle(command)
                    Then("메모의 내용이 변경된다") {
                        val foundMemo = memoRepository.findById(memo.id)!!
                        foundMemo.title shouldBe MemoTitle("")
                        foundMemo.content shouldBe MemoContent("content")
                    }
                }
            }
            And("참조 추가 커맨드가 주어지고") {
                val reference =
                    Memo.init(
                        authorId = AuthorId(1),
                    )
                memoRepository.save(reference)
                val command =
                    IUpdateMemo.Command.AddReference(
                        memoId = memo.id,
                        referenceId = reference.id,
                    )
                When("메모 업데이트를 하면") {
                    sut.handle(command)
                    Then("메모의 참조가 추가된다") {
                        val foundMemo = memoRepository.findById(memo.id)!!
                        foundMemo.references.size shouldBe 1
                        foundMemo.references.first().referenceId shouldBe reference.id
                    }
                }
            }
            And("참조 삭제 커맨드가 주어지고") {
                val reference =
                    Memo.init(
                        authorId = AuthorId(1),
                    )
                memoRepository.save(reference)
                memo.addReference(reference.id)
                val command =
                    IUpdateMemo.Command.RemoveReference(
                        memoId = memo.id,
                        referenceId = reference.id,
                    )
                When("메모 업데이트를 하면") {
                    sut.handle(command)
                    Then("메모의 참조가 삭제된다") {
                        val foundMemo = memoRepository.findById(memo.id)!!
                        foundMemo.references.size shouldBe 0
                    }
                }
            }
        }

        /**
         * I Delete Memo
         */
        Given("유효한 메모가 하나 주어지고") {
            val memo =
                Memo.init(
                    authorId = AuthorId(1),
                )
            memoRepository.save(memo)
            val command =
                IDeleteMemo.Command(
                    id = memo.id,
                )
            When("메모 삭제를 하면") {
                sut.handle(command)
                Then("메모가 삭제된다") {
                    memoRepository.findById(memo.id) shouldBe null
                }
            }
        }

        /**
         * I Make Relationship Folder And Memo
         */
        Given("유효한 커맨드가 주어지고") {
            val memo =
                Memo.init(
                    authorId = AuthorId(1),
                )
            memoRepository.save(memo)
            val folder =
                folderRepository.save(
                    Folder.init(
                        authorId = AuthorId(1),
                    ),
                )
            And("유효한 커맨드가 주어지면") {
                val command =
                    IMakeRelationShipFolderAndMemo.Command(
                        memoId = memo.id,
                        folderId = folder.id,
                    )
                When("메모와 폴더 관계를 설정할때") {
                    sut.handle(command)
                    Then("메모와 폴더가 관계 설정된다") {
                        val foundMemo = memoRepository.findById(memo.id)!!
                        foundMemo.parentFolderId shouldBe folder.id
                    }
                }
            }
        }

        /**
         * I Update References
         */
        Given("I Update References: 유효한 메모가 주어지고") {
            val memo =
                Memo.init(
                    authorId = AuthorId(1),
                )
            memoRepository.save(memo)
            val reference =
                Memo.init(
                    authorId = AuthorId(1),
                )
            memoRepository.save(reference)
            memo.addReference(reference.id)
            val command =
                IUpdateMemo.Command.UpdateReferences(
                    memoId = memo.id,
                    references = setOf(reference.id),
                )
            When("메모의 참조를 업데이트하면") {
                sut.handle(command)
                Then("메모의 참조가 변경된다") {
                    val foundMemo = memoRepository.findById(memo.id)!!
                    foundMemo.references.size shouldBe 1
                    foundMemo.references.first().referenceId shouldBe reference.id
                }
            }
        }
    }

    private fun prepareValidMemoInitializationCommand() =
        IInitMemo.Command(
            content = MemoContent("content"),
            authorId = AuthorId(1),
            title = MemoTitle("title"),
        )
}
