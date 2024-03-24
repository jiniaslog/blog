package kr.co.jiniaslog.blog.domain.category

import jakarta.persistence.Embeddable
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@Embeddable
data class CategoryId(
    val value: Long,
) : ValueObject {
    @PrePersist
    @PreUpdate
    override fun validate() {
        require(value > 0) { "id must be positive" }
    }
}
