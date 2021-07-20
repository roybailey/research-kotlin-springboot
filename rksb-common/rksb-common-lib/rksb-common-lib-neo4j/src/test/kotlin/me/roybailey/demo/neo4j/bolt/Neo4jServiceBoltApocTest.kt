package me.roybailey.demo.neo4j.bolt

import me.roybailey.demo.neo4j.Neo4jDataLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


class Neo4jServiceBoltApocTest : Neo4jServiceBoltTestBase() {


    @Test
    fun testApocJsonLoadAgainstBoltServer() {
        Neo4jDataLoader(neo4jService).loadData(
            mapOf<String, Any>(
                "planetsUrl" to "file:///planets.json",
                "peopleUrl" to "file:///people.json"
            )
        )
        var count = 0
        neo4jService.execute("match (n) return count(n)", emptyMap()) { result ->
            count = result.next().get(0).asInt()
        }
        assertThat(count).isEqualTo(20)
    }
}
