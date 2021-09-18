package me.roybailey.generator

import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
open class GeneratorConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    @Primary
    @ConfigurationProperties("api.datasource.blueprints")
    open fun blueprintsDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary // this will override the datasource autoconfiguration and use your own everywhere
    open fun dataSource(): DataSource {
        return blueprintsDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

/*
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.main")
    open fun mainDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.main")
    open fun mainDataSource(): DataSource? {
        return mainDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

    @Bean
    @ConfigurationProperties("app.datasource.jooq")
    open fun jooqDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties("app.datasource.jooq")
    open fun jooqDataSource(): DataSource? {
        return jooqDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }
*/
}
