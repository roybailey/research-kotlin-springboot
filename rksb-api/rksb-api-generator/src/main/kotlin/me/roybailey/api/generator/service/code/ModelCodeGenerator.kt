package me.roybailey.api.generator.service.code

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.generator.configuration.GeneratorFileContent
import mu.KotlinLogging
import org.springframework.stereotype.Component


@Component
class ModelCodeGenerator : AbstractCodeGenerator() {

    private val logger = KotlinLogging.logger {}

    val modelTemplate = """ 
package {{modelMapping.packageName}}

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp


data class {{modelMapping.className}} (
    {{#each modelMapping.fields}}
    @JsonProperty("{{jsonName}}") var {{fieldName}}: {{fieldType}},
    {{/each}}
)

    """.trimIndent().trim()

    override fun generate(): List<GeneratorFileContent> {

        val results = mutableListOf<GeneratorFileContent>()

        blueprintCollection.blueprints.forEach { blueprint ->
            logger.info("Processing blueprint=${blueprint.id}")
            blueprint.services.forEach { serviceMapping ->
                logger.info("Processing serviceMapping=${serviceMapping.id}")

                val tableMapping = blueprintCollection.allTables().find { it.id == serviceMapping.tableMappingId }!!
                val modelMapping = blueprintCollection.allModels().find { it.id == serviceMapping.modelMappingId }!!

                val model = mapOf(
                    Pair("blueprint", blueprint),
                    Pair("serviceMapping", serviceMapping),
                    Pair("tableMapping", tableMapping),
                    Pair("modelMapping", modelMapping),
                )
                val handlebars = Handlebars()
                val template = handlebars.compileInline(modelTemplate)
                results += GeneratorFileContent(
                    getFilename(modelMapping.packageName!!, modelMapping.className!!),
                    template.apply(model)
                )
            }
        }

        return results
    }
}
