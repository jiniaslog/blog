package kr.co.jiniaslog.shared.core.domain

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private val log = mu.KotlinLogging.logger {}

data class DomainContext(
    private var events: ConcurrentLinkedQueue<DomainEvent> = ConcurrentLinkedQueue(),
) : AbstractCoroutineContextElement(key) {
    fun add(event: DomainEvent) {
        events.add(event)
    }

    fun toListAndClear(): List<DomainEvent> {
        return synchronized(this) {
            events.toList().also {
                this.clear()
            }
        }
    }

    fun clear() {
        events.clear()
    }

    companion object {
        val key = object : CoroutineContext.Key<DomainContext> {}
    }
}
