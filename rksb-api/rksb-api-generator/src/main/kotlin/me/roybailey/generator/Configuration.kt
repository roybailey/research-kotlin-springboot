package me.roybailey.generator

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import javax.sql.DataSource


@Configuration
open class Configuration {

    private val logger = KotlinLogging.logger {}

    @Value("\${project.basedir}")
    lateinit var basedir: String

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
        return mainDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

    @Bean
    open fun apiDefinition(): List<ApiDefinition> {
        val apiDirMap = Files.list(Path.of("$basedir/../docs")).collect(Collectors.toList())
        val mapper = jacksonObjectMapper()

        val apiDefinitions = apiDirMap.stream().map {
            logger.info { it }
            val apiDefinition = mapper.readValue(it.toFile(), ApiDefinition::class.java)
            logger.info { apiDefinition }
            apiDefinition
        }.collect(Collectors.toList())

        return apiDefinitions
    }
}
