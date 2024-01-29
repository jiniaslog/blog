package kr.co.jiniaslog

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import kr.co.jiniaslog.user.IGetOAuthRedirectionUrlTests
import kr.co.jiniaslog.user.IRefreshTokenTests
import kr.co.jiniaslog.user.ISignInOAuthUserTests
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested

abstract class UserUseCasesIntegrationTestsSuite : TestContainerAbstractSkeleton() {
    @Nested
    inner class `인증을 위한 리디렉션 url 조회 테스트` : IGetOAuthRedirectionUrlTests()

    @Nested
    inner class `리프레시 토큰을 이용한 토큰 재발급 테스트` : IRefreshTokenTests()

    @Nested
    inner class `유저 인증 테스트` : ISignInOAuthUserTests()

    companion object {
        private lateinit var googleOauthStub: WireMockServer

        @JvmStatic
        @BeforeAll
        fun setupWireMock() {
            googleOauthStub =
                WireMockServer(7779).apply {
                    stubFor(
                        WireMock.post(WireMock.urlEqualTo("/oauth2/token"))
                            .willReturn(
                                WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withHeader("Content-Length", "200")
                                    .withBody(
                                        """
                                        {
                                          "access_token": "accessToken_66037446f5e2",
                                          "expires_in": 100,
                                          "scope": "scope_70804e9d3fbe",
                                          "token_type": "tokenType_5f944efd296c",
                                          "id_token": "idToken_287fdd94b903"
                                        }
                                        """.trimIndent(),
                                    ),
                            ),
                    )

                    stubFor(
                        WireMock.get(WireMock.urlEqualTo("/oauth2/v3/userinfo"))
                            .willReturn(
                                WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(
                                        """
                                        {
                                          "email": "testUser@google.com",
                                          "name": "testUser",
                                          "picture": "picture_c5fb4b173dc6"
                                        }                            
                                        """.trimIndent(),
                                    ),
                            ),
                    )

                    start()
                }
        }
    }
}
