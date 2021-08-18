package me.roybailey.generator

import mu.KotlinLogging
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

    @PostConstruct
    fun generateDatabaseData() {

        logger.info("Database Data Generation - STARTING")
        val jooq = using(dataSource, SQLDialect.POSTGRES)

        jooq.insertInto(table("BOOKS"), field("TITLE"), field("PUBLICATIONDATE"))
            .values("Game of Thrones", 1985)
            .execute()

        logger.info("Database Data Generation - FINISHED")
    }
}
