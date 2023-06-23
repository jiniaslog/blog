package kr.co.jiniaslog.blogcore.domain.tag

import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import java.time.LocalDateTime

class Tag private constructor(
    id: TagId,
    label: String,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
) : AggregateRoot<TagId>(createdAt, updatedAt) {

    override val id: TagId = id

    var label: String = label
        private set

    companion object Factory {
        fun newOne(
            id: TagId,
            label: String,
        ): Tag {
            return Tag(
                id = id,
                label = label,
                createdAt = null,
                updatedAt = null,
            )
        }

        fun from(
            id: TagId,
            label: String,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?,
        ): Tag {
            return Tag(
                id = id,
                label = label,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}
