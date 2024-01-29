package kr.co.jiniaslog.memo.adapter.inbound.http

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemoSwaggerConfig {
    @Bean
    fun memoApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("MemoResource")
            .packagesToScan("kr.co.jiniaslog.memo.adapter.inbound.http")
            .build()
    }
}
