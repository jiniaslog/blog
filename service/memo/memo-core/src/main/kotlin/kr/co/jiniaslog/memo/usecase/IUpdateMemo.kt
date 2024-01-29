package kr.co.jiniaslog.memo.usecase

import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IUpdateMemo {
    fun handle(command: Command): Info

    sealed class Command {
        abstract val memoId: MemoId

        data class UpdateForm(
            override val memoId: MemoId,
            val title: MemoTitle? = null,
            val content: MemoContent? = null,
        ) : Command()

        data class AddReference(
            override val memoId: MemoId,
            val referenceId: MemoId,
        ) : Command()

        data class RemoveReference(
            override val memoId: MemoId,
            val referenceId: MemoId,
        ) : Command()

        data class UpdateReferences(
            override val memoId: MemoId,
            val references: Set<MemoId>,
        ) : Command()
    }

    data class Info(val id: MemoId)
}
