package me.roybailey.service.demo.configuration

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${starwars.api.planets.uri}")
    lateinit var starwarsApiPlanetUri: String

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
            logger.info { "API token not found, attempting to load graph database anyway..." }
            neo4jBootstrap.initializeGraph(token)
        } else {
            logger.info { "API token, validating token..." }
            val apiTest = RestTemplate()
            val response = apiTest.getForEntity(
                starwarsApiPlanetUri,
                String::class.java
            )
            if (response.statusCode.is2xxSuccessful) {
                logger.info { "API token valid, loading graph database..." }
                neo4jBootstrap.initializeGraph(token)
            } else {
                exitError("Error response from API ${response.statusCode.value()} from API_TOKEN=${token}")
            }
        }
    }
}
