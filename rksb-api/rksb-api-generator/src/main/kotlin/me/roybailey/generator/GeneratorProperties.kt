package me.roybailey.generator

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
open class GeneratorProperties {

    @Value("\${project.basedir}")
    lateinit var basedir: String

    @Value("\${project.version}")
    lateinit var version: String

    @Value("\${project.target:/src/main/kotlin}")
    lateinit var target: String

}
