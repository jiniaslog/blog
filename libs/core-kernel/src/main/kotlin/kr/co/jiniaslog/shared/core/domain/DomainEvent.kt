package kr.co.jiniaslog.shared.core.domain

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID.randomUUID

abstract class DomainEvent(
    val eventVersion: Int,
    val occurredAt: LocalDateTime = LocalDateTime.now(),
) : Serializable {
    val id: String = randomUUID().toString()

    val eventName: String
        get() = this::class.simpleName!!

    val channelName: String
        get() = "${eventName}$CHANNEL_NAME_CONVENTION"

    companion object {
        const val EVENT_ID = "eventId"
        const val CHANNEL_NAME_CONVENTION = "Channel"
    }
}
