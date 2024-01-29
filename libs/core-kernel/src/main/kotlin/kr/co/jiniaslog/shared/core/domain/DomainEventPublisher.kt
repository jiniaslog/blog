package kr.co.jiniaslog.shared.core.domain

interface DomainEventPublisher {
    fun publish(event: DomainEvent)
}
