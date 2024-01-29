package kr.co.jiniaslog.memo.domain.memo

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class MemoContent(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {}

    companion object {
        val EMPTY: MemoContent = MemoContent("")
    }
}
