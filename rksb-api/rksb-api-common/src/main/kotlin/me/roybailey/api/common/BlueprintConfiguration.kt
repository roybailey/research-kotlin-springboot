package me.roybailey.api.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.RuntimeException


@Configuration
@ConditionalOnProperty(value = ["blueprints.enabled"], havingValue = "true", matchIfMissing = true)
open class BlueprintConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    open fun jsonMapper() = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)

    @Bean
    open fun defaultBlueprintCollection(): BlueprintCollection {
        val blueprintCollectionFilename = "blueprint-collection.json"
        try {
            return jsonMapper().readValue(
                this.javaClass.classLoader.getResourceAsStream(blueprintCollectionFilename),
                BlueprintCollection::class.java
            )
        } catch (err: Exception) {
            throw RuntimeException("Failed to load BlueprintCollection [$blueprintCollectionFilename] with [${err.message}]", err)
        }
    }
}

