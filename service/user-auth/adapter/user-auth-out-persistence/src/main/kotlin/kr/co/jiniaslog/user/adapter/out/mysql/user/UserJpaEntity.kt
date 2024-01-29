package kr.co.jiniaslog.user.adapter.out.mysql.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.jiniaslog.shared.adapter.out.rdb.AbstractPersistenceModel
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.NickName
import kr.co.jiniaslog.user.domain.user.Role
import kr.co.jiniaslog.user.domain.user.User
import kr.co.jiniaslog.user.domain.user.UserId
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class UserJpaEntity(
    @Id
    @Column(name = "id", nullable = false, unique = true)
    override val id: Long,
    @Column(name = "email", nullable = false, unique = true)
    var email: String,
    @Column(name = "nickName", nullable = false)
    var nickName: String,
    @Column(name = "roles", nullable = false)
    var roles: String,
    createdAt: LocalDateTime?,
    updatedAt: LocalDateTime?,
) : AbstractPersistenceModel<Long>(createdAt, updatedAt) {
    fun toDomain(): User {
        return User.from(
            id = UserId(this.id),
            nickName = NickName(this.nickName),
            roles = this.roles.split(",").map { Role.valueOf(it.trim()) }.toSet(),
            email = Email(this.email),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}
