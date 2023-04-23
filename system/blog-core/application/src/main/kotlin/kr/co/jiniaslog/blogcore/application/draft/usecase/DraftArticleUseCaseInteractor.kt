package kr.co.jiniaslog.blogcore.application.draft.usecase

import kr.co.jiniaslog.blogcore.application.infra.TransactionHandler
import kr.co.jiniaslog.blogcore.domain.draft.DraftArticle
import kr.co.jiniaslog.blogcore.domain.draft.DraftArticleId
import kr.co.jiniaslog.blogcore.domain.draft.DraftArticleIdGenerator
import kr.co.jiniaslog.blogcore.domain.draft.DraftArticleRepository
import kr.co.jiniaslog.blogcore.domain.user.UserId
import kr.co.jiniaslog.shared.core.context.UseCaseInteractor
import kr.co.jiniaslog.shared.core.domain.ResourceNotFoundException

@UseCaseInteractor
internal class DraftArticleUseCaseInteractor(
    private val draftArticleIdGenerator: DraftArticleIdGenerator,
    private val draftArticleRepository: DraftArticleRepository,
    private val transactionHandler: TransactionHandler,
) : DraftArticleCommands, DraftArticleQueries {

    override fun upsertDraftArticle(command: DraftArticleCommands.UpsertDraftArticleCommand): DraftArticleCommands.UpsertDraftArticleResult = with(command) {
        val draftArticle = draftArticleId?.let { draftArticleRepository.getById(it) }
            ?.apply {
                update(
                    title = title,
                    content = content,
                    thumbnailUrl = thumbnailUrl,
                    categoryId = categoryId,
                    tags = tags,
                )
            } ?: DraftArticle.Factory.newOne(
            id = draftArticleIdGenerator.generate(),
            writerId = UserId(writerId),
            title = title,
            content = content,
            thumbnailUrl = thumbnailUrl,
            categoryId = categoryId,
        )

        transactionHandler.runInReadCommittedTransaction {
            draftArticleRepository.save(draftArticle)
        }

        return DraftArticleCommands.UpsertDraftArticleResult(draftArticleId = draftArticle.id.value)
    }

    override fun deleteDraftArticle(command: DraftArticleCommands.DeleteDraftArticleCommand) {
        transactionHandler.runInReadCommittedTransaction {
            draftArticleRepository.deleteById(command.draftArticleId)
        }
    }

    override fun getDraftArticleById(draftArticleId: DraftArticleId): DraftArticle =
        draftArticleRepository.getById(draftArticleId) ?: throw ResourceNotFoundException()
}
