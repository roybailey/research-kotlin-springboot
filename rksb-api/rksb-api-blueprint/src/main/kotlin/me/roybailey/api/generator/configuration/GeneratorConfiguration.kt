package me.roybailey.api.generator.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import me.roybailey.api.blueprint.BlueprintCompiler
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.blueprint.FlywayMigration
import me.roybailey.api.generator.service.AsciiDocGenerator
import me.roybailey.api.generator.service.CodeGenerator
import me.roybailey.api.generator.service.DatabaseCodeGenerator
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
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var blueprintCompiler: BlueprintCompiler

    @Autowired
    lateinit var databaseCodeGenerator: DatabaseCodeGenerator

    @Autowired
    lateinit var codeGenerator: CodeGenerator

    @Autowired
    lateinit var asciiDocGenerator: AsciiDocGenerator


    @Bean("database-migration")
    open fun databaseMigration(): FlywayMigration {
        logger.info("---->>>> databaseMigration()")
        val databaseMigration = FlywayMigration(
            url = blueprintProperties.blueprintsDatabaseUrl,
            username = blueprintProperties.blueprintsDatabaseUsername,
            password = blueprintProperties.blueprintsDatabasePassword
        )
        databaseMigration.call()
        return databaseMigration
    }

    @Bean("blueprint-compiler")
    @DependsOn("database-migration")
    open fun blueprintCompiler(): BlueprintCompiler {
        logger.info("---->>>> blueprintCompiler()")
        val blueprintCollection = blueprintCompiler.compileBlueprints()

        if(blueprintProperties.codegenWriteFilesEnabled) {
            File(generatorProperties.basedir+"/"+blueprintProperties.codegenBlueprintCollection)
                .writer().run {
                    write(mapper.writeValueAsString(blueprintCollection))
                    flush()
                    close()
                }
        }

        return blueprintCompiler
    }

    @Bean("database-code-generator")
    @DependsOn("blueprint-compiler")
    open fun databaseCodeGenerator(): DatabaseCodeGenerator {
        logger.info("---->>>> databaseCodeGenerator()")
        databaseCodeGenerator.call()
        return databaseCodeGenerator
    }

    @Bean("code-generator")
    @DependsOn("blueprint-compiler")
    open fun codeGenerator(): CodeGenerator {
        logger.info("---->>>> codeGenerator()")
        codeGenerator.call()
        return codeGenerator
    }

    @Bean("asciidoc-generator")
    @DependsOn("blueprint-compiler")
    open fun asciiDocGenerator(): AsciiDocGenerator {
        logger.info("---->>>> asciiDocGenerator()")
        asciiDocGenerator.call()
        return asciiDocGenerator
    }

}
