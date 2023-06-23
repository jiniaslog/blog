package kr.co.jiniaslog.blogcore.domain.article

import kr.co.jiniaslog.blogcore.domain.category.CategoryId
import kr.co.jiniaslog.blogcore.domain.draft.DraftArticleId
import kr.co.jiniaslog.blogcore.domain.tag.TagId
import kr.co.jiniaslog.blogcore.domain.user.UserId
import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import java.time.LocalDateTime

class Article private constructor(
    id: ArticleId,
    title: String,
    content: String,
    thumbnailUrl: String,
    userId: UserId,
    categoryId: CategoryId,
    tags: Set<TagId>,
    hit: Long = 0,
    createdAt: LocalDateTime?,
    updatedAt: LocalDateTime?,
) : AggregateRoot<ArticleId>(createdAt, updatedAt) {

    override val id: ArticleId = id

    var title: String = title
        private set

    var content: String = content
        private set

    var hit: Long = hit
        private set

    var thumbnailUrl: String = thumbnailUrl
        private set

    val writerId: UserId = userId

    var categoryId: CategoryId = categoryId
        private set

    var tags: Set<TagId> = tags
        private set

    fun edit(title: String, content: String, thumbnailUrl: String, categoryId: CategoryId, tags: Set<TagId>) {
        ArticleValidatePolicy.validate(
            this.id,
            this.writerId,
            title,
            content,
            thumbnailUrl,
            categoryId,
            tags,
        )
        this.title = title
        this.content = content
        this.thumbnailUrl = thumbnailUrl
        this.categoryId = categoryId
        this.tags = tags
    }

    fun hit() {
        this.hit++
    }

    fun registerPublishedArticleDeletedEvent() {
        registerEvent(PublishedArticleDeletedEvent(this.id, this.writerId))
    }

    object ArticleValidatePolicy {
        fun validate(
            id: ArticleId,
            writerId: UserId,
            title: String,
            content: String,
            thumbnailUrl: String,
            categoryId: CategoryId,
            tags: Set<TagId>,
        ) {
            if (id.value < 1) throw ArticleNotValidException("shouldHaveId")
            if (writerId.value < 1) throw ArticleNotValidException("shouldHaveWriter")
            if (title.isBlank()) throw ArticleNotValidException("shouldHaveTitle")
            if (content.isBlank()) throw ArticleNotValidException("shouldHaveContent")
            if (categoryId.value < 1) throw ArticleNotValidException("shouldHaveCategory")
            if (tags.isEmpty()) throw ArticleNotValidException("shouldHaveTags")
            if (thumbnailUrl.isBlank()) throw ArticleNotValidException("shouldHaveThumbnail")
        }
    }

    object Factory {
        fun newPublishedArticle(
            id: ArticleId,
            writerId: UserId,
            title: String,
            content: String,
            thumbnailUrl: String,
            categoryId: CategoryId,
            tags: Set<TagId>,
            draftArticleId: DraftArticleId?,
        ): Article = ArticleValidatePolicy.validate(
            id,
            writerId,
            title,
            content,
            thumbnailUrl,
            categoryId,
            tags,
        ).let {
            Article(
                id = id,
                userId = writerId,
                title = title,
                content = content,
                thumbnailUrl = thumbnailUrl,
                categoryId = categoryId,
                tags = tags,
                createdAt = null,
                updatedAt = null,
            ).apply {
                registerEvent(PublishedArticleCreatedEvent(this.id, this.writerId, draftArticleId))
            }
        }

        fun from(
            id: ArticleId,
            writerId: UserId,
            title: String,
            content: String,
            hit: Long,
            thumbnailUrl: String,
            categoryId: CategoryId,
            tags: Set<TagId>,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?,
        ): Article = ArticleValidatePolicy.validate(
            id,
            writerId,
            title,
            content,
            thumbnailUrl,
            categoryId,
            tags,
        ).let {
            Article(
                id = id,
                title = title,
                content = content,
                hit = hit,
                thumbnailUrl = thumbnailUrl,
                userId = writerId,
                categoryId = categoryId,
                tags = tags,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}
