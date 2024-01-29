package kr.co.jiniaslog.shared.core.domain

object EventManger {
    lateinit var eventContextManager: EventContextManager

    fun add(event: DomainEvent) {
        eventContextManager.add(event)
    }

    fun clear() {
        eventContextManager.clear()
    }

    fun toListAndClear(): List<DomainEvent> {
        return eventContextManager.getDomainEventsAndClear()
    }
}

interface EventContextManager {
    fun add(event: DomainEvent)

    fun clear()

    fun getDomainEventsAndClear(): List<DomainEvent>
}
