package me.roybailey.api.generator.service

import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.generator.configuration.GeneratorProperties
import me.roybailey.api.generator.configuration.GeneratorResult
import mu.KotlinLogging
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.Callable

@Component
class DatabaseCodeGenerator : Callable<GeneratorResult> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var generatorProperties: GeneratorProperties


    override fun call(): GeneratorResult {

        logger.info("Database Code Generation - STARTING")

        val basedir = generatorProperties.basedir
        val target = generatorProperties.target
        val directory = "${basedir}/${target}"
        val packageName = "${blueprintProperties.blueprintsBasePackage}.jooq.database"
        val excludes = blueprintProperties.blueprintsDatabaseExcludes
        val includes = blueprintCollection.allTables()
            .map { tableMapping -> tableMapping.tableName }
            .toList()
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
                    .withDriver(blueprintProperties.blueprintsDatabaseDriver)
                    .withUrl(blueprintProperties.blueprintsDatabaseUrl)
                    .withUser(blueprintProperties.blueprintsDatabaseUsername)
                    .withPassword(blueprintProperties.blueprintsDatabasePassword)
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
                            .withInputSchema(blueprintProperties.blueprintsDatabaseSchema)
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
        return GeneratorResult(this.javaClass.simpleName, emptyList())
    }

}
