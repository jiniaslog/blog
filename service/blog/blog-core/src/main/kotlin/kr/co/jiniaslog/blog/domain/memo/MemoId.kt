package kr.co.jiniaslog.blog.domain.memo

import jakarta.persistence.Embeddable
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.io.Serializable
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@Embeddable
data class MemoId(val value: Long) : ValueObject, Serializable {
    @PrePersist
    @PreUpdate
    override fun validate() {
        require(value > 0) { "id must be positive" }
    }
}
