package me.roybailey.common.neo4j.bolt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Neo4jServiceBoltBasicTest : Neo4jServiceBoltTestBase() {


    @Test
    fun testNeo4jServiceAgainstBoltServer() {
        var count = 0
        neo4jService.execute("create (n:Test { name:'value' }) return count(n)", emptyMap()) { result ->
            count = result.next().get(0).asInt()
        }
        assertThat(count).isEqualTo(1)
        count = 0
        neo4jService.execute("match (n) return count(n)", emptyMap()) { result ->
            count = result.next().get(0).asInt()
        }
        assertThat(count).isEqualTo(1)
    }
}

