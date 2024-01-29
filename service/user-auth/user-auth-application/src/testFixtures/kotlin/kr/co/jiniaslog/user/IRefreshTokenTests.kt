package kr.co.jiniaslog.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.auth.token.TokenManger
import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.UserId
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

abstract class IRefreshTokenTests {
    @Autowired
    lateinit var sut: IRefreshToken

    @Autowired
    lateinit var tokenManger: TokenManger

    @Autowired
    lateinit var tokenStore: TokenStore

    @Test
    fun `포멧이 유효하지 않은 리프레시 토큰으로 갱신 요청을 하면 실패한다`() {
        // given
        val command =
            IRefreshToken.Command(
                refreshToken = RefreshToken("12345678901"),
            )

        // when, then
        shouldThrow<IllegalArgumentException> {
            sut.handle(command)
        }
    }

    @Test
    fun `포멧이 유효한 리프레시 토큰이여도 스토어에 저장되어있지 않다면 실패한다`() {
        // given
        val refreshToken = tokenManger.generateRefreshToken(UserId(1L), setOf(Role.USER))

        val command =
            IRefreshToken.Command(
                refreshToken = refreshToken,
            )

        // when, then
        shouldThrow<IllegalArgumentException> {
            sut.handle(command)
        }
    }

    @Test
    fun `저장된 리프레시 토큰이 존재하고, 캐시 유효기간 이내에 이전 리프레시 토큰으로 재발급 요청시 캐싱데이터를 사용한다`() {
        // given context
        val userId = UserId(1L)
        val accessToken = tokenManger.generateAccessToken(userId, setOf(Role.USER))
        val tempRefreshToken = tokenManger.generateRefreshToken(userId, setOf(Role.USER))
        tokenStore.save(userId, accessToken, tempRefreshToken)
        val newRefreshToken = tokenManger.generateRefreshToken(userId, setOf(Role.USER))
        tokenStore.save(userId, accessToken, newRefreshToken)

        val command =
            IRefreshToken.Command(
                refreshToken = tempRefreshToken,
            )

        // when
        val info = sut.handle(command)

        // then
        info.accessToken shouldNotBe null
        info.refreshToken shouldNotBe null

        // 갱신 체크
        info.accessToken.value shouldBe accessToken.value
        info.refreshToken.value shouldBe newRefreshToken.value
        info.refreshToken.value shouldNotBe tempRefreshToken.value

        // tearDown
        tokenStore.delete(userId)
    }

    @Test
    @Disabled("테스트 시간이 오래걸려서 로컬에서만 확인")
    fun `저장된 리프레시 토큰이 존재하고, 캐시 유효기간 이후 리프레시토큰으로 재발급 요청시 재발급한다`() {
        // given context
        val userId = UserId(1L)
        val accessToken = tokenManger.generateAccessToken(userId, setOf(Role.USER))
        val refreshToken = tokenManger.generateRefreshToken(userId, setOf(Role.USER))
        tokenStore.save(userId, accessToken, refreshToken)

        Thread.sleep(6000)

        val command =
            IRefreshToken.Command(
                refreshToken = refreshToken,
            )

        // when
        val info = sut.handle(command)

        // then
        info.accessToken shouldNotBe null
        info.refreshToken shouldNotBe null

        // 갱신 체크
        info.accessToken.value shouldNotBe accessToken.value
        info.refreshToken.value shouldNotBe refreshToken.value

        // tearDown
        tokenStore.delete(userId)
    }

    @Test
    @Disabled("테스트 시간이 오래걸려서 로컬에서만 확인")
    fun `동일 유저아이디에 대한 동시성 요청시, 후속 요청은 캐싱을 사용한다`() {
        // given
        val userId = UserId(1L)
        val accessToken = tokenManger.generateAccessToken(userId, setOf(Role.USER))
        val refreshToken = tokenManger.generateRefreshToken(userId, setOf(Role.USER))
        tokenStore.save(userId, accessToken, refreshToken)

        Thread.sleep(6000)

        val executorService = Executors.newFixedThreadPool(2)

        val command =
            IRefreshToken.Command(
                refreshToken = refreshToken,
            )

        // when
        val task1 =
            CompletableFuture.supplyAsync({
                sut.handle(command)
            }, executorService)
        val task2 =
            CompletableFuture.supplyAsync({
                sut.handle(command)
            }, executorService)

        // then
        val result: IRefreshToken.Info = task1.get()
        val result2: IRefreshToken.Info = task2.get()

        result.accessToken.value shouldNotBe accessToken.value
        result.refreshToken.value shouldNotBe refreshToken.value

        result.accessToken.value shouldBe result2.accessToken.value
        result.refreshToken.value shouldBe result2.refreshToken.value

        // tearDown
        tokenStore.delete(userId)
    }
}
