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
    lateinit var blueprintDatabaseMigration: BlueprintDatabaseMigration

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
            blueprintProperties.blueprintDatabaseUrl,
            blueprintProperties.blueprintDatabaseUsername
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

        // compile the blueprint templates into the fully qualified aggregate blueprint collection
        blueprintCompiler.compileBlueprintTemplates()

        // run the code generators now the database is updated and blueprint collection compiled
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
    println("    CODE GENERATOR")
    println("")

    val envVariableEnabler = "BLUEPRINT_GENERATOR"
    val enabled = System.getenv(envVariableEnabler)

    if("true" == enabled) {
        runApplication<GeneratorApplication>(*args)
    } else {
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("!!!!!!!!!! Generator exiting as $envVariableEnabler != 'true' but == '$enabled'")
        println("!!!!!!!!!! this is normal for CI/CD builds")
        println("!!!!!!!!!! in development you might want to generate code for local builds by assigning $envVariableEnabler=true")
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

}

