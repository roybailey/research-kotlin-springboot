package me.roybailey.api.generator.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Options
import com.github.jknack.handlebars.helper.DefaultHelperRegistry
import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.generator.service.BlueprintCompiler
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import java.io.File


@Configuration
open class GeneratorConfiguration {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var blueprintCompiler: BlueprintCompiler

    @Bean
    open fun jsonMapper():ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)


    @Bean
    open fun handlebars(): Handlebars {
        val handlebars = Handlebars()
        val helperRegistry = DefaultHelperRegistry()

        class HelperSource {
            fun uppercase(context: String, options: Options): String {
                return context.toUpperCase()
            }

            fun lowercase(context: String, options: Options): String {
                return context.toLowerCase()
            }
        }
        helperRegistry.registerHelpers(HelperSource())
        handlebars.with(helperRegistry)
        return handlebars
    }


    @Bean("blueprint-compiler")
    @DependsOn("blueprint-database-migration")
    open fun blueprintCompiler(): BlueprintCollection {
        logger.info("---->>>> blueprintCompiler()")
        val blueprintCollection = blueprintCompiler.compileBlueprintTemplates()

        if(generatorProperties.codegenWriteFilesEnabled) {
            File(generatorProperties.basedir+"/"+generatorProperties.codegenBlueprintCollection)
                .writer().run {
                    write(jsonMapper().writeValueAsString(blueprintCollection))
                    flush()
                    close()
                }
        }

        return blueprintCollection
    }

}
