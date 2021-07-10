package me.roybailey.demo.configuration

import me.roybailey.demo.neo4j.Neo4jBootstrap
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import kotlin.system.exitProcess


@Component
open class ApplicationBootstrap(
    val neo4jBootstrap: Neo4jBootstrap
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = KotlinLogging.logger {}

    private fun exitError(message: String, status: Int = 1): Unit =
        logger.run {
            error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            error(message)
            error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            exitProcess(status)
        }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "onApplicationEvent($event)" }

        val token = System.getenv("API_TOKEN")
        if (token == null || token.isEmpty()) {
            logger.warn("API_TOKEN not found")
        }

        val apiTest = RestTemplate()
        val response = apiTest.getForEntity(
            "https://swapi.dev/api/planets/1/",
            String::class.java
        )
        if (response.statusCode.is2xxSuccessful) {
            neo4jBootstrap.initializeGraph(token)
        } else {
            exitError("Error response from API ${response.statusCode.value()} from API_TOKEN=${token}")
        }
    }
}
