package me.roybailey.demo.configuration

import me.roybailey.demo.neo4j.Neo4jService
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.time.Instant


@Component
class Neo4jBootstrap(
        private val neo4jService: Neo4jService
) {

    private val logger = KotlinLogging.logger {}

    fun initializeGraph(token: String?) {

        val initializeTime = Instant.now()
        try {
            val params = mapOf("token" to (token ?: System.getenv("API_TOKEN")))
            val planets = Neo4jBootstrap::class.java.getResource("/cypher/planets.cypher")!!.readText()
            neo4jService.execute(planets, params) {
                logger.info { "Loaded Star Wars Planets Graph Data" }
            }

            val people = Neo4jService::class.java.getResource("/cypher/people.cypher")!!.readText()
            neo4jService.execute(people, params) {
                logger.info { "Loaded Star Wars People Graph Data" }
            }

            val count: Long? = neo4jService.queryForObject("match (n) return count(n)", mutableMapOf())
            logger.info {
                "Loaded Graph Data $count in ${
                    (Instant.now().toEpochMilli() - initializeTime.toEpochMilli())
                } millis"
            }
        } catch(err:Exception) {
            logger.error( "Failed to load Star Wars data with ", err )
        }
    }
}
