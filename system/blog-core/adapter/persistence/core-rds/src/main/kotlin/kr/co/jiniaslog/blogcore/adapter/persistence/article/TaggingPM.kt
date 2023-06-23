package kr.co.jiniaslog.blogcore.adapter.persistence.article

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.jiniaslog.shared.persistence.BasePersistenceModel
import java.time.LocalDateTime

@Entity
@Table(name = "taggings")
class TaggingPM(
    @Id
    @Column(name = "tagging_id")
    override val id: Long,

    @Column(nullable = false, name = "article_id")
    var articleId: Long,

    @Column(nullable = false, name = "tag_id")
    var tagId: Long,

    createdDate: LocalDateTime? = null,

    updatedDate: LocalDateTime? = null,
) : BasePersistenceModel(createdDate, updatedDate)
