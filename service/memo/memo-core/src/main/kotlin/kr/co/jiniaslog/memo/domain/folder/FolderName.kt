package kr.co.jiniaslog.memo.domain.folder

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class FolderName(val value: String) : ValueObject {
    init {
        validate()
    }

    companion object {
        val UNTITLED: FolderName = FolderName("untitled")
    }

    override fun validate() {
        require(value.isNotBlank()) { "FolderName must not be blank" }
    }
}
