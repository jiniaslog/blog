package kr.co.jiniaslog.memo.usecase

import kr.co.jiniaslog.memo.domain.memo.MemoId

interface IDeleteMemo {
    fun handle(command: Command): Info

    data class Command(
        val id: MemoId,
    )

    class Info()
}
