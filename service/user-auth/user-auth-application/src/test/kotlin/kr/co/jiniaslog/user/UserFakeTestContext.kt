package kr.co.jiniaslog.user

import kr.co.jiniaslog.shared.core.annotation.CustomComponent
import kr.co.jiniaslog.shared.core.domain.IdGenerator
import kr.co.jiniaslog.shared.core.domain.IdUtils
import kr.co.jiniaslog.user.application.infra.ProviderAdapter
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.application.infra.UserAuthTransactionHandler
import kr.co.jiniaslog.user.fakes.FakeGoogleProviderAdapter
import kr.co.jiniaslog.user.fakes.TestTransactionHandler
import kr.co.jiniaslog.user.fakes.TokenFakeStore
import kr.co.jiniaslog.user.fakes.UserFakeRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
@ComponentScan(
    includeFilters = [
        ComponentScan.Filter(type = FilterType.ANNOTATION, value = [CustomComponent::class]),
    ],
)
class UserFakeTestContext {
    @Configuration
    class Config {
        @Bean
        fun googleProviderAdapter(): ProviderAdapter {
            return FakeGoogleProviderAdapter()
        }

        @Bean
        fun transactionManager(): UserAuthTransactionHandler {
            return TestTransactionHandler()
        }

        @Bean
        fun tokenStore(): TokenStore {
            return TokenFakeStore()
        }

        @Bean
        fun fakeRepository(): UserFakeRepository {
            return UserFakeRepository()
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
}

@SpringBootTest
class UserFakeTestContextTests {
    @Test
    fun contextLoads() {
    }
}
