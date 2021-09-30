package me.roybailey.api.generator.service

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.generator.configuration.GeneratorFileContent
import me.roybailey.api.generator.configuration.GeneratorProperties
import me.roybailey.api.generator.configuration.GeneratorResult
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.Callable


@Component
class AsciiDocGenerator : Callable<GeneratorResult> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    val asciiDocTemplate = """
            = {{title}} =

            > Auto-generated from code.  DO NOT EDIT

            :toc:
            :toc-placement!:
            :toc-title: TABLE OF CONTENTS
            :toclevels: 2

            toc::[]

            {{#each blueprints}}
            {{#each apiMapping}}
            == {{id}}
            
            [cols=2*]
            |===
            |`apiPath` | `{{apiPath}}`
            |`tableMappingId` | `tableMappingId`
            |===
            {{/each}}
            {{/each}}

    """.trimIndent()

    override fun call(): GeneratorResult {
        logger.info("AsciiDoc Generation - STARTING")

        val model = mapOf(
            Pair("title", "Blueprints v${generatorProperties.version}"),
            Pair("blueprints", blueprintCollection.blueprints),
        )
        val handlebars = Handlebars()
        val template = handlebars.compileInline(asciiDocTemplate)
        val content = template.apply(model)

        val asciiDocFileName = "${generatorProperties.basedir}/../../BLUEPRINTS.asciidoc"
        logger.info("asciiDocFileName=$asciiDocFileName")

        val asciiDocFile = File(asciiDocFileName)
        asciiDocFile.printWriter().use { out ->
            out.print(content)
        }
        println("\n\n$content\n")

        logger.info("AsciiDoc Generation - FINISHED")
        return GeneratorResult(this.javaClass.simpleName, listOf(GeneratorFileContent(asciiDocFileName, content)))
    }

}

