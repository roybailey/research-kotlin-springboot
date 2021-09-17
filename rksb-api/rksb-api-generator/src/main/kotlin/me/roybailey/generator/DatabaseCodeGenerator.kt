package me.roybailey.generator

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintConfiguration
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

@Component
class DatabaseCodeGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintConfiguration: ApiBlueprintConfiguration

    @Autowired
    lateinit var properties: GeneratorConfigurationProperties

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    @PostConstruct
    fun generateDatabaseCode() {

        logger.info("Database Code Generation - STARTING")

        val basedir = properties.basedir
        val target = properties.target
        val directory = "${basedir}/${target}"
        val packageName = "${properties.basePackageName}.jooq.database"
        val excludes = properties.excludes
        val includes = apiBlueprints
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
                    .withDriver(apiBlueprintConfiguration.properties.blueprintsDatabaseDriver)
                    .withUrl(apiBlueprintConfiguration.properties.blueprintsDatabaseUrl)
                    .withUser(apiBlueprintConfiguration.properties.blueprintsDatabaseUsername)
                    .withPassword(apiBlueprintConfiguration.properties.blueprintsDatabasePassword)
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
                            .withInputSchema(apiBlueprintConfiguration.properties.blueprintsDatabaseSchema)
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
