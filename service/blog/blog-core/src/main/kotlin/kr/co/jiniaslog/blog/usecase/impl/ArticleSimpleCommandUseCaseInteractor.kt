package kr.co.jiniaslog.blog.usecase.impl

import kr.co.jiniaslog.blog.domain.article.Article
import kr.co.jiniaslog.blog.domain.article.ArticleId
import kr.co.jiniaslog.blog.outbound.MemoService
import kr.co.jiniaslog.blog.outbound.UserService
import kr.co.jiniaslog.blog.outbound.persistence.ArticleRepository
import kr.co.jiniaslog.blog.outbound.persistence.BlogTransactionHandler
import kr.co.jiniaslog.blog.outbound.persistence.CategoryRepository
import kr.co.jiniaslog.blog.usecase.ArticleSimpleCommandsFacade
import kr.co.jiniaslog.blog.usecase.IDeleteArticle
import kr.co.jiniaslog.blog.usecase.IEditArticle
import kr.co.jiniaslog.blog.usecase.IPostNewArticle
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor

@UseCaseInteractor
class ArticleSimpleCommandUseCaseInteractor(
    private val memoService: MemoService,
    private val userService: UserService,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionHandler: BlogTransactionHandler,
) : ArticleSimpleCommandsFacade {
    override fun handle(command: IPostNewArticle.Command): IPostNewArticle.Info {
        command.validate()
        val article = command.toArticle()

        transactionHandler.runInRepeatableReadTransaction {
            articleRepository.save(article)
        }

        return IPostNewArticle.Info(article.id)
    }

    private fun IPostNewArticle.Command.validate() {
        memoRefId?.let { require(memoService.isExistMemo(this.memoRefId)) { "memo not found" } }
        require(userService.isExistUser(this.authorId)) { "user not found" }
        requireNotNull(categoryRepository.findById(this.categoryId)) { "category not found" }
    }

    private fun IPostNewArticle.Command.toArticle(): Article {
        return Article.newOne(
            memoRefId = this.memoRefId,
            authorId = this.authorId,
            categoryId = this.categoryId,
            articleContents = this.articleContents,
            tags = this.tags,
        )
    }

    override fun handle(command: IEditArticle.Command): IEditArticle.Info {
        command.validate()
        val article = getArticleBy(command.articleId)

        article.edit(
            command.memoRefId,
            command.categoryId,
            command.articleContents,
            command.tags,
        )

        transactionHandler.runInRepeatableReadTransaction {
            articleRepository.save(article)
        }

        return IEditArticle.Info(article.id)
    }

    private fun IEditArticle.Command.validate() {
        memoRefId?.let { require(memoService.isExistMemo(this.memoRefId)) { "memo not found" } }
        requireNotNull(categoryRepository.findById(this.categoryId)) { "category not found" }
    }

    override fun handle(command: IDeleteArticle.Command): IDeleteArticle.Info {
        val article = getArticleBy(command.articleId)

        transactionHandler.runInRepeatableReadTransaction {
            articleRepository.deleteById(article.id)
        }

        return IDeleteArticle.Info()
    }

    private fun getArticleBy(articleId: ArticleId) = (
        articleRepository.findById(articleId)
            ?: throw IllegalArgumentException("article not found")
    )
}
