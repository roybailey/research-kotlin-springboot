package me.roybailey.api.generator.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
open class GeneratorProperties {

    @Value("\${project.basedir:./target}")
    lateinit var basedir: String

    @Value("\${project.version:0.0.0-SNAPSHOT}")
    lateinit var version: String

    @Value("\${project.target:/src/main/kotlin}")
    lateinit var target: String

}
