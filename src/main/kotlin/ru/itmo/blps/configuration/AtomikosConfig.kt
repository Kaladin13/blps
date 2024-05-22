package ru.itmo.blps.configuration

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@Order(Integer.MIN_VALUE)
class AtomikosConfig {
    @Bean(name = ["userDataSource"])
    @Primary
    fun userDataSource(): DataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = "jdbc:postgresql://localhost:6432/postgres"
        dataSource.username = "postgres"
        dataSource.password = "postgres"
        dataSource.driverClassName = "org.postgresql.Driver"
        dataSource.maximumPoolSize = 10
//        dataSource.uniqueResourceName = "xads1-user-db"
//        dataSource.xaDataSourceClassName = "org.postgresql.xa.PGXADataSource"
//        dataSource.xaProperties.setProperty("serverName", "localhost")
//        dataSource.xaProperties.setProperty("portNumber", "6432")
//        dataSource.xaProperties.setProperty("databaseName", "postgres")
//        dataSource.xaProperties.setProperty("user", "postgres")
//        dataSource.xaProperties.setProperty("password", "postgres")
//        dataSource.xaProperties.setProperty("serverTimezone", "UTC")
//        dataSource.xaProperties.setProperty("useSSL", "false")
        return dataSource
    }

    @Bean(name = ["adminDataSource"])
    fun adminDataSource(): DataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = "jdbc:postgresql://localhost:5432/studies"
        dataSource.username = "anonex"
        dataSource.password = "itmo"
        dataSource.driverClassName = "org.postgresql.Driver"
        dataSource.maximumPoolSize = 10
//        dataSource.uniqueResourceName = "xads2-admin-db"
//        dataSource.xaDataSourceClassName = "org.postgresql.xa.PGXADataSource"
//        dataSource.xaProperties.setProperty("serverName", "localhost")
//        dataSource.xaProperties.setProperty("portNumber", "5432")
//        dataSource.xaProperties.setProperty("databaseName", "studies")
//        dataSource.xaProperties.setProperty("user", "anonex")
//        dataSource.xaProperties.setProperty("password", "itmo")
//        dataSource.xaProperties.setProperty("serverTimezone", "UTC")
//        dataSource.xaProperties.setProperty("useSSL", "false")
        return dataSource
    }
//
//    @Bean(name = ["atomikosTransactionManager"], initMethod = "init", destroyMethod = "close")
//    @Primary
//    fun atomikosTransactionManager(): TransactionManager {
//        val userTransactionManager = UserTransactionManager()
//        userTransactionManager.forceShutdown = false
//        return userTransactionManager
//    }
//
//    @Bean(name = ["userTransaction"])
//    @Primary
//    fun userTransaction(): UserTransaction {
//        val userTransactionImp = UserTransactionImp()
//        userTransactionImp.setTransactionTimeout(5)
//        return userTransactionImp
//    }
//
//    @Bean(name = ["transactionManager"])
//    @DependsOn("userTransaction", "atomikosTransactionManager")
//    @Primary
//    fun transactionManager(): PlatformTransactionManager {
//        val userTransaction = userTransaction()
//        val atomikosTransactionManager = atomikosTransactionManager()
//        return JtaTransactionManager(userTransaction, atomikosTransactionManager)
//    }
}