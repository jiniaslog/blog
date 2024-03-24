package kr.co.jiniaslog.blog.outbound.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jiniaslog.blog.domain.article.QTagging.tagging
import kr.co.jiniaslog.blog.domain.tag.QTag.tag
import kr.co.jiniaslog.blog.domain.tag.Tag
import kr.co.jiniaslog.blog.domain.tag.TagId
import kr.co.jiniaslog.blog.domain.tag.TagName
import kr.co.jiniaslog.shared.core.domain.Repository
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : Repository<Tag, TagId> {
    fun isExist(name: TagName): Boolean

    fun findNotUsedTags(): List<Tag>
}

interface TagJpaRepository : JpaRepository<Tag, TagId> {
    fun findByTagName(name: TagName): Tag?
}

@org.springframework.stereotype.Repository
class TagRepositoryAdapter(
    private val tagJpaRepository: TagJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : TagRepository {
    override fun save(entity: Tag): Tag {
        return tagJpaRepository.save(entity)
    }

    override fun isExist(name: TagName): Boolean {
        return tagJpaRepository.findByTagName(name) != null
    }

    override fun findNotUsedTags(): List<Tag> {
        return queryFactory.selectFrom(tag)
            .leftJoin(tag.tagging, tagging)
            .where(tagging.tagId.isNull)
            .fetch()
    }

    override fun findById(id: TagId): Tag? {
        return tagJpaRepository.findById(id).orElse(null)
    }

    override fun findAll(): List<Tag> {
        return tagJpaRepository.findAll()
    }

    override fun deleteById(id: TagId) {
        tagJpaRepository.deleteById(id)
    }
}
