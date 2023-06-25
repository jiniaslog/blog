package kr.co.jiniaslog.blogcore.application.article.usecase

import kr.co.jiniaslog.blogcore.application.article.usecase.ArticleCommands.DeleteArticleCommand
import kr.co.jiniaslog.blogcore.application.article.usecase.ArticleCommands.EditArticleCommand
import kr.co.jiniaslog.blogcore.application.article.usecase.ArticleCommands.EditArticleResult
import kr.co.jiniaslog.blogcore.application.article.usecase.ArticleCommands.PostArticleCommand
import kr.co.jiniaslog.blogcore.application.article.usecase.ArticleCommands.PostArticleResult
import kr.co.jiniaslog.blogcore.application.category.usecase.CategoryQueries
import kr.co.jiniaslog.blogcore.application.infra.TransactionHandler
import kr.co.jiniaslog.blogcore.domain.article.Article
import kr.co.jiniaslog.blogcore.domain.article.ArticleId
import kr.co.jiniaslog.blogcore.domain.article.ArticleIdGenerator
import kr.co.jiniaslog.blogcore.domain.article.ArticleRepository
import kr.co.jiniaslog.blogcore.domain.user.UserServiceClient
import kr.co.jiniaslog.shared.core.context.UseCaseInteractor
import kr.co.jiniaslog.shared.core.domain.ResourceConflictException
import kr.co.jiniaslog.shared.core.domain.ResourceNotFoundException

@UseCaseInteractor
internal class ArticleUseCaseInteractor(
    private val articleIdGenerator: ArticleIdGenerator,
    private val articleRepository: ArticleRepository,
    private val transactionHandler: TransactionHandler,
    private val userServiceClient: UserServiceClient,
    private val categoryQueries: CategoryQueries,
) : ArticleCommands, ArticleQueries {
    override fun post(command: PostArticleCommand): PostArticleResult = with(command) {
        command.isValid()
        val article = command.toDomain()

        transactionHandler.runInReadCommittedTransaction {
            articleRepository.save(article)
        }

        return PostArticleResult(articleId = article.id)
    }

    private fun PostArticleCommand.isValid() {
        if (userServiceClient.doesUserExist(writerId).not()) throw ResourceNotFoundException("User does not exist.")
        val category = categoryQueries.findCategory(categoryId) ?: throw ResourceNotFoundException("Category does not exist.")
        if (category.isRoot) throw ResourceConflictException("Root category cannot be used for article")
    }

    private fun PostArticleCommand.toDomain() = Article.Factory.newPublishedArticle(
        id = articleIdGenerator.generate(),
        writerId = writerId,
        title = title,
        content = content,
        thumbnailUrl = thumbnailUrl,
        categoryId = categoryId,
        tags = tags,
        draftArticleId = draftArticleId,
    )

    override fun edit(command: EditArticleCommand): EditArticleResult = with(command) {
        command.isValid()
        val targetArticle = articleRepository.findById(articleId) ?: throw ResourceNotFoundException()
        targetArticle.edit(
            title = title,
            content = content,
            thumbnailUrl = thumbnailUrl,
            categoryId = categoryId,
            tags = tags,
        )

        transactionHandler.runInReadCommittedTransaction {
            articleRepository.update(targetArticle)
        }

        return EditArticleResult(articleId = targetArticle.id)
    }

    private fun EditArticleCommand.isValid() {
        if (userServiceClient.doesUserExist(writerId).not()) throw ResourceNotFoundException("User does not exist.")
        val category = categoryQueries.findCategory(categoryId) ?: throw ResourceNotFoundException("Category does not exist.")
        if (category.isRoot) throw ResourceConflictException("Root category cannot be used for article")
    }

    override fun delete(command: DeleteArticleCommand) = with(command) {
        val targetArticle = articleRepository.findById(articleId) ?: throw ResourceNotFoundException()

        transactionHandler.runInReadCommittedTransaction {
            targetArticle.registerPublishedArticleDeletedEvent()
            articleRepository.delete(targetArticle.id)
        }
    }

    override fun findArticle(articleId: ArticleId): Article? {
        return articleRepository.findById(articleId)
    }
}
