package kr.co.jiniaslog.memo.usecase

import kr.co.jiniaslog.memo.domain.folder.FolderId

interface IDeleteFoldersRecursively {
    fun handle(command: Command): Info

    data class Command(
        val folderId: FolderId,
    )

    data class Info(
        val folderId: FolderId,
    )
}
