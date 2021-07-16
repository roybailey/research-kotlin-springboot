package me.roybailey.generator

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
open class GeneratorApplication : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {
        logger.info("Application started with command-line arguments: {}", args!!.sourceArgs)
        logger.info("NonOptionArgs: {}", args.nonOptionArgs);
        logger.info("OptionNames: {}", args.optionNames);

        println("############################################################")
        args.optionNames.forEach { option ->
            println("Option $option=${args.getOptionValues(option)}")
        }
        println("############################################################")
    }

}

fun main(args: Array<String>) {
    println("WE HAVE LIFT-OFF!!!!")
    runApplication<GeneratorApplication>(*args)
}

