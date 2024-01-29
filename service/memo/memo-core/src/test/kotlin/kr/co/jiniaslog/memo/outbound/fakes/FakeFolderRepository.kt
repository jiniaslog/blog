package kr.co.jiniaslog.memo.outbound.fakes

import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.outbound.FolderRepository

class FakeFolderRepository : FolderRepository {
    private val folders = mutableSetOf<Folder>()

    override fun findById(id: FolderId): Folder? {
        return folders.find { it.id == id }
    }

    override fun findAll(): List<Folder> {
        return folders.toList()
    }

    override fun deleteById(id: FolderId) {
        folders.removeIf { it.id == id }
    }

    override fun save(entity: Folder): Folder {
        folders.add(entity)
        return entity
    }
}
