package kr.co.jiniaslog.memo.domain.memo

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class MemoId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value > 0) { "메모 ID는 0 초과여야합니다" }
    }
}
