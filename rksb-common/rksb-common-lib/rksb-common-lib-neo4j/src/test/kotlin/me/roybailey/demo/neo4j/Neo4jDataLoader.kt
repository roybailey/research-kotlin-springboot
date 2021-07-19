package me.roybailey.demo.neo4j

import mu.KotlinLogging
import java.time.Instant


class Neo4jDataLoader(private val neo4jService: Neo4jService) {

    private val logger = KotlinLogging.logger {}

    fun loadData(importParams:Map<String,Any> = emptyMap()) {
        logger.info { "Neo4jDataLoader.dataLoad()" }

        val initializeTime = Instant.now()
        try {
            val params = mutableMapOf<String,Any>(
                "token" to System.getenv("API_TOKEN"),
                "planetsUrl" to Neo4jDataLoader::class.java.getResource("/import/planets.json")!!.toURI().toString(),
                "peopleUrl" to Neo4jDataLoader::class.java.getResource("/import/people.json")!!.toURI().toString()
            )
            params.putAll(importParams)
            val planets = Neo4jDataLoader::class.java.getResource("/cypher/planets.cypher")!!.readText()
            neo4jService.execute(planets, params) {
                it.hasNext() // this will trigger any errors
                logger.info { "Loaded Star Wars Planets Graph Data" }
            }

            val people = Neo4jDataLoader::class.java.getResource("/cypher/people.cypher")!!.readText()
            neo4jService.execute(people, params) {
                it.hasNext() // this will trigger any errors
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
