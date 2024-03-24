package kr.co.jiniaslog.blog.domain.article

import jakarta.persistence.Embeddable
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import java.io.Serializable

@Embeddable
data class ArticleId(val value: Long) : ValueObject, Serializable {
    @PrePersist
    @PreUpdate
    override fun validate() {
        require(value > 0) { "id must be positive" }
    }
}
