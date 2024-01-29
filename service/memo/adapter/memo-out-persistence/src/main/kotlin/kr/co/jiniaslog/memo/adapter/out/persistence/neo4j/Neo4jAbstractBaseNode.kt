package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

abstract class Neo4jAbstractBaseNode(
    @CreatedDate
    var createdAt: LocalDateTime?,
    @LastModifiedDate
    var updatedAt: LocalDateTime?,
) : Persistable<Long> {
    abstract val id: Long

    override fun isNew(): Boolean = createdAt == null

    override fun getId(): Long = id
}
