package me.roybailey.demo.neo4j

import me.roybailey.demo.testdata.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
open class Neo4jSpringBootApplicationTest : UnitTestBase() {

	@Autowired
	lateinit var neo4jService : Neo4jService

	@Test
	fun contextLoads() {
		var count = 0
		neo4jService.execute("match (n) return count(n)", emptyMap()) { result ->
			count = result.next().get(0).asInt()
		}
		assertThat(count).isEqualTo(20)
	}

}
