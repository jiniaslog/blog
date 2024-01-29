package kr.co.jiniaslog.message.nexus.event

import kr.co.jiniaslog.shared.core.domain.DomainEvent
import kr.co.jiniaslog.shared.messaging.AbstractChannelHandlerBuilder

@AbstractChannelHandlerBuilder
data class ArticleCreated(
    val articleId: Long,
    val articleCommitId: Long,
) : DomainEvent(eventVersion = 1)

@AbstractChannelHandlerBuilder
data class ArticleCommitted(
    val articleId: Long,
    val articleCommitId: Long,
    val writerId: Long,
    val categoryId: Long?,
    val head: Long,
) : DomainEvent(eventVersion = 1)

@AbstractChannelHandlerBuilder
data class ArticleStagingSnapped(
    val articleId: Long,
    val articleStagingId: Long,
) : DomainEvent(eventVersion = 1)

@AbstractChannelHandlerBuilder
data class ArticleDeleted(
    val articleId: Long,
) : DomainEvent(eventVersion = 1)

@AbstractChannelHandlerBuilder
data class ArticlePublished(
    val articleId: Long,
    val articleCommitId: Long,
    val writerId: Long,
    val categoryId: Long,
    val head: Long,
) : DomainEvent(eventVersion = 1)
