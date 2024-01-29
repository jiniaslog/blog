package kr.co.jiniaslog.shared.log

data class TraceStatus(
    val traceId: TraceId,
    val startTimesMs: Long,
    val message: String,
)
