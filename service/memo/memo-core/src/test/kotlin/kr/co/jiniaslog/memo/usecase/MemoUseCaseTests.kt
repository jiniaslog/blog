package kr.co.jiniaslog.memo.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.domain.memo.Memo
import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoTitle
import kr.co.jiniaslog.memo.outbound.FolderRepository
import kr.co.jiniaslog.memo.outbound.MemoRepository
import kr.co.jiniaslog.memo.outbound.fakes.FakeFolderRepository
import kr.co.jiniaslog.memo.outbound.fakes.FakeMemoRepository
import kr.co.jiniaslog.memo.usecase.impl.UseCasesMemoInteractor
import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.IdUtils
import org.junit.jupiter.api.Test
import java.awt.SystemColor.info
import java.util.concurrent.atomic.AtomicLong

abstract class MemoAbstractUseCaseTest(
    var memoRepository: MemoRepository,
    var folderRepository: FolderRepository,
) {
    private val sut: UseCasesMemoFacade =
        UseCasesMemoInteractor(
            memoRepository = memoRepository,
            folderRepository = folderRepository,
        )

    @Test
    fun `유효한 폴더 초기화 요청시 폴더는 초기화 된다`() {
        // given
        val command =
            IInitMemo.Command(
                authorId = AuthorId(1),
                parentFolderId = null,
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        memoRepository.findById(info.id) shouldNotBe null
    }

    @Test
    fun `유효한 메모가 주어지고 업데이트할 유효한 제목이 주어지면 메모는 업데이트 된다`() {
        // given
        val memo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val command =
            IUpdateMemo.Command.UpdateForm(
                memoId = memo.id,
                title = MemoTitle("title"),
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        val foundTarget = memoRepository.findById(info.id)
        foundTarget shouldNotBe null
        foundTarget!!.title shouldBe MemoTitle("title")
    }

    @Test
    fun `유효한 메모가 주어지고 유효한 메모를 업데이트하면 메모는 업데이트 된다`() {
        // given
        val memo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val command =
            IUpdateMemo.Command.UpdateForm(
                memoId = memo.id,
                content = MemoContent("content"),
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        val foundTarget = memoRepository.findById(info.id)
        foundTarget shouldNotBe null
        foundTarget!!.content shouldBe MemoContent("content")
    }

    @Test
    fun `유효한 메모가 주어지고 유효한 참조 추가 커맨드가 주어지면 메모는 참조가 추가된다`() {
        // given
        val rootMemo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val referenceTarget =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )

        val command =
            IUpdateMemo.Command.AddReference(
                memoId = rootMemo.id,
                referenceId = referenceTarget.id,
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        val foundTarget = memoRepository.findById(info.id)
        foundTarget shouldNotBe null
        foundTarget!!.references.size shouldBe 1
        foundTarget.references.first().referenceId shouldBe referenceTarget.id
        foundTarget.references.first().rootId shouldBe rootMemo.id
    }

    @Test
    fun `유효한 메모가 주어지고 유효한 참조 추가 커맨드가 주어지면 메모는 참조가 추가된다 2`() {
        // given
        val rootMemo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val referenceTarget =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val referenceTarget2 =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        rootMemo.addReference(referenceTarget.id)
        memoRepository.save(rootMemo)

        val command =
            IUpdateMemo.Command.AddReference(
                memoId = rootMemo.id,
                referenceId = referenceTarget2.id,
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        val foundTarget = memoRepository.findById(info.id)
        foundTarget shouldNotBe null
        foundTarget!!.references.size shouldBe 2
        foundTarget.references.first().referenceId shouldBe referenceTarget.id
        foundTarget.references.first().rootId shouldBe rootMemo.id
        foundTarget.references.last().referenceId shouldBe referenceTarget2.id
        foundTarget.references.last().rootId shouldBe rootMemo.id
    }

    @Test
    fun `유효한 메모가 주어지고 유효한 참조 삭제 커맨드가 주어지면 메모는 참조가 삭제된다`() {
        // given
        val rootMemo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val referenceTarget =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        rootMemo.addReference(referenceTarget.id)
        memoRepository.save(rootMemo)

        val command =
            IUpdateMemo.Command.RemoveReference(
                memoId = rootMemo.id,
                referenceId = referenceTarget.id,
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        val foundTarget = memoRepository.findById(info.id)
        foundTarget shouldNotBe null
        foundTarget!!.references.size shouldBe 0
    }

    @Test
    fun `유효한 메모가 주어지고 자기자신을 참조하면 실패한다`() {
        // given
        val rootMemo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )

        val command =
            IUpdateMemo.Command.AddReference(
                memoId = rootMemo.id,
                referenceId = rootMemo.id,
            )

        // when
        // then
        shouldThrow<IllegalArgumentException> {
            sut.handle(command)
        }
    }

    @Test
    fun `유효한 메모가 주어지고 삭제 명령을 하면 메모는 삭제된다`() {
        // given
        val memo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val command =
            IDeleteMemo.Command(
                id = memo.id,
            )

        // when
        sut.handle(command)

        // then
        memoRepository.findById(memo.id) shouldBe null
    }

    @Test
    fun `유효한 메모가 주어지고 부모 폴더를 설정하면 부모폴더가 메모에 설정된다`() {
        // given
        val folder =
            folderRepository.save(
                kr.co.jiniaslog.memo.domain.folder.Folder.init(
                    authorId = AuthorId(1),
                ),
            )
        val memo =
            memoRepository.save(
                Memo.init(
                    authorId = AuthorId(1),
                    parentFolderId = null,
                ),
            )
        val command =
            IMakeRelationShipFolderAndMemo.Command(
                memoId = memo.id,
                folderId = folder.id,
            )

        // when
        val info = sut.handle(command)

        // then
        info.memoId shouldNotBe null
        info.memoId shouldBe memo.id
        info.folderId shouldBe folder.id
        val foundedMemo = memoRepository.findById(info.memoId)
        foundedMemo shouldNotBe null
        foundedMemo!!.parentFolderId shouldBe folder.id
    }
}

class MemoUseCaseFakeDependencyTest : MemoAbstractUseCaseTest(
    memoRepository = FakeMemoRepository(),
    folderRepository = FakeFolderRepository(),
) {
    companion object {
        private var atomicSequence = AtomicLong(1)

        init {
            IdUtils.idGenerator =
                object : IdGenerator {
                    override fun generate(): Long {
                        return atomicSequence.getAndIncrement()
                    }
                }
        }
    }
}
