package kr.co.jiniaslog.user.domain.auth.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.UserId
import java.security.Key
import java.time.Duration
import java.util.Date
import kotlin.random.Random

class JwtTokenManager(
    secretKey: String,
    tokenValidDuration: Duration,
    refreshTokenValidDuration: Duration,
) : TokenManger {
    private val roleKey = "roles"
    private val tokenValidDuration = tokenValidDuration.toMillis()
    private val refreshTokenValidDuration = refreshTokenValidDuration.toMillis()
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    override fun generateAccessToken(
        id: UserId,
        roles: Set<Role>,
    ): AccessToken {
        val now = getCurrentDate()
        val token = generateToken(id, roles, now, tokenValidDuration)
        return AccessToken(token)
    }

    override fun generateRefreshToken(
        id: UserId,
        roles: Set<Role>,
    ): RefreshToken {
        val now = getCurrentDate()
        val token = generateToken(id, roles, now, refreshTokenValidDuration)
        return RefreshToken(token)
    }

    override fun validateToken(token: AuthToken): Boolean {
        try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token.value)
            return !claims.body.expiration.before(getCurrentDate())
        } catch (e: Exception) {
            return false
        }
    }

    private fun getCurrentDate() = Date()

    override fun extractUserId(token: AuthToken): UserId {
        val claims =
            Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.value)
                .body
        return UserId(claims.subject.toLong())
    }

    override fun getRole(token: AuthToken): Set<Role> {
        val claims =
            Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.value)
                .body
        return claims[roleKey, List::class.java]
            .map { Role.valueOf(it as String) }
            .toSet()
    }

    private fun generateToken(
        id: UserId,
        roles: Set<Role>,
        now: Date,
        tokenValidDuration: Long,
    ): String =
        Jwts.builder()
            .claims()
            .subject(id.value.toString())
            .add(roleKey, roles)
            .id(Random.nextLong().toString())
            .issuedAt(now)
            .expiration(Date(now.time + tokenValidDuration))
            .and()
            .signWith(key)
            .compact()
}
