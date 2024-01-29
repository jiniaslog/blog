package kr.co.jiniaslog.memo.queries

import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IGetMemoById {
    fun handle(query: Query): Info

    data class Query(
        val memoId: MemoId,
    )

    data class Info(
        val memoId: MemoId,
        val title: MemoTitle,
        val content: MemoContent,
        val references: Set<ReferenceInfo>,
    ) {
        data class ReferenceInfo(
            val rootId: MemoId,
            val referenceId: MemoId,
        )
    }
}
