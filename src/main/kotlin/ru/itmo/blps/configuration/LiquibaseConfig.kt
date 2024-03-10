package ru.itmo.blps.configuration

import com.zaxxer.hikari.HikariDataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class LiquibaseConfig {
    @Bean
    fun liquibase(hikariDataSource: HikariDataSource): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = "classpath:liquibase/changelog.xml"
        liquibase.dataSource = hikariDataSource
        return liquibase
    }
}