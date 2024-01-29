package kr.co.jiniaslog.shared

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.IdUtils
import java.util.concurrent.atomic.AtomicLong

abstract class CustomBehaviorSpec : BehaviorSpec() {
    override fun isolationMode() = IsolationMode.InstancePerTest

    override suspend fun afterEach(
        testCase: TestCase,
        result: TestResult,
    ) {
        atomicSequence = AtomicLong(1)
        super.afterEach(testCase, result)
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
