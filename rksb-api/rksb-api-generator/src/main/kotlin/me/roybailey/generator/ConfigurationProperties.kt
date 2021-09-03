package me.roybailey.generator

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class ConfigurationProperties {

    @Value("\${project.basedir}")
    lateinit var basedir: String

    @Value("\${project.target:/src/main/kotlin}")
    lateinit var target: String

    @Value("\${project.package:me.roybailey.codegen}")
    lateinit var basePackageName: String

    @Value("\${project.database.excludes:.*schema.*|pg_.*}")
    lateinit var excludes: String

    @Value("\${project.database.url:jdbc:postgresql://localhost:5432/postgres}")
    lateinit var databaseUrl: String

    @Value("\${project.database.driver:org.postgresql.Driver}")
    lateinit var databaseDriver: String

    @Value("\${project.database.username:postgres}")
    lateinit var databaseUsername: String

    @Value("\${project.database.password:localhost}")
    lateinit var databasePassword: String

}
