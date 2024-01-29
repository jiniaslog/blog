package kr.co.jiniaslog.shared.core.domain

interface IdGenerator {
    fun generate(): Long
}

object IdUtils {
    lateinit var idGenerator: IdGenerator

    fun generate(): Long {
        return idGenerator.generate()
    }
}
