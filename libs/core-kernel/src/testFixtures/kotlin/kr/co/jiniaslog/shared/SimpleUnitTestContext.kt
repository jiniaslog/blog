package kr.co.jiniaslog.shared

import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.IdUtils
import org.junit.jupiter.api.AfterEach
import java.util.concurrent.atomic.AtomicLong

abstract class SimpleUnitTestContext {
    @AfterEach
    fun tearDown() {
        atomicSequence = AtomicLong(1)
    }

    companion object {
        private var atomicSequence = AtomicLong(1)

        init {
            IdUtils.idGenerator =
                object : IdGenerator {
                    override fun generate(): Long {
                        return atomicSequence.getAndIncrement()
                    }
                }
        }
    }
}
