package kr.co.jiniaslog.blog.outbound.persistence

import kr.co.jiniaslog.blog.domain.category.Category
import kr.co.jiniaslog.blog.domain.category.CategoryId
import kr.co.jiniaslog.shared.core.domain.Repository
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : Repository<Category, CategoryId> {
    fun deleteAll(toBeDelete: List<Category>)

    fun saveAll(toBeUpsert: List<Category>)
}

interface CategoryJpaRepository : JpaRepository<Category, CategoryId>

@org.springframework.stereotype.Repository
class CategoryRepositoryAdapter(
    private val categoryJpaRepository: CategoryJpaRepository,
) : CategoryRepository {
    override fun save(entity: Category): Category {
        return categoryJpaRepository.save(entity)
    }

    override fun deleteAll(toBeDelete: List<Category>) {
        categoryJpaRepository.deleteAll(toBeDelete)
    }

    override fun saveAll(toBeUpsert: List<Category>) {
        categoryJpaRepository.saveAll(toBeUpsert)
    }

    override fun findById(id: CategoryId): Category? {
        return categoryJpaRepository.findById(id).orElse(null)
    }

    override fun findAll(): List<Category> {
        return categoryJpaRepository.findAll()
    }

    override fun deleteById(id: CategoryId) {
        categoryJpaRepository.deleteById(id)
    }
}
