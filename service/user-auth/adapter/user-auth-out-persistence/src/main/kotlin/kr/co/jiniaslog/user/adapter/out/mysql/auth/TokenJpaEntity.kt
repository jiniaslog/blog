package kr.co.jiniaslog.user.adapter.out.mysql.auth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.jiniaslog.shared.adapter.out.rdb.AbstractPersistenceModel
import java.time.LocalDateTime

@Entity
@Table(name = "access_token")
class TokenJpaEntity(
    @Id
    val userId: Long,
    @Column(name = "access_token")
    var accessToken: String,
    @Column(name = "old_refresh_token")
    var oldRefreshToken: String,
    @Column(name = "new_refresh_token")
    var newRefreshToken: String,
    createdAt: LocalDateTime?,
    updatedAt: LocalDateTime?,
) : AbstractPersistenceModel<Long>(createdAt, updatedAt) {
    override val id: Long
        get() = userId
}
