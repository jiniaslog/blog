package kr.co.jiniaslog.blogcore.application.category.usecase

import kr.co.jiniaslog.blogcore.application.category.usecase.CategoryCommands.CategoryData
import kr.co.jiniaslog.blogcore.application.category.usecase.CategoryCommands.SyncCategoryCommand
import kr.co.jiniaslog.blogcore.application.infra.TransactionHandler
import kr.co.jiniaslog.blogcore.domain.category.Category
import kr.co.jiniaslog.blogcore.domain.category.CategoryId
import kr.co.jiniaslog.blogcore.domain.category.CategoryIdGenerator
import kr.co.jiniaslog.blogcore.domain.category.CategoryRepository
import kr.co.jiniaslog.shared.core.context.UseCaseInteractor
import kr.co.jiniaslog.shared.core.domain.ResourceNotFoundException
import kr.co.jiniaslog.shared.core.domain.ValidationException

@UseCaseInteractor
class CategoryUseCaseInteractor(
    private val categoryIdGenerator: CategoryIdGenerator,
    private val categoryRepository: CategoryRepository,
    private val transactionHandler: TransactionHandler,
) : CategoryCommands, CategoryQueries {
    override fun syncCategories(command: SyncCategoryCommand) = with(command) {
        command.validate()
        val existingCategoriesMap = categoryRepository.findAll().associateBy { it.label }.toMutableMap()
        val (newCategoriesData, toBeUpdatedCategoriesData) = categoriesData.partition { it.isNew }
        val toBeUpdatedCategoriesAndData = toBeUpdatedCategoriesData.map { data ->
            val category = existingCategoriesMap[data.label] ?: throw ResourceNotFoundException("category ${data.id} not found")
            data to category
        }
        val toBeDeletedCategories = existingCategoriesMap.filter { existingCategory ->
            categoriesData.notContains(existingCategory.value.id)
        }.map { it.value }

        transactionHandler.runInReadCommittedTransaction {
            save(newCategoriesData, existingCategoriesMap)
            update(toBeUpdatedCategoriesAndData, existingCategoriesMap)
            delete(toBeDeletedCategories)
        }
    }

    private fun SyncCategoryCommand.validate() {
        val groupedByParent = categoriesData.groupBy { it.parentLabel }
        for (categories in groupedByParent.values) {
            if (categories.hasDuplicateOrder()) throw ValidationException("Order conflict")
        }
    }

    private fun List<CategoryData>.notContains(id: CategoryId): Boolean =
        this.none { categoryData -> categoryData.id == id }

    private fun List<CategoryData>.hasDuplicateOrder(): Boolean {
        val orders = this.map { it.order }
        return orders.size != orders.toSet().size
    }

    private fun save(newCategoriesData: List<CategoryData>, existingCategories: MutableMap<String, Category>) {
        val parentCategoriesData = newCategoriesData.filter { data -> data.parentLabel == null }
        val childCategoriesData = newCategoriesData - parentCategoriesData

        parentCategoriesData.forEach { data ->
            val category = data.toDomain(parentId = null)
            categoryRepository.save(category)
            existingCategories[category.label] = category
        }

        childCategoriesData.forEach { data ->
            val parent = existingCategories[data.parentLabel]
                ?: throw ResourceNotFoundException("parent category ${data.parentLabel} not found")
            val category = data.toDomain(parent.id)
            categoryRepository.save(category)
            existingCategories[category.label] = category
        }
    }

    private fun CategoryData.toDomain(parentId: CategoryId?): Category = Category.newOne(
        id = categoryIdGenerator.generate(),
        label = label,
        parentId = parentId,
        order = order,
    )

    private fun update(
        toBeUpdatedCategoriesData: List<Pair<CategoryData, Category>>,
        existingCategories: MutableMap<String, Category>,
    ) {
        toBeUpdatedCategoriesData.forEach { (data, category) ->
            val parent = if (data.parentLabel != null) {
                existingCategories[data.parentLabel]
                    ?: throw ResourceNotFoundException("parent category ${data.parentLabel} not found")
            } else {
                null
            }
            category.update(
                label = data.label,
                parentId = parent?.id,
                order = data.order,
            )
            categoryRepository.update(category)
        }
    }

    private fun delete(toBeDeleteCategories: List<Category>) {
        toBeDeleteCategories.forEach { categoryToDelete ->
            categoryRepository.delete(categoryToDelete.id)
        }
    }

    override fun findCategory(categoryId: CategoryId): Category? =
        categoryRepository.findById(categoryId)
}
