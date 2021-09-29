package me.roybailey.api.generator.service.code

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.generator.configuration.GeneratorFileContent
import me.roybailey.api.generator.configuration.GeneratorProperties
import me.roybailey.api.generator.configuration.GeneratorResult
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.Callable


abstract class AbstractCodeGenerator : Callable<GeneratorResult> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var handlebars: Handlebars


    fun getFilename(packageName: String, className: String, extension: String = "kt"): String {
        return "${packageName.replace(".", "/")}/${className}.$extension"
    }


    override fun call(): GeneratorResult {
        val result = GeneratorResult(name = this.javaClass.simpleName)
        logger.info("${result.name} - STARTING")

        try {
            result.content = generate()
        } catch (err: Exception) {
            logger.error("${result.name} failed with error", err)
            result.error = err
        }

        logger.info("${result.name} - FINISHED")
        return result
    }


    abstract fun generate(): List<GeneratorFileContent>
}
