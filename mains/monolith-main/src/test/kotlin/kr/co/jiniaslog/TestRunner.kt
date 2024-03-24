package kr.co.jiniaslog

import org.junit.jupiter.api.Nested

class TestRunner : TestContainerAbstractSkeleton() {
    @Nested
    inner class `메모 서비스 통합 테스트`() : MemoModulesIntegrationTestsSuite()

    @Nested
    inner class `유저 서비스 통합 테스트`() : UserUseCasesIntegrationTestsSuite()

    @Nested
    inner class `블로그 서비스 통합 테스트`() : BlogUseCasesIntegrationTestsSuite()
}
