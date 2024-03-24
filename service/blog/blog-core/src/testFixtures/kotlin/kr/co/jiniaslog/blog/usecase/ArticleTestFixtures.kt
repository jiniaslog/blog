package kr.co.jiniaslog.blog.usecase

import kr.co.jiniaslog.blog.domain.article.Article
import kr.co.jiniaslog.blog.domain.article.ArticleContents
import kr.co.jiniaslog.blog.domain.category.CategoryId
import kr.co.jiniaslog.blog.domain.memo.MemoId
import kr.co.jiniaslog.blog.domain.tag.TagId
import kr.co.jiniaslog.blog.domain.user.UserId
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.time.LocalDateTime

class ArticleTestFixtures {
    fun createArticle(
        memoRefId: MemoId = MemoId(IdUtils.generate()),
        authorId: UserId = UserId(1L),
        categoryId: CategoryId = CategoryId(IdUtils.generate()),
        title: String = "title",
        contents: String = "contents",
        thumbnailUrl: String = "thumbnailUrl",
        tags: List<TagId> =
            listOf(
                TagId(IdUtils.generate()),
            ),
        createdAt: LocalDateTime? = null,
        updatedAt: LocalDateTime? = null,
    ): Article {
        val articleContents =
            ArticleContents(
                title = title,
                contents = contents,
                thumbnailUrl = thumbnailUrl,
            )

        return Article.newOne(
            memoRefId = memoRefId,
            authorId = authorId,
            categoryId = categoryId,
            articleContents = articleContents,
            tags = tags,
        ).apply {
            this.createdAt = createdAt
            this.updatedAt = updatedAt
        }
    }
}
