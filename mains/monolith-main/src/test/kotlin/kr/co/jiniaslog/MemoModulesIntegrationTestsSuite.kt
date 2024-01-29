package kr.co.jiniaslog

import kr.co.jiniaslog.memo.outbound.FolderRepository
import kr.co.jiniaslog.memo.outbound.MemoRepository
import kr.co.jiniaslog.memo.usecase.AbstractFolderUseCaseTests
import kr.co.jiniaslog.memo.usecase.MemoAbstractUseCaseTest
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired

abstract class MemoModulesIntegrationTestsSuite : TestContainerAbstractSkeleton() {
    @Nested
    inner class `폴더 유스케이스 통합 테스트`
        @Autowired
        constructor(
            folderRepository: FolderRepository,
        ) : AbstractFolderUseCaseTests(
                folderRepository = folderRepository,
            )

    @Nested
    inner class `메모 유즈케이스 통합 테스트`
        @Autowired
        constructor(
            memoRepository: MemoRepository,
            folderRepository: FolderRepository,
        ) : MemoAbstractUseCaseTest(
                memoRepository = memoRepository,
                folderRepository = folderRepository,
            )
}
