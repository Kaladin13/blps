package ru.itmo.blps.configuration

import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import com.atomikos.icatch.provider.TransactionServiceProvider
import com.atomikos.jdbc.AtomikosDataSourceBean
import jakarta.transaction.TransactionManager
import jakarta.transaction.UserTransaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@Order(Integer.MIN_VALUE)
class AtomikosConfig {
    @Bean(name = ["userDataSource"])
    @Primary
    fun userDataSource(): DataSource {
        val dataSource = AtomikosDataSourceBean()
        dataSource.uniqueResourceName = "xads1-user-db"
        dataSource.xaDataSourceClassName = "org.postgresql.xa.PGXADataSource"
        dataSource.xaProperties.setProperty("serverName", "localhost")
        dataSource.xaProperties.setProperty("portNumber", "6432")
        dataSource.xaProperties.setProperty("databaseName", "postgres")
        dataSource.xaProperties.setProperty("user", "postgres")
        dataSource.xaProperties.setProperty("password", "postgres")
//        dataSource.xaProperties.setProperty("serverTimezone", "UTC")
//        dataSource.xaProperties.setProperty("useSSL", "false")
        return dataSource
    }

    @Bean(name = ["adminDataSource"])
    fun adminDataSource(): DataSource {
        val dataSource = AtomikosDataSourceBean()
        dataSource.uniqueResourceName = "xads2-admin-db"
        dataSource.xaDataSourceClassName = "org.postgresql.xa.PGXADataSource"
        dataSource.xaProperties.setProperty("serverName", "localhost")
        dataSource.xaProperties.setProperty("portNumber", "5432")
        dataSource.xaProperties.setProperty("databaseName", "postgres")
        dataSource.xaProperties.setProperty("user", "postgres")
        dataSource.xaProperties.setProperty("password", "postgres")
//        dataSource.xaProperties.setProperty("serverTimezone", "UTC")
//        dataSource.xaProperties.setProperty("useSSL", "false")
        return dataSource
    }

    @Bean(name = ["atomikosTransactionManager"], initMethod = "init", destroyMethod = "close")
    @Primary
    fun atomikosTransactionManager(): TransactionManager {
        val userTransactionManager = UserTransactionManager()
        userTransactionManager.forceShutdown = false
        return userTransactionManager
    }

    @Bean(name = ["userTransaction"])
    @Primary
    fun userTransaction(): UserTransaction {
        val userTransactionImp = UserTransactionImp()
        userTransactionImp.setTransactionTimeout(5)
        return userTransactionImp
    }

    @Bean(name = ["transactionManager"])
    @DependsOn("userTransaction", "atomikosTransactionManager")
    @Primary
    fun transactionManager(): PlatformTransactionManager {
        val userTransaction = userTransaction()
        val atomikosTransactionManager = atomikosTransactionManager()
        return JtaTransactionManager(userTransaction, atomikosTransactionManager)
    }
}