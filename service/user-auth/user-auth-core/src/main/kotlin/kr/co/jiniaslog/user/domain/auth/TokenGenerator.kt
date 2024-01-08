package kr.co.jiniaslog.user.domain.auth

import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.UserId

interface TokenGenerator {
    fun generateAccessToken(
        id: UserId,
        role: Set<Role>,
    ): AccessToken

    fun generateRefreshToken(
        id: UserId,
        role: Set<Role>,
    ): RefreshToken

    fun validateToken(token: String): Boolean

    fun getUserId(token: String): UserId

    fun getRole(token: String): Set<Role>
}
