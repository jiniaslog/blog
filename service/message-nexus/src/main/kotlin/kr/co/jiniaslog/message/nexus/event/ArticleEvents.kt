package kr.co.jiniaslog.message.nexus.event

import kr.co.jiniaslog.shared.core.domain.DomainEvent
import java.time.LocalDateTime

@Channel
data class ArticleCreated(
    val articleId: Long,
    val articleCommitId: Long,
) : DomainEvent(
        eventVersion = 1,
        occurredAt = LocalDateTime.now(),
    )

class Dummy()
