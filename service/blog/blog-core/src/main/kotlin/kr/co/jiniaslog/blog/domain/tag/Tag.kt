package kr.co.jiniaslog.blog.domain.tag

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import kr.co.jiniaslog.blog.domain.article.Tagging
import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import kr.co.jiniaslog.shared.core.domain.IdUtils

@Entity
class Tag private constructor(
    id: TagId,
    name: TagName,
) : AggregateRoot<TagId>() {
    @EmbeddedId
    @AttributeOverride(
        column = Column(name = "tag_id"),
        name = "value",
    )
    override val id: TagId = id

    @AttributeOverride(
        column = Column(name = "tag_name"),
        name = "value",
    )
    val tagName: TagName = name

    @ElementCollection
    val tagging: MutableSet<Tagging> = mutableSetOf()

    @PreUpdate
    @PrePersist
    fun validate() {}

    companion object {
        fun newOne(name: TagName): Tag {
            return Tag(TagId(IdUtils.generate()), name)
        }
    }
}
