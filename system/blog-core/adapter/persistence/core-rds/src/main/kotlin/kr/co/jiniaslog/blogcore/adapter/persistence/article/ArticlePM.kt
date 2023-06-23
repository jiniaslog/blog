package kr.co.jiniaslog.blogcore.adapter.persistence.article

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kr.co.jiniaslog.shared.persistence.BasePersistenceModel
import java.time.LocalDateTime

@Entity
@Table(name = "articles")
@NamedEntityGraph(
    name = "article-with-taggings",
    attributeNodes = [NamedAttributeNode("taggings")],
)
class ArticlePM(
    @Id
    @Column(name = "article_id")
    override val id: Long,

    @Column(nullable = false, length = 50, name = "title")
    var title: String,

    @Column(nullable = false, length = 10000, name = "content")
    var content: String,

    @Column(columnDefinition = "bigint default 0", nullable = false, name = "hit")
    var hit: Long,

    @Column(nullable = false, name = "thumbnail_url")
    var thumbnailUrl: String,

    @Column(nullable = false, name = "writer_id")
    var writerId: Long,

    @Column(nullable = false, name = "category_id")
    var categoryId: Long,

    @OneToMany(mappedBy = "articleId", cascade = [CascadeType.ALL], orphanRemoval = true)
    val taggings: MutableSet<TaggingPM> = mutableSetOf(),

    createdDate: LocalDateTime? = null,

    updatedDate: LocalDateTime? = null,
) : BasePersistenceModel(createdDate, updatedDate)
