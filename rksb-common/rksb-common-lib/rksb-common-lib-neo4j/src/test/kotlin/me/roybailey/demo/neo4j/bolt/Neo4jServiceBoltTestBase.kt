package me.roybailey.demo.neo4j.bolt

import me.roybailey.demo.neo4j.Neo4jService
import me.roybailey.demo.neo4j.Neo4jServiceOptions
import me.roybailey.demo.testdata.UnitTestBase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile


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
            .withNeo4jConfig("dbms.directories.import", "/")
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
