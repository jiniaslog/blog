package kr.co.jiniaslog.user.domain.auth.token

import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.UserId

interface TokenManger {
    fun generateAccessToken(
        id: UserId,
        roles: Set<Role>,
    ): AccessToken

    fun generateRefreshToken(
        id: UserId,
        roles: Set<Role>,
    ): RefreshToken

    fun validateToken(token: AuthToken): Boolean

    fun extractUserId(token: AuthToken): UserId

    fun getRole(token: AuthToken): Set<Role>
}
