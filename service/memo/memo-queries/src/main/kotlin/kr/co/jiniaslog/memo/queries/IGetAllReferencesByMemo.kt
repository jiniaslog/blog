package kr.co.jiniaslog.memo.queries

import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IGetAllReferencesByMemo {
    fun handle(query: Query): Info

    data class Query(
        val memoId: MemoId,
    )

    data class Info(
        val references: Set<ReferenceInfo>,
    )

    data class ReferenceInfo(
        val id: MemoId,
        val title: MemoTitle,
    )
}
