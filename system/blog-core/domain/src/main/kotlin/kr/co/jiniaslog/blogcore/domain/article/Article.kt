package kr.co.jiniaslog.blogcore.domain.article

import kr.co.jiniaslog.blogcore.domain.category.CategoryId
import kr.co.jiniaslog.blogcore.domain.tag.TagId
import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import kr.co.jiniaslog.shared.core.extentions.shouldBe
import java.time.LocalDateTime

class Article private constructor(
    id: ArticleId,
    title: String,
    content: String,
    thumbnailUrl: String?,
    userId: UserId,
    categoryId: CategoryId?,
    tags: Set<TagId>,
) : AggregateRoot<ArticleId>() {

    override val id: ArticleId = id
    var title: String = title
        private set
    var content: String = content
        private set
    var hit: Long = 0L
        private set
    var thumbnailUrl: String? = thumbnailUrl
        private set
    val writerId: UserId = userId
    var categoryId: CategoryId? = categoryId
        private set
    var tags: Set<TagId> = tags
        private set
    var status: ArticleStatus = ArticleStatus.DRAFT
        private set

    val updatedDate: LocalDateTime? = null

    val createdDate: LocalDateTime? = null

    enum class ArticleStatus {
        PUBLISHED, DRAFT
    }

    fun edit(title: String, content: String, thumbnailUrl: String?, categoryId: CategoryId?, tags: Set<TagId>) {
        when (this.status) {
            ArticleStatus.PUBLISHED -> ArticleValidatePolicy.validatePublishingArticle(this.writerId, title, content, thumbnailUrl, categoryId, tags)
            ArticleStatus.DRAFT -> ArticleValidatePolicy.validateDraftingArticle(this.writerId, title, content)
        }

        this.title = title
        this.content = content
        this.thumbnailUrl = thumbnailUrl
        this.categoryId = categoryId
        this.tags = tags
        registerEvent(ArticleEditedEvent(this.id, this.writerId))
    }

    fun publish() {
        ArticleValidatePolicy.validatePublishingArticle(this.writerId, this.title, this.content, this.thumbnailUrl, this.categoryId, this.tags)
        this.status = ArticleStatus.PUBLISHED
        registerEvent(ArticlePublishedEvent(this.id, this.writerId))
    }

    fun drafting() {
        ArticleValidatePolicy.validateDraftingArticle(this.writerId, this.title, this.content)
        this.status = ArticleStatus.DRAFT
        registerEvent(ArticleDraftEvent(this.id, this.writerId))
    }

    fun hit() {
        if (status == ArticleStatus.DRAFT) return
        this.hit++
    }

    object ArticleValidatePolicy {

        fun validateDraftingArticle(writerId: UserId, title: String, content: String) =
            defaultValidate(writerId, title, content)

        fun validatePublishingArticle(writerId: UserId, title: String, content: String, thumbnailUrl: String?, categoryId: CategoryId?, tags: Set<TagId>) {
            defaultValidate(writerId, title, content)
            shouldBe<ArticleNotValidException>(categoryId != CategoryId(0) && categoryId != null) { "shouldHaveCategory" }
            shouldBe<ArticleNotValidException>(tags.isNotEmpty()) { "shouldHaveTags" }
            shouldBe<ArticleNotValidException>(thumbnailUrl != null) { "shouldHaveThumbnail" }
        }

        private fun defaultValidate(writerId: UserId, title: String, content: String) {
            shouldBe<ArticleNotValidException>(writerId != UserId(0)) { "shouldHaveWriter" }
            shouldBe<ArticleNotValidException>(title.isNotBlank()) { "shouldHaveTitle" }
            shouldBe<ArticleNotValidException>(content.isNotBlank()) { "shouldHaveContent" }
        }
    }

    object Factory {

        fun newDraftOne(
            id: ArticleId,
            userId: UserId,
            title: String,
            content: String,
            thumbnailUrl: String?,
            categoryId: CategoryId?,
            tags: Set<TagId>,
        ): Article {
            val newOne = Article(
                id = id,
                userId = userId,
                title = title,
                content = content,
                thumbnailUrl = thumbnailUrl,
                categoryId = categoryId,
                tags = tags,
            )
            newOne.drafting()
            return newOne
        }

        fun newPublishedArticle(
            id: ArticleId,
            userId: UserId,
            title: String,
            content: String,
            thumbnailUrl: String,
            categoryId: CategoryId,
            tags: Set<TagId>,
        ): Article {
            val newOne = Article(
                id = id,
                userId = userId,
                title = title,
                content = content,
                thumbnailUrl = thumbnailUrl,
                categoryId = categoryId,
                tags = tags,
            )
            newOne.publish()
            return newOne
        }
    }
}