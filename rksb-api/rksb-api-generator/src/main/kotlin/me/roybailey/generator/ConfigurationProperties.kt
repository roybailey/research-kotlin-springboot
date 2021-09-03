package me.roybailey.generator

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class ConfigurationProperties {

    @Value("\${project.basedir}")
    lateinit var basedir: String

    @Value("\${project.target:/src/main/kotlin}")
    lateinit var target: String

    @Value("\${app.codegen.base.package}")
    lateinit var basePackageName: String

    @Value("\${project.database.excludes:.*schema.*|pg_.*}")
    lateinit var excludes: String

    @Value("\${app.datasource.main.url}")
    lateinit var mainDatabaseUrl: String

    @Value("\${app.datasource.main.driver:org.postgresql.Driver}")
    lateinit var mainDatabaseDriver: String

    @Value("\${app.datasource.main.username}")
    lateinit var mainDatabaseUsername: String

    @Value("\${app.datasource.main.password}")
    lateinit var mainDatabasePassword: String

    @Value("\${app.datasource.jooq.url}")
    lateinit var jooqDatabaseUrl: String

    @Value("\${app.datasource.jooq.driver:org.postgresql.Driver}")
    lateinit var jooqDatabaseDriver: String

    @Value("\${app.datasource.jooq.username}")
    lateinit var jooqDatabaseUsername: String

    @Value("\${app.datasource.jooq.password}")
    lateinit var jooqDatabasePassword: String

    @Value("\${app.datasource.jooq.schema}")
    lateinit var jooqDatabaseSchema: String

}
