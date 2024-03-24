package kr.co.jiniaslog.blog.adapter.inbound.http

import kr.co.jiniaslog.blog.domain.article.ArticleContents
import kr.co.jiniaslog.blog.domain.article.ArticleId
import kr.co.jiniaslog.blog.domain.category.CategoryId
import kr.co.jiniaslog.blog.domain.memo.MemoId
import kr.co.jiniaslog.blog.domain.tag.TagId
import kr.co.jiniaslog.blog.domain.user.UserId
import kr.co.jiniaslog.blog.usecase.IEditArticle
import kr.co.jiniaslog.blog.usecase.IPostNewArticle

data class ArticlePostRequest(
    val memoRefId: Long,
    val categoryId: Long,
    val title: String,
    val contents: String,
    val thumbnailUrl: String,
    val tags: List<Long>,
) {
    fun toCommand(userId: Long): IPostNewArticle.Command {
        return IPostNewArticle.Command(
            memoRefId = MemoId(memoRefId),
            authorId = UserId(userId),
            categoryId = CategoryId(categoryId),
            articleContents =
                ArticleContents(
                    title = title,
                    contents = contents,
                    thumbnailUrl = thumbnailUrl,
                ),
            tags = tags.map { TagId(it) },
        )
    }
}

data class ArticlePostResponse(
    val articleId: Long,
)

data class ArticleUpdateRequest(
    val categoryId: Long,
    val refMemoId: Long,
    val title: String,
    val contents: String,
    val thumbnailUrl: String,
    val tags: List<Long>,
) {
    fun toCommand(articleId: Long): IEditArticle.Command {
        return IEditArticle.Command(
            articleId = ArticleId(articleId),
            categoryId = CategoryId(categoryId),
            memoRefId = MemoId(refMemoId),
            articleContents =
                ArticleContents(
                    title = title,
                    contents = contents,
                    thumbnailUrl = thumbnailUrl,
                ),
            tags = tags.map { TagId(it) },
        )
    }
}

data class ArticleUpdateResponse(
    val articleId: Long,
)

data class ArticleDeleteResponse(
    val articleId: Long,
)
