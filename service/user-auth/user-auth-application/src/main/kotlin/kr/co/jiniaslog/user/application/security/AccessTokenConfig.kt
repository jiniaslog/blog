package kr.co.jiniaslog.user.application.security

import kr.co.jiniaslog.user.domain.auth.token.JwtTokenManager
import kr.co.jiniaslog.user.domain.auth.token.TokenManger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class AccessTokenConfig {
    @Value("\${jwt.secret-key}")
    lateinit var secretKey: String

    @Value("\${jwt.token-valid-duration}")
    lateinit var tokenValidDuration: Duration

    @Value("\${jwt.refresh-token-valid-duration}")
    lateinit var refreshTokenValidDuration: Duration

    @Bean
    fun jwtTokenGenerator(): TokenManger {
        return JwtTokenManager(secretKey, tokenValidDuration, refreshTokenValidDuration)
    }
}
