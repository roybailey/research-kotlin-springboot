package me.roybailey.generator

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintConfiguration
import mu.KotlinLogging
import org.jooq.SQLDialect
import org.jooq.impl.DSL.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.stream.IntStream
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Component
class DatabaseDataGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintConfiguration: ApiBlueprintConfiguration

    @Autowired
    lateinit var properties: GeneratorConfigurationProperties

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    @Autowired
    lateinit var dataSource: DataSource

    fun generateSequenceString(column: String, index: Int): Any {
        return "$column${1000000 + index}"
    }

    fun generateSequenceNumber(column: String, index: Int): Any {
        return 1000000 + index
    }

    @PostConstruct
    fun generateColumn() {

        logger.info("Database Data Generation - STARTING")
        val jooq = using(dataSource, SQLDialect.POSTGRES)

        // SELECT * FROM  WHERE table_schema = 'information_schema' ORDER BY table_name;
        val fetch = jooq.select(
            field("table_schema"),
            field("table_name"),
            field("ordinal_position"),
            field("column_name"),
            field("data_type"),
            field("character_maximum_length"),
            field("numeric_precision"),
            field("is_nullable")
        )
            .from("information_schema.columns")
            .where("table_schema='${apiBlueprintConfiguration.properties.blueprintsDatabaseSchema}'")
            .fetch()

        fetch.forEach {
            logger.info(it.formatCSV())
        }

        logger.info("Database Data Generation - FINISHED")
    }

    @PostConstruct
    fun generateDatabaseData() {

        logger.info("Database Data Generation - STARTING")

        val jooq = using(dataSource, SQLDialect.POSTGRES)

        val tableMappings = apiBlueprints
            .map { apiDefinition -> apiDefinition.tableMapping }
            .toList()
            .flatten()

        logger.info("tableMappings=$tableMappings")

        tableMappings.forEach { tableMapping ->

            val columns = tableMapping.columnMapping.map { columnMapping ->
                Pair(
                    columnMapping.column,
                    when ("${columnMapping.type}:${columnMapping.testDataStrategy}".toUpperCase()) {
                        "ID:SEQUENCE" -> ::generateSequenceNumber
                        "DOUBLE:SEQUENCE" -> ::generateSequenceNumber
                        "STRING:SEQUENCE" -> ::generateSequenceString
                        "NUMBER:SEQUENCE" -> ::generateSequenceNumber
                        "INTEGER:SEQUENCE" -> ::generateSequenceNumber
                        "INTEGER:DATESEQUENCE" -> ::generateSequenceNumber // todo generate date
                        else -> ::generateSequenceString
                    }
                )
            }.toTypedArray()

            tableMapping.testData.forEach { testData ->
                val existingCount = jooq.select(count(field("*")))
                    .from(table(tableMapping.table))
                    .fetchOneInto(Integer::class.java)
                    ?: 0
                IntStream.range(0, testData.count.toInt() - existingCount.toInt()).forEach { index ->
                    logger.info("Inserting data into ${tableMapping.table} ($index)")
                    jooq.insertInto(table(tableMapping.table), columns.map { field(it.first) }.toList())
                        .values(columns.mapIndexed { cdx, column -> column.second(column.first, index) }.toList())
                        .execute()
                }
            }
        }

        logger.info("Database Data Generation - FINISHED")
    }
}
