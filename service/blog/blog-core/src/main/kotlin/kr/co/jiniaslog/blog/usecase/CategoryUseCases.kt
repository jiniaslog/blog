package kr.co.jiniaslog.blog.usecase

import kr.co.jiniaslog.blog.domain.category.CategoryDataHolder

interface UseCasesCategoryFacade :
    IChangeCategories

interface IChangeCategories {
    fun handle(command: Command): Info

    data class Command(
        val requestedFlattenedCategoryData: List<CategoryDataHolder>,
    )

    class Info()
}
