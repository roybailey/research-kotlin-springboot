package me.roybailey.api.generator.app

import me.roybailey.api.blueprint.BlueprintDatabaseMigration
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.generator.configuration.GeneratorResult
import me.roybailey.api.generator.service.AsciiDocGenerator
import me.roybailey.api.generator.service.BlueprintCompiler
import me.roybailey.api.generator.service.CodeGenerator
import me.roybailey.api.generator.service.DatabaseCodeGenerator
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import kotlin.system.exitProcess


// we don't want @EnableAutoConfiguration as this triggers many classpath configs and errors
@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = ["me.roybailey.api.blueprint", "me.roybailey.api.generator"])
open class GeneratorApplication : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintCompiler: BlueprintCompiler

    @Autowired
    lateinit var databaseCodeGenerator: DatabaseCodeGenerator

    @Autowired
    lateinit var codeGenerator: CodeGenerator

    @Autowired
    lateinit var asciiDocGenerator: AsciiDocGenerator


    override fun run(args: ApplicationArguments?) {
        logger.info("Application started with command-line arguments: {}", args!!.sourceArgs)
        logger.info("NonOptionArgs: {}", args.nonOptionArgs)
        logger.info("OptionNames: {}", args.optionNames)

        logger.info(
            "blueprintProperties: {} {}",
            blueprintProperties.blueprintsDatabaseUrl,
            blueprintProperties.blueprintsDatabaseUsername
        )

        logger.info("############################################################")
        args.optionNames.forEach { option ->
            logger.info("Option $option=${args.getOptionValues(option)}")
        }
        logger.info("############################################################")
        if (!args.containsOption("project.basedir")) {
            logger.error("!!!!!!!!!! project.basedir not provided - exiting generator")
            exitProcess(-1)
        }

        // prepare the database with the latest schema changes
        val databaseMigration = BlueprintDatabaseMigration(
            url = blueprintProperties.blueprintsDatabaseUrl,
            username = blueprintProperties.blueprintsDatabaseUsername,
            password = blueprintProperties.blueprintsDatabasePassword
        )
        databaseMigration.call()

        // compile the blueprints templates into the fully qualified aggregate blueprints collection
        blueprintCompiler.compileBlueprintsTemplates()

        // run the code generators now the database and blueprints are updated
        val generators = listOf(databaseCodeGenerator, codeGenerator, asciiDocGenerator)
        val mapGeneratorResult = mutableListOf<GeneratorResult>()
        generators.forEach { generator ->
            mapGeneratorResult.add(generator.call())
        }
        logger.info("Results...")
        mapGeneratorResult.forEach { generatorResult ->
            logger.info("$generatorResult}")
        }
    }
}


fun main(args: Array<String>) {

    println("     ######\\ #######\\###\\  ##\\#######\\######\\  #####\\ ########\\ #####\\ ######\\ ")
    println("    ##/----/ ##/----/####\\ ##|##/----/##/--##\\##/--##\\\\--##/--/##/--##\\##/--##\\")
    println("    ##|  ##\\ #####\\  ##/##\\##|#####\\  ######//#######|   ##|   ##|  ##|######//")
    println("    ##|  \\##\\##/--/  ##|\\####|##/--/  ##/--##\\##/--##|   ##|   ##|  ##|##/--##\\")
    println("    \\######//#######\\##| \\###|#######\\##|  ##|##|  ##|   ##|   \\#####//##|  ##|")
    println("     \\-----/ \\------/\\-/  \\--/\\------/\\-/  \\-/\\-/  \\-/   \\-/    \\----/ \\-/  \\-/")
    println("    EVIDENCE LAB DISTRIBUTION CODE GENERATOR")
    println("")

    // now the database schema has been updated the database/code generation can start
    runApplication<GeneratorApplication>(*args)
}

