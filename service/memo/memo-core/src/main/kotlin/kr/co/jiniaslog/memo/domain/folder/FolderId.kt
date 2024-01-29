package kr.co.jiniaslog.memo.domain.folder

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class FolderId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value > 0) { "FolderId must be greater than 0" }
    }
}
