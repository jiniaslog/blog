package kr.co.jiniaslog.user.application.infra

interface UserAuthTransactionHandler {
    fun <T> runInRepeatableReadTransaction(supplier: () -> T): T
}
