package kr.co.jiniaslog.blogcore.adapter.http.category

import kr.co.jiniaslog.blogcore.application.category.usecase.CategoryCommands
import kr.co.jiniaslog.blogcore.domain.category.CategoryId

data class CategorySyncApiRequest(
    val categories: List<CategoryApiData>,
) {
    fun toCommand(): CategoryCommands.SyncCategoryCommand {
        return CategoryCommands.SyncCategoryCommand(
            categoriesData = categories.map { it.toVo() },
        )
    }
}

data class CategoryApiData(
    val id: Long?,
    val label: String,
    val order: Int,
    val parentLabel: String?,
) {
    fun toVo(): CategoryCommands.CategoryData {
        return CategoryCommands.CategoryData(
            id = id?.let { CategoryId(it) },
            label = label,
            order = order,
            parentLabel = parentLabel,
        )
    }
}
