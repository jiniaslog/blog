package kr.co.jiniaslog.memo.domain.memo

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.time.LocalDateTime

class Memo private constructor(
    id: MemoId,
    authorId: AuthorId,
    content: MemoContent,
    title: MemoTitle,
    references: MutableSet<MemoReference>,
    parentFolderId: FolderId?,
) : AggregateRoot<MemoId>() {
    override val id: MemoId = id

    val authorId: AuthorId = authorId

    var title: MemoTitle = title
        private set

    var content: MemoContent = content
        private set

    private var _references: MutableSet<MemoReference> = references

    val references: Set<MemoReference>
        get() = _references.toSet()

    var parentFolderId: FolderId? = parentFolderId
        private set

    fun update(
        title: MemoTitle? = null,
        content: MemoContent? = null,
    ) {
        title?.let {
            require(it.value.isNotEmpty()) { "title must not be empty" }
            this.title = it
        }
        content?.let {
            require(it.value.isNotEmpty()) { "content must not be empty" }
            this.content = it
        }
    }

    fun addReference(referenceId: MemoId) {
        this._references.add(MemoReference(this.id, referenceId))
    }

    fun removeReference(referenceId: MemoId) {
        this._references.remove(MemoReference(this.id, referenceId))
    }

    fun setParentFolder(folderId: FolderId?) {
        this.parentFolderId = folderId
    }

    override fun toString(): String {
        return "Memo(id=$id, authorId=$authorId, title=$title, content=$content, reference=$_references)"
    }

    fun updateReferences(references: Set<MemoId>) {
        this._references = references.map { MemoReference(this.id, it) }.toMutableSet()
    }

    companion object {
        fun init(
            authorId: AuthorId,
            parentFolderId: FolderId?,
        ): Memo {
            return Memo(
                id = MemoId(IdUtils.idGenerator.generate()),
                content = MemoContent.EMPTY,
                title = MemoTitle.UNTITLED,
                references = mutableSetOf(),
                authorId = authorId,
                parentFolderId = parentFolderId,
            )
        }

        fun from(
            id: MemoId,
            authorId: AuthorId,
            title: MemoTitle,
            content: MemoContent,
            reference: MutableSet<MemoReference>,
            parentFolderId: FolderId?,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?,
        ): Memo {
            return Memo(
                id = id,
                authorId = authorId,
                content = content,
                title = title,
                references = reference,
                parentFolderId = parentFolderId,
            ).apply {
                this.createdAt = createdAt
                this.updatedAt = updatedAt
            }
        }
    }
}
