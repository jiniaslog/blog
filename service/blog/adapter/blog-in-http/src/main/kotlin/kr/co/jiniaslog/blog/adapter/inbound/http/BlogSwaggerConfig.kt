package kr.co.jiniaslog.blog.adapter.inbound.http

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogSwaggerConfig {
    @Bean
    fun blogApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("blog")
            .packagesToScan("kr.co.jiniaslog.blog.adapter.inbound.http")
            .build()
    }
}
