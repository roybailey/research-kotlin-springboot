package me.roybailey.api.blueprint

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.yaml.snakeyaml.Yaml
import java.util.stream.Collectors


@Configuration
open class ApiBlueprintConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    open fun apiBlueprintFiles(): List<String> {
        // this was easier than getting Spring to load YAML arrays properly
        val propertiesFile = "blueprints.yml"
        val yaml = Yaml()
        val inputStream = this.javaClass.classLoader.getResourceAsStream(propertiesFile)
        val properties: Map<String, Any> = yaml.load(inputStream)
        logger.info("Loaded $propertiesFile")
        return properties["api.blueprints"] as List<String>
    }

    @Bean
    open fun apiBlueprints(): List<ApiBlueprint> {

        val apiBlueprintFiles = apiBlueprintFiles()
        logger.info("Loaded api.blueprints as $apiBlueprintFiles")

        if (apiBlueprintFiles.isEmpty()) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            logger.error("!!!!! NO API BLUEPRINTS FOUND !!!!!")
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }

        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val apiBlueprints = apiBlueprintFiles.stream().map { apiBlueprintFile ->
            logger.info { apiBlueprintFile }
            val apiBlueprint = mapper.readValue(
                this.javaClass.classLoader.getResourceAsStream(apiBlueprintFile),
                ApiBlueprint::class.java
            )
            logger.info { apiBlueprint }

            apiBlueprint.tableMapping
//                .filter { it.createSql != null }
                .forEach { tableMapping ->
                    val columnMap =
                        tableMapping.columnMapping.associateByTo(mutableMapOf(), { it.column.toUpperCase() }, { it })

                    val apiCreateSqlFile = apiBlueprintFile.replace("-blueprint.json", "-create.sql")
                    val createSql =
                        String(this.javaClass.classLoader.getResourceAsStream(apiCreateSqlFile)!!.readAllBytes())
                    //val createSql = File(apiBlueprintFile.toFile().parentFile.absolutePath + "/" + tableMapping.createSql).readText()

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

            apiBlueprint
        }.collect(Collectors.toList())
        return apiBlueprints
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
