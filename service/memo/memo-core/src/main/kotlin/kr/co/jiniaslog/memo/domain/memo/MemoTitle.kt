package kr.co.jiniaslog.memo.domain.memo

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class MemoTitle(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.length in 1..100) { "제목은 1자 이상 100자 이하여야 합니다." }
    }

    companion object {
        val UNTITLED: MemoTitle = MemoTitle("untitled")
    }
}
