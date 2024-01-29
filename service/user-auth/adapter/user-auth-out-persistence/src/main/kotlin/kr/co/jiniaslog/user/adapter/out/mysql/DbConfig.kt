package kr.co.jiniaslog.user.adapter.out.mysql

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

object UserDb {
    const val BASE_PACKAGE = "kr.co.jiniaslog.user.adapter.out.mysql"
    const val DATASOURCE_PREFIX = "spring.datasource.user"
    const val ENTITY_MANAGER_FACTORY = "userEntityManagerFactory"
    const val PERSISTENT_UNIT = "userEntityManager"
    const val TRANSACTION_MANAGER = "userTransactionManager"
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = UserDb.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = UserDb.TRANSACTION_MANAGER,
    basePackages = [UserDb.BASE_PACKAGE],
)
class UserDatasourceConfig {
    @Bean
    @ConfigurationProperties(prefix = UserDb.DATASOURCE_PREFIX)
    fun userDatasource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    fun userEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(userDatasource())
            .packages(UserDb.BASE_PACKAGE)
            .properties(mapOf("hibernate.hbm2ddl.auto" to "create-drop"))
            .build()
    }

    @Bean
    fun userTransactionManager(
        @Qualifier(UserDb.ENTITY_MANAGER_FACTORY) entityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        transactionManager.persistenceUnitName = UserDb.PERSISTENT_UNIT

        return transactionManager
    }
}
