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

}
