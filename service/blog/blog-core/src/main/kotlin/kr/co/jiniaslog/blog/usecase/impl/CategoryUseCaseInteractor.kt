package kr.co.jiniaslog.blog.usecase.impl

import kr.co.jiniaslog.blog.domain.category.CategorySyncer
import kr.co.jiniaslog.blog.outbound.persistence.BlogTransactionHandler
import kr.co.jiniaslog.blog.outbound.persistence.CategoryRepository
import kr.co.jiniaslog.blog.usecase.IChangeCategories
import kr.co.jiniaslog.blog.usecase.UseCasesCategoryFacade
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor

@UseCaseInteractor
class CategoryUseCaseInteractor(
    private val categoryRepository: CategoryRepository,
    private val categorySyncer: CategorySyncer,
    private val transactionHandler: BlogTransactionHandler,
) : UseCasesCategoryFacade {
    override fun handle(command: IChangeCategories.Command): IChangeCategories.Info {
        val asIsCategories = categoryRepository.findAll()
        val toBeCategoriesData = command.requestedFlattenedCategoryData

        val syncedResult =
            categorySyncer.syncCategories(
                asIs = asIsCategories,
                toBe = toBeCategoriesData,
            )

        transactionHandler.runInRepeatableReadTransaction {
            categoryRepository.deleteAll(syncedResult.toBeDelete)
            categoryRepository.saveAll(syncedResult.toBeUpsert)
        }

        return IChangeCategories.Info()
    }
}
