package kr.co.jiniaslog.user.adapter.out.mysql

import kr.co.jiniaslog.user.application.infra.UserAuthTransactionHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Component
open class TransactionHandlerImpl : UserAuthTransactionHandler {
    @Transactional("userTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun <T> runInRepeatableReadTransaction(supplier: () -> T): T {
        return supplier()
    }
}
