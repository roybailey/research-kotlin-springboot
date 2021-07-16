package me.roybailey.demo.neo4j

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.test.context.ActiveProfiles

// under test source because it's only needed for testing
@SpringBootApplication
open class Neo4jSpringBootApplication
