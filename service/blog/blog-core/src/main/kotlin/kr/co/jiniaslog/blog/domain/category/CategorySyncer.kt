package kr.co.jiniaslog.blog.domain.category

import kr.co.jiniaslog.shared.core.annotation.DomainService
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.util.LinkedList
import java.util.Queue

/**
 * 카테고리 동기화를 위한 도메인 서비스
 *
 * 카테고리 변경에 대한 요청 데이터 자료구조가 어떻게 되있든 상관없이 평탄화와 정렬하여 현재 상태와 비교하여
 *
 * - 삭제할 카테고리 목록
 * - 추가 및 수정할 카테고리 목록
 * 을 분리하여 반환한다.
 *
 */
@DomainService
class CategorySyncer {
    fun syncCategories(
        asIs: List<Category>,
        toBe: List<CategoryDataHolder>,
    ): SyncResult {
        val sortedToBe = toBe.flattenAndSorted()

        val asIsMap = asIs.associateBy { it.id }
        val toBeMap =
            sortedToBe
                .associateBy { it.categoryId ?: CategoryId(IdUtils.generate()) }

        val toBeDelete = getToBeDeleted(toBeMap, asIsMap)
        val toBeUpsert: List<Category> = getToBeUpsert(toBeMap, asIsMap)

        return SyncResult(toBeDelete, toBeUpsert)
    }

    private fun List<CategoryDataHolder>.flattenAndSorted(): List<CategoryDataHolder> {
        val result = mutableListOf<CategoryDataHolder>()

        fun flattenAndSortRecursively(category: CategoryDataHolder) {
            result.add(category)
            category.children.sortedBy { it.sortingPoint }.forEach { flattenAndSortRecursively(it) }
        }

        this.sortedBy { it.sortingPoint }
            .forEach { flattenAndSortRecursively(it) }

        return result
    }

    private fun getToBeDeleted(
        toBeMap: Map<CategoryId, CategoryDataHolder>,
        asIsMap: Map<CategoryId, Category>,
    ): List<Category> {
        val toBeIds = toBeMap.keys
        val asIsIds = asIsMap.keys

        val toDelete = asIsIds - toBeIds
        val toBeDelete = toDelete.map { asIsMap.getValue(it) }
        return toBeDelete
    }

    private fun getToBeUpsert(
        toBeMap: Map<CategoryId, CategoryDataHolder>,
        asIsMap: Map<CategoryId, Category>,
    ): List<Category> {
        val queue =
            toBeMap.map { (categoryId, categoryVo) ->
                categoryId to categoryVo
            }.toCollection(LinkedList())

        val result = mutableListOf<Category>()
        getToBeUpsertRecursively(
            queue = queue,
            asIsMap = asIsMap,
            depth = 0,
            parent = null,
            result = result,
        )
        return result
    }

    private fun getToBeUpsertRecursively(
        queue: Queue<Pair<CategoryId, CategoryDataHolder>>,
        asIsMap: Map<CategoryId, Category>,
        depth: Int,
        parent: Category?,
        result: MutableList<Category>,
    ) {
        while (queue.isNotEmpty()) {
            val peeked = queue.peek().second
            val needToMoveDepth = peeked.depth != depth
            if (needToMoveDepth) {
                if (depth > peeked.depth) return
                getToBeUpsertRecursively(queue, asIsMap, depth + 1, result.last(), result)
            } else {
                val (categoryId, categoryVO) = queue.poll()
                val category = upsert(asIsMap, categoryId, categoryVO, parent, depth)
                result.add(category)
            }
        }
        return
    }

    private fun upsert(
        asIsMap: Map<CategoryId, Category>,
        categoryId: CategoryId,
        categoryVO: CategoryDataHolder,
        parent: Category?,
        depth: Int,
    ): Category {
        val category =
            asIsMap[categoryId]?.apply {
                this.edit(
                    categoryTitle = categoryVO.categoryName,
                    depth = depth,
                    sortingPoint = categoryVO.sortingPoint,
                )
            } ?: categoryVO.toEntity(categoryId)

        parent?.let {
            category.setParent(parent)
        }

        return category
    }

    private fun CategoryDataHolder.toEntity(categoryId: CategoryId) =
        Category(
            categoryId = categoryId,
            categoryTitle = this.categoryName,
            depth = this.depth,
            sortingPoint = this.sortingPoint,
        )
}

data class SyncResult(
    val toBeDelete: List<Category>,
    val toBeUpsert: List<Category>,
)
