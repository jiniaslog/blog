package kr.co.jiniaslog.blog.usecase

import io.kotest.assertions.NoopErrorCollector.depth
import kr.co.jiniaslog.blog.domain.category.Category
import kr.co.jiniaslog.blog.domain.category.CategoryId
import kr.co.jiniaslog.blog.domain.category.CategoryTitle
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.time.LocalDateTime

class CategoryTestFixtures {
    fun createCategory(
        categoryId: CategoryId = CategoryId(IdUtils.generate()),
        categoryTitle: CategoryTitle = CategoryTitle("카테고리"),
        depth: Int = 0,
        sortingPoint: Int = 0,
        createdAt: LocalDateTime? = null,
        updatedAt: LocalDateTime? = null,
    ): Category {
        return Category(
            categoryId = categoryId,
            categoryTitle = categoryTitle,
            depth = depth,
            sortingPoint = sortingPoint,
        ).apply {
            this.createdAt = createdAt
            this.updatedAt = updatedAt
        }
    }
}
