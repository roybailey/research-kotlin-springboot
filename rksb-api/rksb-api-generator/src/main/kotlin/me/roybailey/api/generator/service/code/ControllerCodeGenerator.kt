package me.roybailey.api.generator.service.code

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.generator.configuration.GeneratorFileContent
import mu.KotlinLogging
import org.springframework.stereotype.Component


@Component
class ControllerCodeGenerator : AbstractCodeGenerator() {

    private val logger = KotlinLogging.logger {}

    val controllerTemplate = """ 
package {{controllerMapping.packageName}}

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.api.common.ApiResponse
import {{serviceMapping.packageName}}.{{serviceMapping.className}}
import {{modelMapping.packageName}}.{{modelMapping.className}}


@RestController
@RequestMapping("{{controllerMapping.apiPath}}")
class {{controllerMapping.className}}(
    private val {{serviceMapping.variableName}}: {{serviceMapping.className}}
) : BaseController(
    blueprintId = "{{blueprint.id}}",
    controllerMappingId = "{{controllerMapping.id}}"
) {

    {{#each controllerMapping.endpoints}}
    @GetMapping
    @RequestMapping("{{apiPath}}")
    fun {{apiMethodName}}(request:HttpServletRequest): ApiResponse<{{modelMapping.className}}> {
        val params = LinkedHashMap(request.parameterMap).apply { 
            putAll(getApiRequestParameters("{{apiPath}}"))
        }
        val results = {{serviceMapping.variableName}}.{{serviceMethodName}}(params)
        return ApiResponse(results.size, results)
    }
    {{/each}}
}
    """.trimIndent().trim()


    override fun generate() : List<GeneratorFileContent> {

        val results = mutableListOf<GeneratorFileContent>()

        blueprintCollection.blueprints.forEach { blueprint ->
            logger.info("Processing blueprint=${blueprint.id}")
            blueprint.controllers.forEach { controllerMapping ->
                logger.info("Processing controllerMapping=${controllerMapping.id}")

                val serviceMapping = blueprintCollection.allServices().find { it.id == controllerMapping.serviceMappingId }!!
                val tableMapping = blueprintCollection.allTables().find { it.id == serviceMapping.tableMappingId }!!
                val modelMapping = blueprintCollection.allModels().find { it.id == serviceMapping.modelMappingId }!!

                val model = mapOf(
                    Pair("blueprint", blueprint),
                    Pair("controllerMapping", controllerMapping),
                    Pair("serviceMapping", serviceMapping),
                    Pair("tableMapping", tableMapping),
                    Pair("modelMapping", modelMapping),
                )
                val handlebars = Handlebars()
                val template = handlebars.compileInline(controllerTemplate)
                results += GeneratorFileContent(
                    getFilename(controllerMapping.packageName!!, controllerMapping.className!!),
                    template.apply(model)
                )
            }
        }

        return results
    }
}
