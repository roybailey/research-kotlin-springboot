package me.roybailey.common.neo4j.bolt

import me.roybailey.common.neo4j.Neo4jService
import me.roybailey.common.neo4j.Neo4jServiceOptions
import me.roybailey.common.test.UnitTestBase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile

object BoltNeo4jServiceFactory {

    fun createNeo4jService(): Neo4jService = Neo4jService.getInstance(
        Neo4jServiceOptions(
            neo4jUri = "bolt://localhost",
            boltPort = 7987,
            username = "neo4j",
            password = "localhost"
        )
    )

}

@Tag("test-containers")
@Testcontainers
abstract class Neo4jServiceBoltTestBase : UnitTestBase() {

    companion object {

        class KNeo4jContainer : Neo4jContainer<KNeo4jContainer>("neo4j:4.0.12-community")

        @JvmStatic
        @Container
        val neo4jContainer = KNeo4jContainer()
            .withAdminPassword("localhost")
            .withPlugins(MountableFile.forClasspathResource("/plugins"))
            .withNeo4jConfig("dbms.security.procedures.unrestricted", "apoc.*")
            .withNeo4jConfig("apoc.import.file.enabled", "true")
            .withNeo4jConfig("dbms.directories.import", "/var/lib/neo4j/import")
            .withFileSystemBind(
                "$moduleResourceFolder/import", "/var/lib/neo4j/import",
                BindMode.READ_WRITE
            )
        //.withNetwork(Network.newNetwork())
        //.withExtraHost("host.testcontainers.localhost", InetAddress.getLocalHost().hostAddress)

    }

    val neo4jImportFolder: String = "/var/lib/neo4j/import"
    val apiTestServer: String = "host.testcontainers.internal"

    var neo4jService: Neo4jService = Neo4jService.getInstance(
        Neo4jServiceOptions(
            neo4jUri = neo4jContainer.boltUrl,
            boltPort = 0,
            username = "neo4j",
            password = "localhost"
        )
    )

    @BeforeEach
    fun setupDatabase(testInfo: TestInfo) {
    }

    @AfterEach
    fun shutdownDatabase(testInfo: TestInfo) {
        neo4jService.shutdown()
    }

}
