package kr.co.jiniaslog.blog.domain.category

/**
 * 카테고리 입력 데이터
 *
 * 카테고리 싱커가 일괄적으로 들어오는 카테고리 입력 데이터를 처리하기 위한 값 객체
 *
 * 실제 카테고리 어그리게이트와는 다른 용도이며, 순수 데이터 홀더이다.
 *
 * @property categoryId 카테고리 식별자로 새로 만들 경우 null, 이미 있는 경우는 식별자를 가진다.
 * @property categoryName 카테고리 이름
 * @property children 자식 카테고리 목록
 * @property depth 카테고리 계층 레벨로 최상위 카테고리는 0부터 시작한다.
 * @property sortingPoint 카테고리 정렬 순서
 *
 * @see Category
 * @see CategorySyncer
 */
data class CategoryDataHolder(
    val categoryId: CategoryId?,
    val categoryName: CategoryTitle,
    val children: List<CategoryDataHolder> = listOf(),
    val depth: Int,
    val sortingPoint: Int,
) : Comparable<CategoryDataHolder> {
    override fun compareTo(other: CategoryDataHolder): Int {
        return this.sortingPoint.compareTo(other.sortingPoint)
    }
}
