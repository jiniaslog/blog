package kr.co.jiniaslog.shared.log

import java.util.UUID

data class TraceId(
    val id: String,
    val level: Int,
) {
    fun createNextId(): TraceId {
        return TraceId(id, level + 1)
    }

    fun createPrevId(): TraceId {
        return TraceId(id, level - 1)
    }

    val isFirstLevel: Boolean
        get() = level == 0

    companion object {
        fun create(id: String?): TraceId {
            return TraceId(id + "/" + UUID.randomUUID().toString().substring(0, 8), 0)
        }
    }
}
