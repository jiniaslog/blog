package kr.co.jiniaslog.user.domain.user

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.shared.SimpleUnitTestContext
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.provider.ProviderUserInfo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserTests : SimpleUnitTestContext() {
    @Nested
    inner class `유저 도메인 모델 생성 테스트` {
        @Test
        fun `유효한 데이터가 주어지면 유저를 생성할 수 있다`() {
            // given
            val nickName = NickName("12345678901")
            val email = Email("jinia91@gmail.com")

            // when
            val sut =
                User.newOne(
                    ProviderUserInfo(
                        nickName = nickName,
                        email = email,
                        picture = null,
                        provider = Provider.GOOGLE,
                    ),
                )

            // then
            sut shouldNotBe null
            sut.id shouldNotBe null
            sut.id shouldBe UserId(1L)
            sut.nickName shouldBe nickName
            sut.email shouldBe email
            sut.roles shouldBe listOf(Role.USER)
        }

        @Test
        fun `유저는 닉네임을 변경할 수 있다`() {
            // given
            val nickName = NickName("12345678901")
            val email = Email("jinia91@gmail.com")

            val sut =
                User.newOne(
                    ProviderUserInfo(
                        nickName = nickName,
                        email = email,
                        picture = null,
                        provider = Provider.GOOGLE,
                    ),
                )

            // when
            sut.refreshWith(
                ProviderUserInfo(
                    nickName = NickName("12345678902"),
                    email = email,
                    picture = null,
                    provider = Provider.GOOGLE,
                ),
            )

            // then
            sut.nickName shouldBe NickName("12345678902")
        }
    }
}