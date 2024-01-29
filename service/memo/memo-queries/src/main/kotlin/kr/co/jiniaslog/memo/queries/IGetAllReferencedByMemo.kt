package kr.co.jiniaslog.memo.queries

import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IGetAllReferencedByMemo {
    fun handle(query: Query): Info

    data class Query(
        val memoId: MemoId,
    )

    data class Info(
        val referenceds: Set<ReferencedInfo>,
    )

    data class ReferencedInfo(
        val id: MemoId,
        val title: MemoTitle,
    )
}
