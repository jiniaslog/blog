package kr.co.jiniaslog.user.domain.user

import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.time.LocalDateTime

class User private constructor(
    id: UserId,
    nickName: NickName,
    roles: Set<Role>,
    email: Email,
) : AggregateRoot<UserId>() {
    override val id: UserId = id

    var nickName: NickName = nickName
        private set

    var roles = roles
        private set

    var email: Email = email
        private set

    fun refreshWith(nickName: NickName) {
        takeIf { this.nickName != nickName }
            .let { this.nickName = nickName }
    }

    companion object {
        fun newOne(
            nickName: NickName,
            email: Email,
        ): User {
            return User(
                id = UserId(IdUtils.generate()),
                nickName = nickName,
                roles = setOf(Role.USER),
                email = email,
            )
        }

        fun from(
            id: UserId,
            nickName: NickName,
            roles: Set<Role>,
            email: Email,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?,
        ): User {
            return User(
                id = id,
                nickName = nickName,
                roles = roles,
                email = email,
            ).apply {
                this.createdAt = createdAt
                this.updatedAt = updatedAt
            }
        }
    }
}
