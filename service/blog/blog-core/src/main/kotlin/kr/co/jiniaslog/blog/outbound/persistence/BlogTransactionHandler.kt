package kr.co.jiniaslog.blog.outbound.persistence

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

interface BlogTransactionHandler {
    fun <T> runInRepeatableReadTransaction(block: () -> T): T
}

@Component
class BlogTransactionHandlerImpl : BlogTransactionHandler {
    @Transactional("blogTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun <T> runInRepeatableReadTransaction(block: () -> T): T {
        return block()
    }
}
