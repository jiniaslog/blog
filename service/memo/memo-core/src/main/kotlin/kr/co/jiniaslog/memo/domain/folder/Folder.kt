package kr.co.jiniaslog.memo.domain.folder

import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.shared.core.domain.AggregateRoot
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.time.LocalDateTime

class Folder private constructor(
    id: FolderId,
    name: FolderName,
    authorId: AuthorId,
    parent: FolderId?,
) : AggregateRoot<FolderId>() {
    override val id: FolderId = id

    var name: FolderName = name
        private set

    val authorId: AuthorId = authorId

    var parent: FolderId? = parent
        private set

    fun changeName(name: FolderName) {
        this.name = name
    }

    fun changeParent(parent: Folder?) {
        require(parent?.id != this.id) { "자기 자신을 부모로 설정할 수 없습니다." }
        require(parent?.parent != this.id) { "상위 폴더를 자식 폴더로 설정할 수 없습니다." }
        this.parent = parent?.id
    }

    companion object {
        fun init(
            authorId: AuthorId,
            parent: FolderId? = null,
        ): Folder {
            return Folder(
                id = FolderId(IdUtils.idGenerator.generate()),
                name = FolderName.UNTITLED,
                authorId = authorId,
                parent = parent,
            )
        }

        fun from(
            id: FolderId,
            name: FolderName,
            authorId: AuthorId,
            parent: FolderId?,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?,
        ): Folder {
            return Folder(
                id = id,
                name = name,
                authorId = authorId,
                parent = parent,
            ).apply {
                this.createdAt = createdAt
                this.updatedAt = updatedAt
            }
        }
    }
}
