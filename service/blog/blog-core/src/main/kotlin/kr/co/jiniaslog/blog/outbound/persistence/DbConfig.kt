package kr.co.jiniaslog.blog.outbound.persistence

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

object BlogDb {
    const val BASE_PACKAGE = "kr.co.jiniaslog.blog"
    const val DATASOURCE_PREFIX = "spring.datasource.blog"
    const val ENTITY_MANAGER_FACTORY = "blogEntityManagerFactory"
    const val DATASOURCE = "blogDatasource"
    const val PERSISTENT_UNIT = "blogEntityManager"
    const val TRANSACTION_MANAGER = "blogTransactionManager"
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = BlogDb.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = BlogDb.TRANSACTION_MANAGER,
    basePackages = [BlogDb.BASE_PACKAGE],
)
class BlogDatasourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = BlogDb.DATASOURCE_PREFIX)
    fun blogDatasource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    @Primary
    fun blogEntityManagerFactory(
        @Qualifier(BlogDb.DATASOURCE) dataSource: DataSource,
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages(BlogDb.BASE_PACKAGE)
            .properties(mapOf("hibernate.hbm2ddl.auto" to "create-drop"))
            .build()
    }

    @Bean
    fun blogTransactionManager(
        @Qualifier(BlogDb.ENTITY_MANAGER_FACTORY) entityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        transactionManager.persistenceUnitName = BlogDb.PERSISTENT_UNIT

        return transactionManager
    }
}
