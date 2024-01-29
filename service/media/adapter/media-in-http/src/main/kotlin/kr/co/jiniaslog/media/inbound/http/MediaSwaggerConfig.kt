package kr.co.jiniaslog.media.inbound.http

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MediaSwaggerConfig {
    @Bean
    fun mediaApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("MediaResource")
            .packagesToScan("kr.co.jiniaslog.media.inbound.http")
            .build()
    }
}
