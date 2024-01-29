package kr.co.jiniaslog.user.domain.auth.token

import io.jsonwebtoken.security.WeakKeyException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.UserId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Duration

class JwtTokenManagerTests {
    @Nested
    inner class `토큰 매니저 생성 테스트`() {
        @Test
        fun `암호키가 32비트 이하이면 초기화에 실패한다`() {
            // given
            val key = "123456789012345678901234567890"
            // when, then
            shouldThrow<WeakKeyException> {
                JwtTokenManager(key, Duration.ofDays(1), Duration.ofDays(1))
            }
        }

        @Test
        fun `암호키가 32비트 초과이면 초기화에 성공한다`() {
            // given
            val key = "12345678901234567890123456789012"
            // when
            JwtTokenManager(key, Duration.ofDays(1), Duration.ofDays(1))
            // then
            assert(true)
        }
    }

    @Nested
    inner class `토큰 테스트` {
        @Test
        fun `유효한 유저 id 로 access 토큰을 생성할 수 있다`() {
            // given
            val userId = UserId(1L)
            val roles = setOf(Role.USER)
            val sut = buildSut()

            // when
            sut.generateAccessToken(userId, roles)

            // then
            assert(true)
        }

        @Test
        fun `생성된 access 토큰의 클레임에는 userId가 포함되어 있다`() {
            // given
            val userId = UserId(1L)
            val roles = setOf(Role.USER)
            val sut = buildSut()
            val accessToken = sut.generateAccessToken(userId, roles)

            // when
            val extractedUserId = sut.extractUserId(accessToken)

            // then
            extractedUserId shouldBe userId
        }

        @Test
        fun `생성된 access 토큰의 클레임에는 roles가 포함되어 있다`() {
            // given
            val userId = UserId(1L)
            val roles = setOf(Role.USER)
            val sut = buildSut()
            val accessToken = sut.generateAccessToken(userId, roles)

            // when
            val extractedRoles = sut.getRole(accessToken)

            // then
            extractedRoles shouldBe roles
        }

        @Test
        fun `주어진 토큰 문자열이 유효하지 않으면 false를 반환한다`() {
            // given
            val sut = buildSut()
            val invalidToken = AccessToken("invalid token")

            // when
            val result = sut.validateToken(invalidToken)

            // then
            result shouldBe false
        }

        @Test
        fun `주어진 토큰이 만료되었으면 false를 반환한다`() {
            // given
            val token =
                AccessToken(
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjYzMTExOTYwMCwiZXhwIjo2MzEyMDYwMDB9.Yi7YVV4wyIdKsw7fgRXQCgUR_TIPAnofp422ALPqSX0",
                )
            val sut = buildSut()

            // when
            val result = sut.validateToken(token)

            // then
            result shouldBe false
        }

        @Test
        fun `주어진 토큰 문자열이 유효하면 true를 반환한다`() {
            // given
            val userId = UserId(1L)
            val roles = setOf(Role.USER)
            val sut = buildSut()
            val accessToken = sut.generateAccessToken(userId, roles)

            // when
            val result = sut.validateToken(accessToken)

            // then
            result shouldBe true
        }
    }

    private fun buildSut() = JwtTokenManager("12345678901234567890123456789012", Duration.ofDays(1), Duration.ofDays(1))
}
