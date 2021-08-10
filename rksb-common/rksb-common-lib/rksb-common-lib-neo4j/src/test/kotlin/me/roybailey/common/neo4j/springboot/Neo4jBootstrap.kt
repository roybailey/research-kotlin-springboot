package me.roybailey.common.neo4j.springboot

import me.roybailey.common.neo4j.Neo4jDataLoader
import me.roybailey.common.neo4j.Neo4jService
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component


@Component
class Neo4jBootstrap(
        private val neo4jService: Neo4jService
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "Neo4jBootstrap.onApplicationEvent($event)" }
        Neo4jDataLoader(neo4jService).loadData()
    }
}
