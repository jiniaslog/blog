package kr.co.jiniaslog.memo.usecase

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.domain.memo.MemoId

interface IInitMemo {
    fun handle(command: Command): Info

    data class Command(
        val authorId: AuthorId,
        val parentFolderId: FolderId?,
    )

    data class Info(val id: MemoId)
}
