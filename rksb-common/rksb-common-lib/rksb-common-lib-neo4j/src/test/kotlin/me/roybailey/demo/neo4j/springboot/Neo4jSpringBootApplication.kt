package me.roybailey.demo.neo4j.springboot

import me.roybailey.demo.neo4j.Neo4jConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.test.context.ActiveProfiles

// under test source because it's only needed for testing
@SpringBootApplication(scanBasePackageClasses = [Neo4jConfiguration::class])
open class Neo4jSpringBootApplication
