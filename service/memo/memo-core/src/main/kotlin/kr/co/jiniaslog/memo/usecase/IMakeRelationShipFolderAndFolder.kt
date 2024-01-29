package kr.co.jiniaslog.memo.usecase

import kr.co.jiniaslog.memo.domain.folder.FolderId

interface IMakeRelationShipFolderAndFolder {
    fun handle(command: Command): Info

    data class Command(
        val parentFolderId: FolderId?,
        val childFolderId: FolderId,
    )

    data class Info(val parentFolderId: FolderId?, val childFolderId: FolderId)
}
