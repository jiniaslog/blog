package kr.co.jiniaslog.memo.usecase

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderName
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.outbound.FolderRepository
import kr.co.jiniaslog.memo.outbound.fakes.FakeFolderRepository
import kr.co.jiniaslog.memo.usecase.impl.UseCasesFolderInteractor
import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.IdUtils
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicLong

abstract class AbstractFolderUseCaseTests(
    var folderRepository: FolderRepository,
) {
    private var sut: UseCasesFolderFacade =
        UseCasesFolderInteractor(
            folderRepository = folderRepository,
        )

    @Test
    fun `유효한 폴더 초기화 요청시 폴더는 초기화 된다`() {
        // given
        val command =
            ICreateNewFolder.Command(
                authorId = AuthorId(1),
            )

        // when
        val info = sut.handle(command)

        // then
        info.id shouldNotBe null
        folderRepository.findById(info.id) shouldNotBe null
    }

    @Test
    fun `유효한 폴더가 있고 유효한 폴더 이름 변경 요청이 있으면 폴더이름이 변경된다`() {
        // given
        val folder =
            folderRepository.save(
                Folder.init(
                    authorId = AuthorId(1),
                ),
            )
        val command =
            IChangeFolderName.Command(
                folderId = folder.id,
                name = FolderName("name"),
            )
        // when
        val info = sut.handle(command)
        // then
        info.folderId shouldNotBe null
        folderRepository.findById(info.folderId) shouldNotBe null
    }

    @Test
    fun `유효한 폴더가 있고 유효한 폴더 삭제 요청이 있으면 폴더가 삭제된다`() {
        // given
        val folder =
            folderRepository.save(
                Folder.init(
                    authorId = AuthorId(1),
                ),
            )
        val command =
            IDeleteFoldersRecursively.Command(
                folderId = folder.id,
            )
        // when
        val info = sut.handle(command)
        // then
        info.folderId shouldNotBe null
        folderRepository.findById(info.folderId) shouldBe null
    }

    @Test
    fun `유효한 폴더 관계 설정 요청이 있고 유효한 폴더 둘이 있으면 폴더 관계가 설정된다`() {
        // given
        val folder1 =
            folderRepository.save(
                Folder.init(
                    authorId = AuthorId(1),
                ),
            )
        val folder2 =
            folderRepository.save(
                Folder.init(
                    authorId = AuthorId(1),
                ),
            )
        val command =
            IMakeRelationShipFolderAndFolder.Command(
                parentFolderId = folder1.id,
                childFolderId = folder2.id,
            )
        // when
        val info = sut.handle(command)
        // then
        info.parentFolderId shouldNotBe null
        info.childFolderId shouldNotBe null
        folderRepository.findById(info.parentFolderId!!) shouldNotBe null
        folderRepository.findById(info.childFolderId) shouldNotBe null
        info.parentFolderId shouldBe folder1.id
        info.childFolderId shouldBe folder2.id
    }
}

class FolderUseCaseFakeDependencyTests : AbstractFolderUseCaseTests(
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
