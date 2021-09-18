package me.roybailey.generator

import me.roybailey.api.blueprint.ApiBlueprintConfiguration
import me.roybailey.api.blueprint.ApiBlueprintProperties
import me.roybailey.api.blueprint.FlywayMigration
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
import javax.annotation.PostConstruct
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
    scanBasePackageClasses = [ApiBlueprintConfiguration::class],
    scanBasePackages = ["me.roybailey.generator"]
)
open class GeneratorApplication : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Autowired
    lateinit var databaseCodeGenerator: DatabaseCodeGenerator

    @Autowired
    lateinit var databaseDataGenerator: DatabaseDataGenerator

    @Autowired
    lateinit var codeGenerator: CodeGenerator

    override fun run(args: ApplicationArguments?) {
        logger.info("Application started with command-line arguments: {}", args!!.sourceArgs)
        logger.info("NonOptionArgs: {}", args.nonOptionArgs)
        logger.info("OptionNames: {}", args.optionNames)

        logger.info(
            "apiBlueprintProperties: {} {}",
            apiBlueprintProperties.blueprintsDatabaseUrl,
            apiBlueprintProperties.blueprintsDatabaseUsername
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
    }

    @PostConstruct
    fun run() {
        logger.info("############################################################")
        val resultDatabaseCodeGenerator = databaseCodeGenerator.call()
        logger.info("############################################################")
        val resultDatabaseDataGenerator = databaseDataGenerator.call()
        logger.info("############################################################")
        val resultCodeGenerator = codeGenerator.call()
        logger.info("############################################################")

        logger.info("DatabaseCodeGenerator $resultDatabaseCodeGenerator")
        logger.info("DatabaseDataGenerator $resultDatabaseDataGenerator")
        logger.info("CodeGenerator $resultCodeGenerator")
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

