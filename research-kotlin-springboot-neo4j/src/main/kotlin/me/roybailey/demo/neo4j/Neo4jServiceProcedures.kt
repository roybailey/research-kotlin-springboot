package me.roybailey.demo.neo4j

import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction
import java.util.*


class Neo4jServiceProcedures {

    @UserFunction("custom.data.encrypt")
    fun format(@Name("value") value: String,
               @Name(value = "key", defaultValue = "") key: String): String =
            String(Base64.getEncoder().encode(value.toByteArray()))
}
