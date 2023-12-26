package kr.co.jiniaslog.memo.adapter.inbound.http

import kr.co.jiniaslog.memo.queries.IGetAllMemos
import kr.co.jiniaslog.memo.queries.IGetAllReferencedByMemo
import kr.co.jiniaslog.memo.queries.IGetAllReferencesByMemo
import kr.co.jiniaslog.memo.queries.IGetMemoById
import kr.co.jiniaslog.memo.queries.IRecommendRelatedMemo
import kr.co.jiniaslog.memo.usecase.IUpdateMemo

data class MemoResponse(
    val memos: List<IGetAllMemos.Info>,
)

fun IRecommendRelatedMemo.Info.toResponse(): MemoResponse {
    return MemoResponse(
        memos =
            this.relatedMemoCandidates.map {
                IGetAllMemos.Info(
                    memoId = it.first,
                    title = it.second,
                    content = it.third,
                    references = emptySet(),
                )
            },
    )
}

fun List<IGetAllMemos.Info>.toResponse(): MemoResponse {
    return MemoResponse(
        memos = this,
    )
}

data class GetMemoByIdResponse(
    val memoId: Long,
    val title: String,
    val content: String,
    val references: Set<Reference>,
) {
    data class Reference(
        val rootId: Long,
        val referenceId: Long,
    )
}

fun IGetMemoById.Info.toResponse(): GetMemoByIdResponse {
    return GetMemoByIdResponse(
        memoId = this.memoId.value,
        title = this.title.value,
        content = this.content.value,
        references =
            this.references.map {
                GetMemoByIdResponse.Reference(
                    rootId = it.rootId.value,
                    referenceId = it.referenceId.value,
                )
            }.toSet(),
    )
}

data class InitMemoRequest(
    val authorId: Long,
)

data class InitMemoResponse(
    val memoId: Long,
)

data class GetAllReferencesByMemoResponse(
    val references: List<Reference>,
) {
    data class Reference(
        val id: Long,
        val title: String,
    )
}

fun IGetAllReferencesByMemo.Info.toResponse(): GetAllReferencesByMemoResponse {
    return GetAllReferencesByMemoResponse(
        references =
            this.references.map {
                GetAllReferencesByMemoResponse.Reference(
                    id = it.id.value,
                    title = it.title.value,
                )
            }.toList(),
    )
}

data class GetAllReferencedByMemoResponse(
    val referenceds: List<Referenced>,
) {
    data class Referenced(
        val id: Long,
        val title: String,
    )
}

fun IGetAllReferencedByMemo.Info.toResponse(): GetAllReferencedByMemoResponse {
    return GetAllReferencedByMemoResponse(
        referenceds =
            this.referenceds.map {
                GetAllReferencedByMemoResponse.Referenced(
                    id = it.id.value,
                    title = it.title.value,
                )
            }.toList(),
    )
}

data class AddReferenceResponse(
    val memoId: Long,
)

fun IUpdateMemo.Info.toResponse(): AddReferenceResponse {
    return AddReferenceResponse(
        memoId = this.id.value,
    )
}
