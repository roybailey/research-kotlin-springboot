package me.roybailey.api.blueprint

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnProperty(value = ["blueprints.enabled"], havingValue = "true", matchIfMissing = true)
open class BlueprintConfiguration {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties : BlueprintProperties

    @Bean
    open fun jsonMapper() = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)

    @Bean
    open fun defaultBlueprintCollection(): BlueprintCollection {
        try {
            logger.info("Loading BlueprintCollection [${blueprintProperties.blueprintsCollectionFilename}]...")
            return jsonMapper().readValue(
                this.javaClass.classLoader.getResourceAsStream(blueprintProperties.blueprintsCollectionFilename),
                BlueprintCollection::class.java
            )
        } catch (err: Exception) {
            throw RuntimeException("Failed to load BlueprintCollection [${blueprintProperties.blueprintsCollectionFilename}] with [${err.message}]", err)
        }
    }
}

