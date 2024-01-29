package kr.co.jiniaslog.user

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import kr.co.jiniaslog.user.application.usecase.IGetOAuthRedirectionUrl
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

abstract class IGetOAuthRedirectionUrlTests {
    @Autowired
    lateinit var sut: IGetOAuthRedirectionUrl

    @Test
    fun `유효한 auth url 요청을 하면 유효한 응답이 온다`() {
        // given
        val command =
            IGetOAuthRedirectionUrl.Command(
                provider = Provider.GOOGLE,
            )

        // when
        val info = sut.handle(command)

        // then
        info shouldNotBe null
        info.url shouldNotBe null
        info.url.value shouldContain "google"
    }
}
