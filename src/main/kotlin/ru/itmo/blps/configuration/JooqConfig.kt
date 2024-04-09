package ru.itmo.blps.configuration

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.annotation.Order
import javax.sql.DataSource


@Configuration
@Order(Int.MIN_VALUE)
class JooqConfig {

    @Bean("userDslContext")
    @DependsOn("userDataSource")
    fun userDslContext(@Qualifier("userDataSource") userDataSource: DataSource): DSLContext {
        return DSL.using(userDataSource, SQLDialect.POSTGRES)
    }

    @Bean("adminDslContext")
    @DependsOn("adminDataSource")
    fun adminDslContext(@Qualifier("adminDataSource") adminDataSource: DataSource): DSLContext {
        return DSL.using(adminDataSource, SQLDialect.POSTGRES)
    }

    @Bean
    @DependsOn("userDataSource")
    fun jooqConfigUser(userDataSource: DataSource): DefaultConfiguration {
        val config = DefaultConfiguration()
        config.set(DataSourceConnectionProvider(userDataSource))
        config.set(SQLDialect.POSTGRES)
        return config
    }

    @Bean
    @DependsOn("adminDataSource")
    fun jooqConfigAdmin(adminDataSource: DataSource): DefaultConfiguration {
        val config = DefaultConfiguration()
        config.set(DataSourceConnectionProvider(adminDataSource))
        config.set(SQLDialect.POSTGRES)
        return config
    }

}