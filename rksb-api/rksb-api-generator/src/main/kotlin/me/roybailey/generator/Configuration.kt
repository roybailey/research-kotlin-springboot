package me.roybailey.generator

import com.fasterxml.jackson.databind.DeserializationFeature
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
    open fun apiSpecification(): List<ApiSpecification> {
        val apiDirMap = Files
            .walk(Path.of("$basedir/../docs"))
            .filter { it.toFile().name.endsWith("-spec.json") }
            .collect(Collectors.toList())
        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val apiSpecifications = apiDirMap.stream().map { apiSpecPath ->
            logger.info { apiSpecPath }
            val apiSpecification = mapper.readValue(apiSpecPath.toFile(), ApiSpecification::class.java)
            logger.info { apiSpecification }

            apiSpecification.tableMapping
                .filter { it.createSql != null }
                .forEach { tableMapping ->
                    val columnMap = tableMapping.columnMapping.associateByTo(mutableMapOf(), { it.column.toUpperCase() }, { it })

                    val createSql = File(apiSpecPath.toFile().parentFile.absolutePath + "/" + tableMapping.createSql)
                        .readText()
                    logger.info("Parsing createSql\n$createSql")
                    val databaseColumns = getDatabaseColumns(createSql)
                    tableMapping.columnMapping = databaseColumns.map { databaseColumn ->
                        val specColumn = columnMap[databaseColumn.column]
                        logger.info("Column parsed as [$databaseColumn]")
                        logger.info("Column spec'd as [$specColumn]")
                        val apiColumnMapping = specColumn ?: databaseColumn
                        apiColumnMapping.databaseType = databaseColumn.databaseType
                        logger.info("Column merged as [$apiColumnMapping]")
                        apiColumnMapping
                    }
                }

            apiSpecification
        }.collect(Collectors.toList())

        return apiSpecifications
    }


    private fun getDatabaseColumns(createSql: String): List<ApiColumnMapping> {
        val parsedColumns = createSql
            .substring(createSql.indexOf('(') + 1, createSql.lastIndexOf(')'))
            .replace("\n", " ")
            .split(",")
            .map { column -> column.trim() }
            .map { column ->
                Pair(
                    column.substring(0, column.indexOf(' ')).trim().toUpperCase(),
                    column.substring(column.indexOf(' ')).trim().toUpperCase()
                )
            }
            .map { pair ->
                ApiColumnMapping(
                    pair.first,
                    pair.second,
                    when {
                        pair.second.contains("key") -> "ID"
                        pair.second.contains(Regex("varchar|text")) -> "TEXT"
                        pair.second.contains("double") -> "DOUBLE"
                        else -> pair.second.toUpperCase()
                    }
                )
            }
            .toList()
        return parsedColumns
    }
}
