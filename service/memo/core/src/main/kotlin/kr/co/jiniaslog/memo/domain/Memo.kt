package kr.co.jiniaslog.memo.domain

import kr.co.jiniaslog.shared.core.domain.AggregateRoot

class Memo(
    id: MemoId,
    content: MemoContent,
) : AggregateRoot<MemoId>() {
    override val id: MemoId = id

    var content: MemoContent = content
        private set

    companion object {
        fun newOne(
            id: MemoId,
            content: MemoContent,
        ): Memo {
            return Memo(id, content)
        }
    }
}
