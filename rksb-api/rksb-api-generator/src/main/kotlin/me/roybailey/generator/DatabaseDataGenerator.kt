package me.roybailey.generator

import mu.KotlinLogging
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.IntStream
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Component
class DatabaseDataGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var properties: ConfigurationProperties

    @Autowired
    lateinit var apiDefinitions: List<ApiDefinition>

    @Autowired
    lateinit var dataSource: DataSource

    fun generateSequenceString(column:String, index: Int): Any {
        return "$column${1000000+index}"
    }

    fun generateSequenceNumber(column:String, index: Int): Any {
        return 1000000 + index
    }

    @PostConstruct
    fun generateDatabaseData() {

        logger.info("Database Data Generation - STARTING")
        val jooq = using(dataSource, SQLDialect.POSTGRES)

        val tableMappings = apiDefinitions
            .map { apiDefinition -> apiDefinition.tableMapping }
            .toList()
            .flatten()

        logger.info("tableMappings=$tableMappings")

        tableMappings.forEach { tableMapping ->

            val columns = tableMapping.columnMapping.map { columnMapping ->
                Pair(
                    columnMapping.column,
                    when ("${columnMapping.type}:${columnMapping.testDataStrategy}".toUpperCase()) {
                        "STRING:SEQUENCE" -> ::generateSequenceString
                        "NUMBER:SEQUENCE" -> ::generateSequenceNumber
                        else -> ::generateSequenceString
                    }
                )
            }.toTypedArray()

            tableMapping.testData.forEach { testData ->
                val existingCount = jooq.select(count(field("*")))
                    .from(table(tableMapping.table))
                    .fetchOneInto(Integer::class.java)
                    ?: 0
                IntStream.range(0, testData.count.toInt()-existingCount.toInt()).forEach { index ->
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
