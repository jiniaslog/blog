package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j

import org.neo4j.cypherdsl.core.renderer.Configuration
import org.neo4j.cypherdsl.core.renderer.Dialect
import org.neo4j.driver.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.neo4j.config.EnableNeo4jAuditing
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
@EnableNeo4jAuditing
@EnableNeo4jRepositories(basePackages = ["kr.co.jiniaslog.memo.adapter.out.persistence.neo4j"])
class Neo4JConfiguration {
    @Bean
    fun cypherDslConfiguration(): Configuration {
        return Configuration.newConfig()
            .withDialect(Dialect.NEO4J_5).build()
    }

    @Bean(name = ["transactionManager"])
    @Primary
    fun neo4jTransactionManager(driver: Driver): PlatformTransactionManager {
        return Neo4jTransactionManager(driver)
    }
}
