package me.roybailey.generator.code

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.blueprint.*
import me.roybailey.generator.GeneratorResult
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.nio.file.Files.createDirectories
import java.nio.file.Path


@Component
class PojoCodeGenerator : AbstractCodeGenerator() {

    private val logger = KotlinLogging.logger {}

    val pojoTemplate = """ 
package {{pojoMapping.packageName}}

import com.fasterxml.jackson.annotation.JsonProperty


data class {{pojoMapping.name}} (
    {{#each pojoMapping.fieldMapping}}
    @JsonProperty("{{jsonName}}") var {{fieldName}}: {{fieldType}},
    {{/each}}
)

    """.trimIndent().trim()

    override fun generateClass(
        writeToFile: Boolean,
        basePackageDirectory: String,
        basePackageName: String
    ): GeneratorResult {

        apiBlueprints.forEach { apiBlueprint ->
            apiBlueprint.tableMapping.forEach { tableMapping: ApiTableMapping ->
                val domainLowercase = tableMapping.domain.toLowerCase()
                val domainPackageDirectory = "$basePackageDirectory/$domainLowercase"
                val domainPackageName = "$basePackageName.$domainLowercase"

                logger.info("Processing apiBlueprint=$apiBlueprint tableMappings=$tableMapping domain=${tableMapping.domain} packageDirectory=$domainPackageDirectory")
                createDirectories(Path.of(domainPackageDirectory))

                val fieldMapping = tableMapping.columnMapping.map { columnMapping ->
                    ApiFieldMapping(
                        fieldName = columnMapping.column,
                        fieldType = apiBlueprintProperties.getFieldType(columnMapping.type),
                        jsonName = columnMapping.column,
                    )
                }
                val model = mapOf(
                    Pair("apiBlueprint", apiBlueprint),
                    Pair("tableMapping", tableMapping),
                    Pair(
                        "pojoMapping",
                        ApiPojoMapping(
                            id = tableMapping.domain,
                            packageName = domainPackageName,
                            name = tableMapping.domain,
                            domain = tableMapping.domain,
                            fieldMapping = fieldMapping,
                        )
                    ),
                )
                val handlebars = Handlebars()
                val template = handlebars.compileInline(pojoTemplate)
                val output = template.apply(model)

                if (writeToFile) {
                    writeToFile("$domainPackageDirectory/${tableMapping.domain}.kt", output)
                }
            }
        }
        return true
    }
}
