package kr.co.jiniaslog.shared.core.domain

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import java.time.LocalDateTime

abstract class DomainEntity<out T : ValueObject>(
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    abstract val id: T?

    val isPersisted: Boolean
        get() = createdAt != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainEntity<*>

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
