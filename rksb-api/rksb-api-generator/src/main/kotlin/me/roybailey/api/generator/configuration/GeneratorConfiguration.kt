package me.roybailey.api.generator.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Options
import com.github.jknack.handlebars.helper.DefaultHelperRegistry
import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.blueprint.BlueprintDatabaseMigration
import me.roybailey.api.blueprint.BlueprintProperties
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

//    @Autowired
//    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var blueprintCompiler: BlueprintCompiler

//    @Autowired
//    lateinit var databaseCodeGenerator: DatabaseCodeGenerator
//
//    @Autowired
//    lateinit var codeGenerator: CodeGenerator
//
//    @Autowired
//    lateinit var asciiDocGenerator: AsciiDocGenerator


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


    @Bean("database-migration")
    open fun databaseMigration(): BlueprintDatabaseMigration {
        logger.info("---->>>> databaseMigration()")
        val databaseMigration = BlueprintDatabaseMigration(
            url = blueprintProperties.blueprintsDatabaseUrl,
            username = blueprintProperties.blueprintsDatabaseUsername,
            password = blueprintProperties.blueprintsDatabasePassword
        )
        databaseMigration.call()
        return databaseMigration
    }

    @Bean("blueprint-compiler")
    @DependsOn("database-migration")
    open fun blueprintCompiler(): BlueprintCollection {
        logger.info("---->>>> blueprintCompiler()")
        val blueprintCollection = blueprintCompiler.compileBlueprintsTemplates()

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

//    @Bean("database-code-generator")
//    @DependsOn("blueprint-compiler")
//    open fun databaseCodeGenerator(): DatabaseCodeGenerator {
//        logger.info("---->>>> databaseCodeGenerator()")
//        databaseCodeGenerator.call()
//        return databaseCodeGenerator
//    }
//
//    @Bean("code-generator")
//    @DependsOn("blueprint-compiler")
//    open fun codeGenerator(): CodeGenerator {
//        logger.info("---->>>> codeGenerator()")
//        codeGenerator.call()
//        return codeGenerator
//    }
//
//    @Bean("asciidoc-generator")
//    @DependsOn("blueprint-compiler")
//    open fun asciiDocGenerator(): AsciiDocGenerator {
//        logger.info("---->>>> asciiDocGenerator()")
//        asciiDocGenerator.call()
//        return asciiDocGenerator
//    }

}
