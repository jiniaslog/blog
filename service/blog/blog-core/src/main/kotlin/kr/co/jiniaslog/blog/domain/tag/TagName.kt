package kr.co.jiniaslog.blog.domain.tag

import jakarta.persistence.Embeddable
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import java.io.Serializable

@Embeddable
data class TagName(val value: String) : ValueObject, Serializable {
    @PreUpdate
    @PrePersist
    override fun validate() {
        require(value.length <= 20) { "태그 이름은 20자를 넘을 수 없습니다." }
    }

    companion object {
        val EMPTY = TagName("")
    }
}
