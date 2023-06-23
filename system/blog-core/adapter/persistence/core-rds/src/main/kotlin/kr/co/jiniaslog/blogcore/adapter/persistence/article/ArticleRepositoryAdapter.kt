package kr.co.jiniaslog.blogcore.adapter.persistence.article

import kr.co.jiniaslog.blogcore.domain.article.Article
import kr.co.jiniaslog.blogcore.domain.article.ArticleId
import kr.co.jiniaslog.blogcore.domain.article.ArticleIdGenerator
import kr.co.jiniaslog.blogcore.domain.article.ArticleRepository
import kr.co.jiniaslog.blogcore.domain.category.CategoryId
import kr.co.jiniaslog.blogcore.domain.tag.TagId
import kr.co.jiniaslog.blogcore.domain.user.UserId
import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.ResourceNotFoundException
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Repository
class ArticleRepositoryAdapter(
    private val jpaArticleRepository: JpaArticleRepository,
    private val idGenerator: IdGenerator,
) : ArticleRepository, ArticleIdGenerator {
    override fun save(newArticle: Article) {
        val articlePM = newArticle.toPm().apply {
            newFlag = true
        }
        jpaArticleRepository.save(articlePM)
        newArticle.syncAuditAfterPersist(
            articlePM.createdDate!!,
            articlePM.updatedDate!!,
        )
    }

    override fun update(article: Article) {
        val toBeUpdated = jpaArticleRepository.findById(articleId = article.id.value)
            .getOrElse { throw ResourceNotFoundException("${article.id} is not found") }
        toBeUpdated.apply {
            title = article.title
            content = article.content
            hit = article.hit
            thumbnailUrl = article.thumbnailUrl
            writerId = article.writerId.value
            categoryId = article.categoryId.value
            taggings.clear()
            taggings.addAll(
                article.tags.map {
                    TaggingPM(id = idGenerator.generate(), articleId = article.id.value, tagId = it.value)
                        .apply { newFlag = true }
                },
            )
        }
        jpaArticleRepository.save(toBeUpdated)
        article.syncAuditAfterPersist(
            toBeUpdated.createdDate!!,
            toBeUpdated.updatedDate!!,
        )
    }

    override fun findById(articleId: ArticleId): Article? =
        jpaArticleRepository.findById(articleId.value).getOrNull()?.toDomain()

    override fun delete(articleId: ArticleId) {
        val target = jpaArticleRepository.findById(articleId.value)
            .getOrElse { throw ResourceNotFoundException("$articleId is not found") }
        jpaArticleRepository.delete(target)
    }

    override fun findAll(): List<Article> {
        return jpaArticleRepository.findAll().map { it.toDomain() }
    }

    override fun generate(): ArticleId {
        return ArticleId(idGenerator.generate())
    }

    private fun ArticlePM.toDomain(): Article {
        return Article.Factory.from(
            id = ArticleId(id),
            title = title,
            content = content,
            hit = hit,
            thumbnailUrl = thumbnailUrl,
            writerId = UserId(writerId),
            categoryId = CategoryId(categoryId),
            tags = taggings.map { TagId(it.tagId) }.toSet(),
            createdAt = createdDate,
            updatedAt = updatedDate,
        )
    }

    private fun Article.toPm(): ArticlePM =
        ArticlePM(
            id = id.value,
            title = title,
            content = content,
            hit = hit,
            thumbnailUrl = thumbnailUrl,
            writerId = writerId.value,
            categoryId = categoryId.value,
            taggings = tags.map { TaggingPM(id = idGenerator.generate(), articleId = id.value, tagId = it.value) }
                .toMutableSet(),
            createdDate = createdDate,
            updatedDate = updatedDate,
        )
}
