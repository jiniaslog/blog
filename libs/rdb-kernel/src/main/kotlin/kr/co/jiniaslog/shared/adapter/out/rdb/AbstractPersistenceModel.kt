package kr.co.jiniaslog.shared.adapter.out.rdb

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractPersistenceModel<T>(
    @CreatedDate
    @Column(updatable = false, name = "created_at")
    var createdAt: LocalDateTime?,
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime?,
) : Persistable<Long> {
    abstract val id: Long

    override fun isNew(): Boolean = createdAt == null

    override fun getId(): Long? = id

    fun isPersisted(): Boolean = isNew.not()
}
