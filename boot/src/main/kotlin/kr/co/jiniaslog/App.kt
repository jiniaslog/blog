package kr.co.jiniaslog

import kr.co.jiniaslog.lib.context.AntiCorruptLayer
import kr.co.jiniaslog.lib.context.DomainService
import kr.co.jiniaslog.lib.context.UseCaseInteractor
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType



@ComponentScan(
    includeFilters = [
        ComponentScan.Filter(type = FilterType.ANNOTATION, value = [UseCaseInteractor::class]),
        ComponentScan.Filter(type = FilterType.ANNOTATION, value = [DomainService::class]),
        ComponentScan.Filter(type = FilterType.ANNOTATION, value = [AntiCorruptLayer::class]),
    ]
)
@SpringBootApplication
@ConfigurationPropertiesScan(
    basePackages = [
        "kr.co.jiniaslog"
    ]
)
class App

fun main(args: Array<String>) {
    val application = SpringApplication(App::class.java)
    application.addInitializers(CustomAutoConfigYmlImportInitializer())
    application.run(*args)
}

