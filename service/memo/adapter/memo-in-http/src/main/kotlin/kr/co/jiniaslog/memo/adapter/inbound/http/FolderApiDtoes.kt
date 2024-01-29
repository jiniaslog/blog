package kr.co.jiniaslog.memo.adapter.inbound.http

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.folder.FolderName
import kr.co.jiniaslog.memo.usecase.IChangeFolderName
import kr.co.jiniaslog.memo.usecase.IMakeRelationShipFolderAndFolder

data class InitFolderResponse(
    val folderId: Long,
    val folderName: String,
)

data class AddParentFolderResponse(
    val memoId: Long,
    val folderId: Long?,
)

data class ChangeFolderNameRequest(
    val folderId: Long,
    val name: String,
) {
    fun toCommand(): IChangeFolderName.Command {
        return IChangeFolderName.Command(
            folderId = FolderId(folderId),
            name = FolderName(name),
        )
    }
}

data class ChangeFolderNameResponse(
    val folderId: Long,
)

data class MakeFolderRelationshipRequest(
    val parentId: Long,
    val childId: Long,
) {
    fun toCommand(): IMakeRelationShipFolderAndFolder.Command {
        return IMakeRelationShipFolderAndFolder.Command(
            parentFolderId = FolderId(parentId),
            childFolderId = FolderId(childId),
        )
    }
}

data class MakeFolderRelationshipResponse(
    val parentId: Long?,
    val childId: Long,
)

data class DeleteFolderResponse(
    val folderId: Long,
)
