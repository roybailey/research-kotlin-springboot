package me.roybailey.generator

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import java.lang.System.exit
import javax.sql.DataSource
import kotlin.system.exitProcess

@SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
open class GeneratorApplication : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var dataSource: DataSource

    override fun run(args: ApplicationArguments?) {
        logger.info("Application started with command-line arguments: {}", args!!.sourceArgs)
        logger.info("NonOptionArgs: {}", args.nonOptionArgs);
        logger.info("OptionNames: {}", args.optionNames);

        logger.info("dataSource: {} {}", dataSource.connection.getClientInfo(), dataSource.connection.metaData);

        logger.info("############################################################")
        args.optionNames.forEach { option ->
            logger.info("Option $option=${args.getOptionValues(option)}")
        }
        logger.info("############################################################")
        if(!args.containsOption("project.basedir")) {
            logger.error("!!!!!!!!!! project.basedir not provided - exiting generator")
            exitProcess(-1)
        }
    }

}


fun main(args: Array<String>) {

    println("    .######\\.#######\\###\\..##\\#######\\######\\..#####\\.########\\.#####\\.######\\.")
    println("    ##/----/.##/----/####\\.##|##/----/##/--##\\##/--##\\\\--##/--/##/--##\\##/--##\\")
    println("    ##|..##\\.#####\\..##/##\\##|#####\\..######//#######|...##|...##|..##|######//")
    println("    ##|..\\##\\##/--/..##|\\####|##/--/..##/--##\\##/--##|...##|...##|..##|##/--##\\")
    println("    \\######//#######\\##|.\\###|#######\\##|..##|##|..##|...##|...\\#####//##|..##|")
    println("    .\\-----/.\\------/\\-/..\\--/\\------/\\-/..\\-/\\-/..\\-/...\\-/....\\----/.\\-/..\\-/")
    println("")

    println("     ######  ####### ###   ## ####### ######   #####  ########  #####  ######  ")
    println("    ##       ##      ####  ## ##      ##   ## ##   ##    ##    ##   ## ##   ## ")
    println("    ##   ##  #####   ## ## ## #####   ######  #######    ##    ##   ## ######  ")
    println("    ##    ## ##      ##  #### ##      ##   ## ##   ##    ##    ##   ## ##   ## ")
    println("     ######  ####### ##   ### ####### ##   ## ##   ##    ##     #####  ##   ## ")
    println("                                                                               ")
    println("")

    runApplication<GeneratorApplication>(*args)
}

