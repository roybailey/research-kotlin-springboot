package me.roybailey.generator

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintProperties
import mu.KotlinLogging
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import javax.annotation.PostConstruct

@Component
class DatabaseCodeGenerator : Callable<Boolean> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>


    override fun call(): Boolean {

        logger.info("Database Code Generation - STARTING")

        val basedir = generatorProperties.basedir
        val target = generatorProperties.target
        val directory = "${basedir}/${target}"
        val packageName = "${apiBlueprintProperties.codegenBasePackage}.jooq.database"
        val excludes = apiBlueprintProperties.blueprintsDatabaseExcludes
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
                    .withDriver(apiBlueprintProperties.blueprintsDatabaseDriver)
                    .withUrl(apiBlueprintProperties.blueprintsDatabaseUrl)
                    .withUser(apiBlueprintProperties.blueprintsDatabaseUsername)
                    .withPassword(apiBlueprintProperties.blueprintsDatabasePassword)
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
                            .withInputSchema(apiBlueprintProperties.blueprintsDatabaseSchema)
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
        return true
    }

}
