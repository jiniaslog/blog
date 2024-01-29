package kr.co.jiniaslog.shared.log

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

private val log = mu.KotlinLogging.logger {}

@Component
class LogTracer {
    private val traceIdHolder = ThreadLocal<TraceId>()
    private val logBuffer = ThreadLocal<MutableList<String>>()

    fun begin(
        message: String,
        args: String,
        returnType: String,
    ): TraceStatus {
        syncTraceId()
        val traceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        if (traceId.isFirstLevel) {
            addToLogBuffer("------------------------------" + traceId.id + "'s transaction start------------------------------")
        }
        addToLogBuffer(
            "[${traceId.id}] ${
                addSpace(
                    START_PREFIX,
                    traceId.level,
                )
            }$message ,args = $args, returnType = $returnType",
        )
        return TraceStatus(traceId, startTimeMs, message)
    }

    private fun syncTraceId() {
        val traceId = traceIdHolder.get()
        val log = logBuffer.get()
        if (traceId == null) {
            try {
                val request: HttpServletRequest =
                    (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
                val clientIP = IpGetHelper.getClientIP(request)
                traceIdHolder.set(TraceId.create(clientIP))
                logBuffer.set(mutableListOf())
            } catch (e: Exception) {
                traceIdHolder.set(TraceId.create("not found ip"))
                logBuffer.set(mutableListOf())
            }
        } else {
            traceIdHolder.set(traceId.createNextId())
        }
    }

    fun end(traceStatusVO: TraceStatus) {
        complete(traceStatusVO, null)
    }

    fun handleException(
        traceStatusVO: TraceStatus,
        ex: Exception?,
    ) {
        complete(traceStatusVO, ex)
    }

    private fun complete(
        traceStatus: TraceStatus,
        ex: Exception?,
    ) {
        val stopTimeMs = System.currentTimeMillis()
        val resultTimeMs: Long = stopTimeMs - traceStatus.startTimesMs
        val traceId: TraceId = traceStatus.traceId
        if (ex == null) {
            addToLogBuffer(
                "[${traceId.id}] ${addSpace(COMPLETE_PREFIX, traceId.level)}${traceStatus.message} time = ${resultTimeMs}ms",
            )
        } else {
            addToLogBuffer(
                "[${traceId.id}] ${
                    addSpace(
                        EX_PREFIX,
                        traceId.level,
                    )
                } ${traceStatus.message} time = ${resultTimeMs}ms ex = $ex",
            )
        }
        if (traceStatus.traceId.isFirstLevel) {
            addToLogBuffer(
                "-------------------------------" + traceId.id + "'s transaction end/" + resultTimeMs + "ms-------------------------",
            )
        }
        releaseAndFlushIfNeeded()
    }

    private fun releaseAndFlushIfNeeded() {
        val traceId = traceIdHolder.get()
        if (traceId.isFirstLevel) {
            flushLogs()
            traceIdHolder.remove()
            logBuffer.remove()
        } else {
            traceIdHolder.set(traceId.createPrevId())
        }
    }

    private fun addSpace(
        prefix: String,
        level: Int,
    ): String {
        val sb = StringBuilder()
        for (i in 0 until level) {
            sb.append(if ((i == level - 1)) "|$prefix" else "|   ")
        }
        return sb.toString()
    }

    fun flushLogs() {
        logBuffer.get().reduce { first, second ->
            first + "\n" + second
        }.also {
            log.info("\n$it")
        }
        logBuffer.remove()
    }

    private fun addToLogBuffer(message: String) {
        logBuffer.get().add(LocalDateTime.now().toString() + " : " + message)
    }

    companion object {
        private const val START_PREFIX = "-->"
        private const val COMPLETE_PREFIX = "<--"
        private const val EX_PREFIX = "<X-"
    }
}
