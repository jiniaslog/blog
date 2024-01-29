package kr.co.jiniaslog.user

import kr.co.jiniaslog.user.fakes.UserFakeRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserUseCasesWithFakeTests {
    @Nested
    inner class IGetOAuthRedirectionUrlWithFakeTests : IGetOAuthRedirectionUrlTests()

    @Nested
    inner class IRefreshTokenWithFakeTests : IRefreshTokenTests()

    @Nested
    inner class IGetUserWithFakeTests : ISignInOAuthUserTests() {
        @Autowired
        lateinit var internalUserRepository: UserFakeRepository

        @AfterEach
        fun tearDown() {
            internalUserRepository.tearDown()
        }
    }
}
