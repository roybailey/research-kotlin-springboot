package me.roybailey.demo.neo4j

import mu.KotlinLogging
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import java.net.InetAddress
import kotlin.system.exitProcess


@Suppress("UNCHECKED_CAST")
open class Neo4jBoltService(val options: Neo4jServiceOptions) : Neo4jService {

    private val logger = KotlinLogging.logger {}
    private val instanceSignature = InetAddress.getLocalHost().canonicalHostName + "-" + hashCode()

    private val neo4jConfiguration = Neo4jService::class.java.getResource("/neo4j.conf")

    private var driver: Driver

    init {
        logger.info("########### ########## ########## ########## ##########")
        logger.info("Creating Neo4j Database options=$options instance=$instanceSignature")
        logger.info("########### ########## ########## ########## ##########")

        logger.info("Created Neo4j Database from: $neo4jConfiguration")

        val neo4jUri = if (options.neo4jUri.substring(7).contains(":")) options.neo4jUri else options.neo4jUri + ":" + options.boltPort

        try {
            driver = GraphDatabase.driver(neo4jUri, AuthTokens.basic(options.username, options.password))
        } catch(err: Exception) {
            logger.error("########### ########## ########## ########## ##########")
            logger.error("!!!!!!!!!! Error connecting to Neo4j Database !!!!!!!!!!")
            logger.error("Error connecting to Neo4j Database", err)
            logger.error("########### ########## ########## ########## ##########")
            exitProcess(-1)
        }

        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                driver.close()
            }
        })
    }

    override fun toString(): String = "Neo4jBoltService{ options=$options }"


    override fun shutdown() {
        try {
            logger.info("########### ########## ########## ########## ##########")
            logger.info("Shutdown Neo4j Database options=$options instance=${hashCode()}")
            logger.info("########### ########## ########## ########## ##########")
            driver.close()
        } catch (err: Exception) {
            logger.warn("Unable to shutdown Neo4j bolt database: $err")
        }
    }


    override fun isEmbedded(): Boolean = false
    override fun driver(): Driver = this.driver


    /**
     * Since a bolt connection can't inject the procedures,
     * we instead query all installed procedures and attempt to verify all the required procedures exist
     */
    override fun registerProcedures(toRegister: List<Class<*>>): Neo4jService {
        val unregisteredProcedures = mutableSetOf<String>()
        unregisteredProcedures.addAll(toRegister.map { it.name.toLowerCase() })
        query("CALL dbms.procedures()") {
            record -> record.asMap()
        }.map {
            logger.debug { it }
            it
        }.forEach { procedure ->
            procedure["name"].let { procedureName ->
                if (!unregisteredProcedures.remove(procedureName)) {
                    val packageName = procedureName.toString().substring(0, procedureName.toString().lastIndexOf('.'))
                    val packageProcedures = unregisteredProcedures.filter { it.startsWith(packageName) }
                    packageProcedures.forEach {
                        logger.debug { "Package found $it" }
                        unregisteredProcedures.remove(it)
                    }
                } else {
                    logger.debug { "Procedure found $procedureName" }
                }
            }
        }
        unregisteredProcedures.forEach { logger.error { "Stored Procedure not found using classname or package name $it" } }
        if (!options.ignoreProcedureNotFound && unregisteredProcedures.size > 0) {
            throw RuntimeException("Stored procedures not found $unregisteredProcedures")
        }
        return this
    }


    override fun execute(cypher: String, params: Map<String, Any>, code: Neo4jResultMapper): Neo4jService {
        driver.session().let { session ->
            session.writeTransaction { tx ->
                val result = tx.run(cypher, params)
                code(result)
            }
        }
        return this
    }
}

