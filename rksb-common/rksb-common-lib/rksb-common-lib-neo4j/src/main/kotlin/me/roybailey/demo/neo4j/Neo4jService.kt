package me.roybailey.demo.neo4j

import org.neo4j.driver.Driver
import org.neo4j.driver.Record
import org.neo4j.driver.Result
import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction
import java.util.*


data class Neo4jServiceOptions(
        val neo4jUri: String,
        val boltPort: Int = -1,
        val username: String = "neo4j",
        val password: String = "",
        val neo4jProcedures: List<Class<*>> = Neo4jService.getDefaultProcedures(),
        val ignoreErrorOnDrop: Boolean = true,
        val ignoreProcedureNotFound: Boolean = true
) {
    val mode: String = neo4jUri.toLowerCase().substring(0, 4)
}


typealias Neo4jServiceStatementResult = Result
typealias Neo4jServiceRecord = Record

typealias Neo4jResultMapper = (result: Neo4jServiceStatementResult) -> Unit
typealias Neo4jRecordMapper<T> = (record: Neo4jServiceRecord) -> T

val nullNeo4jResultMapper = { _:Neo4jServiceStatementResult -> }


interface Neo4jService {

    fun registerProcedures(toRegister: List<Class<*>>): Neo4jService
    fun shutdown()
    fun isEmbedded(): Boolean
    fun driver(): Driver

    fun execute(cypher: String, params: Map<String, Any> = emptyMap(), code: Neo4jResultMapper = nullNeo4jResultMapper): Neo4jService

    /**
     * Query for a list of records
     *
     * @param cypher - the append query string
     * @param params - the map of append query parameters
     * @param mapper - the record mapper to convert Neo4jServiceRecord objects into <T> objects
     * @return list of <T> objects; otherwise empty list
     */
    fun <T> query(cypher: String, params: Map<String, Any> = emptyMap(), mapper: Neo4jRecordMapper<T>): List<T> {
        val result = mutableListOf<T>()
        execute(cypher, params) { statementResult ->
            while (statementResult.hasNext())
                mapper(statementResult.next() as Neo4jServiceRecord)?.let { result.add(it) }
        }
        return result.toList()
    }


    /**
     * Query for an object
     *
     * @param cypher - the append query string
     * @param params - the map of append query parameters
     * @param mapper - the record mapper to convert the first Neo4jServiceRecord object into <T> object;
     * null to take the singular value returned as a primitive
     * @return the object result
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> queryForObject(cypher: String, params: Map<String, Any> = emptyMap(), mapper: Neo4jRecordMapper<T>? = null): T? {
        var result: T? = null
        execute(cypher, params) {
            if (it.hasNext()) {
                val value = it.next()
                result = mapper?.let { it(value as Neo4jServiceRecord) } ?: (value.asMap().entries.first().value as T)
            }
        }
        return result
    }


    /**
     * Sets an apoc static value in the neo4j database, for use in cypher later
     * @param key the name of the static variable to assign
     * @param value the value to assign the static variable
     * @return this service for chaining multiple calls
     */
    fun setStatic(key: String, value: Any): Neo4jService {
        // set static global variables such as sensitive connection values...
        execute("call apoc.static.set('$key', '${value}')", emptyMap())
        val storedValue = getStatic(key)
        if (storedValue != value)
            throw RuntimeException("Failed to assign static key [$key] with value [$value], came back with [$storedValue] instead")
        return this
    }


    /**
     * Gets an apoc static value from the neo4j database
     * @param key the name of the static variable to assign
     * @param defaultValue the value to return if no static variable value is found (defaults to null)
     * @return the static variable value from the database; otherwise the default value provided
     */
    fun getStatic(key: String, defaultValue: Any? = null): Any? {
        var result: Any? = null
        execute("call apoc.static.get('$key')", emptyMap()) {
            if (it.hasNext())
                result = it.single()["value"]
        }
        return result?.let { result } ?: defaultValue
    }


    companion object {

        fun getInstance(options: Neo4jServiceOptions): Neo4jService {
            val neo4jService = when (options.mode) {
                "bolt" -> Neo4jBoltService(options)
                else -> Neo4jEmbeddedService(options)
            }
            neo4jService.registerProcedures(options.neo4jProcedures)
            return neo4jService
        }

        fun getDefaultProcedures(): List<Class<out Any>> {
            return listOf(
                    Neo4jServiceProcedures::class.java,
                    apoc.help.Help::class.java,
                    apoc.coll.Coll::class.java,
                    apoc.create.Create::class.java,
                    apoc.path.PathExplorer::class.java,
                    apoc.meta.Meta::class.java,
                    apoc.refactor.GraphRefactoring::class.java,
                    apoc.cache.Static::class.java,
                    apoc.lock.Lock::class.java,
                    apoc.text.Strings::class.java,
                    apoc.date.Date::class.java,
                    apoc.map.Maps::class.java,
                    apoc.convert.Json::class.java,
                    apoc.convert.Convert::class.java,
                    apoc.load.Jdbc::class.java,
                    apoc.load.LoadJson::class.java,
                    apoc.load.Xml::class.java,
                    apoc.periodic.Periodic::class.java
            )
        }
    }
}
