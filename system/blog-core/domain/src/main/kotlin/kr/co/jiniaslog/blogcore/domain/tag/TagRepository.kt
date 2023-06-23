package kr.co.jiniaslog.blogcore.domain.tag

interface TagRepository {
    fun save(tag: Tag)
    fun findById(tagId: TagId): Tag?
    fun findAll(): List<Tag>
}
