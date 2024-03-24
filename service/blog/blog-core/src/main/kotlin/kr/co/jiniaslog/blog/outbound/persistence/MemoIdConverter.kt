package kr.co.jiniaslog.blog.outbound.persistence

import jakarta.persistence.AttributeConverter
import kr.co.jiniaslog.blog.domain.memo.MemoId

class MemoIdConverter : AttributeConverter<MemoId, Long> {
    override fun convertToDatabaseColumn(attribute: MemoId?): Long {
        return attribute?.value ?: 0
    }

    override fun convertToEntityAttribute(dbData: Long?): MemoId {
        return MemoId(dbData ?: 0)
    }
}
