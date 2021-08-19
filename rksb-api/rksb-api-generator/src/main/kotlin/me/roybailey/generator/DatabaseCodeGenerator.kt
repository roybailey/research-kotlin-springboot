package me.roybailey.generator

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct

@Component
class DatabaseCodeGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var properties: ConfigurationProperties

    @Autowired
    lateinit var apiDefinitions: List<ApiDefinition>

    @PostConstruct
    fun generateDatabaseCode() {

        logger.info("Database Code Generation - STARTING")

        val basedir = properties.basedir
        val target = properties.target
        val directory = "${basedir}/${target}"
        val packageName = "${properties.basePackageName}.jooq.database"
        val excludes = properties.excludes
        val includes = apiDefinitions
            .map { apiDefinition -> apiDefinition.tableMapping.map { tableMapping -> tableMapping.table } }
            .toList()
            .flatten()
            .joinToString("|")

        logger.info("basedir=$basedir")
        logger.info("target=$target")
        logger.info("directory=$directory")
        logger.info("packageName=$packageName")
        logger.info("excludes=$excludes")
        logger.info("includes=$includes")

        var configuration = org.jooq.meta.jaxb.Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl("jdbc:postgresql://localhost:5432/postgres")
                    .withUser("postgres")
                    .withPassword("localhost")
            )
            .withGenerator(
                Generator()
                    // Optional: The fully qualified class name of the code generator. Available generators:
                    //
                    // - org.jooq.codegen.JavaGenerator
                    // - org.jooq.codegen.KotlinGenerator
                    // - org.jooq.codegen.ScalaGenerator
                    //
                    // Defaults to org.jooq.codegen.JavaGenerator
                    .withName("org.jooq.codegen.JavaGenerator")
                    // A programmatic naming strategy implementation, referenced by class name
                    .withDatabase(
                        Database()
                            .withName("org.jooq.meta.postgres.PostgresDatabase")
                            .withIncludes(includes)
                            .withExcludes(excludes)
                            .withInputSchema("public")
                    )
                    .withTarget(
                        Target()
                            .withPackageName(packageName)
                            .withDirectory(directory)
                    )
                    .withGenerate(Generate().withPojos(true))
            )
        GenerationTool.generate(configuration)
        logger.info("Database Code Generation - FINISHED")
    }
}
