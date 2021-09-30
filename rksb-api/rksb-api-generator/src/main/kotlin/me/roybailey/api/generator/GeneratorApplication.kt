package me.roybailey.api.generator

import me.roybailey.api.blueprint.BlueprintConfiguration
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.blueprint.FlywayMigration
import me.roybailey.api.generator.configuration.GeneratorResult
import me.roybailey.api.generator.service.AsciiDocGenerator
import me.roybailey.api.generator.service.CodeGenerator
import me.roybailey.api.generator.service.DatabaseCodeGenerator
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication
import kotlin.system.exitProcess


@SpringBootApplication(
    // we dont want any of the spring auto datasource as we use the properties directly with jooq only
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JooqAutoConfiguration::class,
        R2dbcAutoConfiguration::class,
        FlywayAutoConfiguration::class
    ],
    scanBasePackageClasses = [BlueprintConfiguration::class],
    scanBasePackages = ["me.roybailey.api.generator"]
)
open class GeneratorApplication : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

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

    // we call the database migration here, outside spring, for couple of reasons
    // 1. When inside Spring the database migration does not happen before the Blueprint loading queries the database, thus in the first build it fails to generate the migrated database schema
    // 2. We keep the database migration out of inherited Spring code so only the build and manager projects will control it's execution into an environment
    // 3. We can load the database properties from application-blueprint.yml instead of having to define them again as spring.flyway.* properties
    FlywayMigration().call()
    // now the database schema has been updated the database/code generation can start
    runApplication<GeneratorApplication>(*args)
}

