package kr.co.jiniaslog.memo.queries

import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IGetAllMemos {
    fun handle(query: Query): List<Info>

    class Query()

    data class Info(
        val memoId: MemoId,
        val title: MemoTitle,
        val content: MemoContent? = null,
        val references: Set<MemoReferenceInfo>,
    )

    data class MemoReferenceInfo(
        val rootId: MemoId,
        val referenceId: MemoId,
    )
}
