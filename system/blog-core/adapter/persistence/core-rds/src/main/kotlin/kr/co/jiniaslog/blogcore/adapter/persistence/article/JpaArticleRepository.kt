package kr.co.jiniaslog.blogcore.adapter.persistence.article

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaArticleRepository : JpaRepository<ArticlePM, Long> {
    @EntityGraph("article-with-taggings")
    override fun findById(articleId: Long): Optional<ArticlePM>

    @EntityGraph("article-with-taggings")
    override fun findAll(): List<ArticlePM>
}
