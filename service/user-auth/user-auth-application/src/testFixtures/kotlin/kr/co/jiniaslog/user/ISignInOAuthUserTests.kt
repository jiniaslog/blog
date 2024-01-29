package kr.co.jiniaslog.user

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.jiniaslog.user.application.infra.UserRepository
import kr.co.jiniaslog.user.application.usecase.ISignInOAuthUser
import kr.co.jiniaslog.user.domain.auth.provider.Provider
import kr.co.jiniaslog.user.domain.auth.token.AuthorizationCode
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.NickName
import kr.co.jiniaslog.user.domain.user.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

abstract class ISignInOAuthUserTests {
    @Autowired
    lateinit var sut: ISignInOAuthUser

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `유효한 로그인 시도시 기존 유저가 없으면 회원가입 후 로그인에 성공한다`() {
        // given
        val command =
            ISignInOAuthUser.Command(
                provider = Provider.GOOGLE,
                code = AuthorizationCode("authCode"),
            )

        // when
        val result = sut.handle(command)

        // then
        result.accessToken shouldNotBe null

        val userList = userRepository.findAll()
        userList.size shouldBe 1
        result.nickName shouldBe userList.first().nickName
    }

    @Test
    fun `유효한 로그인 시도시 기존 유저가 있으면 로그인에 성공하며, 항상 새로운 이름으로 갱신된다`() {
        val command =
            ISignInOAuthUser.Command(
                provider = Provider.GOOGLE,
                code = AuthorizationCode("authCode"),
            )
        val email = Email("testUser@google.com")
        userRepository.save(User.newOne(nickName = NickName("test"), email = email))

        // when
        val result = sut.handle(command)

        // then
        val userList = userRepository.findAll()
        userList.size shouldBe 1
        result.nickName shouldBe NickName("testUser")
        result.email shouldBe email
    }
}
