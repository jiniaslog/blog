package kr.co.jiniaslog.blogcore.adapter.persistence.article

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.jiniaslog.shared.persistence.BasePersistenceModel

@Entity
@Table(
    name = "temp_article",
)
class TempArticlePM(
    @Id
    @Column(name = "temp_article_id")
    override val id: Long,

    @Column(nullable = true, length = 50, name = "title")
    var title: String?,

    @Column(nullable = true, length = 10000, name = "content")
    var content: String?,

    @Column(nullable = true, name = "thumbnail_url")
    var thumbnailUrl: String?,

    @Column(nullable = false, name = "writer_id")
    var writerId: Long,

    @Column(nullable = true, name = "category_id")
    var categoryId: Long?,
) : BasePersistenceModel()
