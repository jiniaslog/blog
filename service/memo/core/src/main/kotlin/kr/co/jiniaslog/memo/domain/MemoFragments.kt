package kr.co.jiniaslog.memo.domain

import kr.co.jiniaslog.shared.core.domain.ValueObject

@JvmInline
value class MemoContent(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        if (value.isEmpty()) {
            throw IllegalArgumentException("메모 컨텐츠는 1자 이상이어야 합니다.")
        }
    }
}

@JvmInline
value class MemoId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        if (value < 0) {
            throw IllegalArgumentException("메모 ID는 0 이상이어야 합니다.")
        }
    }
}
