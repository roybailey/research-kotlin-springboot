package me.roybailey.common.neo4j

import mu.KotlinLogging
import org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME
import org.neo4j.configuration.connectors.BoltConnector
import org.neo4j.configuration.helpers.SocketAddress
import org.neo4j.dbms.api.DatabaseManagementService
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.driver.Driver
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.kernel.internal.GraphDatabaseAPI
import java.io.File
import java.net.InetAddress
import java.time.Instant.now
import kotlin.system.exitProcess


@Suppress("UNCHECKED_CAST")
open class Neo4jEmbeddedService(val options: Neo4jServiceOptions) : Neo4jService {

    private val logger = KotlinLogging.logger {}
    private val instanceSignature = InetAddress.getLocalHost().canonicalHostName + "-" + hashCode()

    private val neo4jConfiguration = Neo4jService::class.java.getResource("/neo4j.conf")

    private var neo4jDatabaseFolder: File
    var graphDbService: DatabaseManagementService
    var graphDb: GraphDatabaseService
    var neo4jBoltService: Neo4jBoltService

    init {
        val (neo4jUri, boltPort) = options
        logger.info("########### ########## ########## ########## ##########")
        logger.info("Creating Neo4j Database with neo4jUri=$neo4jUri instance=$instanceSignature")
        logger.info("########### ########## ########## ########## ##########")

        try {
            neo4jDatabaseFolder = File(
                when {
                    neo4jUri.trim().isEmpty() -> createTempDir(
                        "${Neo4jService::class.simpleName}-",
                        "-$instanceSignature"
                    ).canonicalPath
                    neo4jUri.startsWith("file://") -> File(neo4jUri.substring("file://".length)).canonicalPath
                    else -> throw IllegalArgumentException("neo4jUri must be file:// based as only embedded instance supported")
                }.replace("{timestamp}", now().toString()) + "/graph.db"
            )
            logger.info("Creating Neo4j Database at $neo4jDatabaseFolder")

            val graphDbBuilder = DatabaseManagementServiceBuilder(neo4jDatabaseFolder)
                .loadPropertiesFromFile(neo4jConfiguration!!.toURI().toString().replace("file:",""))

            if (boltPort > 0) {
                // val bolt = BoltConnector()
                val boltListenAddress = "0.0.0.0"
                val boltAdvertisedAddress = InetAddress.getLocalHost().hostName
                graphDbBuilder
                    .setConfig(BoltConnector.enabled, true)
                    .setConfig(BoltConnector.listen_address, SocketAddress(boltListenAddress, boltPort))
                    .setConfig(
                        BoltConnector.advertised_address,
                        SocketAddress(InetAddress.getLocalHost().hostName, boltPort)
                    )
                logger.info("Creating Neo4j Bolt Connector on Port : $boltPort")
                logger.info("Creating Neo4j Bolt Listen Address : $boltListenAddress")
                logger.info("Creating Neo4j Bolt Advertised Address : $boltAdvertisedAddress")
            }

            graphDbService = graphDbBuilder.build()
            graphDb = graphDbService.database(DEFAULT_DATABASE_NAME)
        } catch (err: Exception) {
            logger.error("########### ########## ########## ########## ##########")
            logger.error("!!!!!!!!!! Error creating Neo4j Database !!!!!!!!!!")
            logger.error("Error creating Neo4j Database", err)
            logger.error("########### ########## ########## ########## ##########")
            exitProcess(-1)
        }

        logger.info("Created Neo4j Database from: $neo4jConfiguration")

        neo4jBoltService = Neo4jBoltService(
            Neo4jServiceOptions(
                "bolt://0.0.0.0", boltPort, "neo4j", "", emptyList()
            )
        )
        logger.info("Created Neo4j Bolt Connection: $neo4jBoltService")

        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                shutdown()
            }
        })
    }


    override fun toString(): String =
        "Neo4jEmbeddedService{ options=$options, neo4jDatabaseFolder=$neo4jDatabaseFolder }"


    override fun shutdown() {
        try {
            logger.info("########### ########## ########## ########## ##########")
            logger.info("Shutdown Neo4j Database options=$options instance=${hashCode()}")
            logger.info("########### ########## ########## ########## ##########")
            graphDbService.shutdown()
        } catch (err: Exception) {
            logger.warn("Unable to shutdown Neo4j embedded database: $err")
        }
    }


    override fun isEmbedded(): Boolean = options.neo4jUri.startsWith("file://")
    override fun driver(): Driver = neo4jBoltService.driver()


    override fun registerProcedures(toRegister: List<Class<*>>): Neo4jService {
        if (isEmbedded()) {
            val procedures = graphDb
                .let { it as GraphDatabaseAPI }
                .dependencyResolver
                .resolveDependency(org.neo4j.kernel.api.procedure.GlobalProcedures::class.java)

            toRegister.forEach { proc ->
                try {
                    logger.info { "Registering Procedures $proc" }
                    procedures.registerProcedure(proc, true)
                    procedures.registerFunction(proc, true)

                } catch (e: Exception) {
                    throw RuntimeException("Error registering $proc", e)
                }
            }
        }
        return this
    }


    override fun execute(cypher: String, params: Map<String, Any>, code: Neo4jResultMapper): Neo4jService {
        neo4jBoltService.execute(cypher, params, code)
        return this
    }

}

