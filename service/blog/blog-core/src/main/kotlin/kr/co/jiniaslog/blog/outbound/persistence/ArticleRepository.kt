package kr.co.jiniaslog.blog.outbound.persistence

import kr.co.jiniaslog.blog.domain.article.Article
import kr.co.jiniaslog.blog.domain.article.ArticleId
import kr.co.jiniaslog.shared.core.domain.Repository
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : Repository<Article, ArticleId>

interface ArticleJpaRepository : JpaRepository<Article, ArticleId>

@org.springframework.stereotype.Repository
class ArticleRepositoryAdapter(
    private val articleJpaRepository: ArticleJpaRepository,
) : ArticleRepository {
    override fun save(entity: Article): Article {
        return articleJpaRepository.save(entity)
    }

    override fun findById(id: ArticleId): Article? {
        return articleJpaRepository.findById(id).orElse(null)
    }

    override fun findAll(): List<Article> {
        return articleJpaRepository.findAll()
    }

    override fun deleteById(id: ArticleId) {
        articleJpaRepository.deleteById(id)
    }
}
