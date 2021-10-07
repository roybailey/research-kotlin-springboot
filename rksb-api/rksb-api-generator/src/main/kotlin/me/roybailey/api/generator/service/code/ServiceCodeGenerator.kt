package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.configuration.GeneratorFileContent
import mu.KotlinLogging
import org.springframework.stereotype.Component


@Component
class ServiceCodeGenerator : AbstractCodeGenerator() {

    private val logger = KotlinLogging.logger {}

    val serviceTemplate = """ 
package {{serviceMapping.packageName}}

import me.roybailey.api.common.BaseService
import me.roybailey.codegen.jooq.database.Tables.{{uppercase tableMapping.tableName}}
import {{modelMapping.packageName}}.{{modelMapping.className}}

import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired


@Component
open class {{serviceMapping.className}} : BaseService(
    blueprintId = "{{blueprint.id}}",
    serviceMappingId = "{{serviceMapping.id}}",
    tableMappingId = "{{tableMapping.id}}",
    modelMappingId = "{{modelMapping.id}}"
) {

    @Autowired
    protected lateinit var dsl: DSLContext

    open fun getAllData(params: Map<String,Any>): List<{{modelMapping.className}}> {
        val results = dsl
            .select()
            .from({{uppercase tableMapping.tableName}})
            .where(getFilterCondition(params, {{uppercase tableMapping.tableName}}))
            .limit(100)
            .fetch()
            .into({{modelMapping.className}}::class.java)
        return results;
    }
    
    {{#each serviceMethods}}
    open fun {{this}}(params: Map<String,Any>): List<{{modelMapping.className}}> {
        throw RuntimeException("{{serviceMapping.className}}.{{this}} Not Implemented")
    }
    {{/each}}
}
    """.trimIndent().trim()

    override fun generate() : List<GeneratorFileContent> {

        val results = mutableListOf<GeneratorFileContent>()

        blueprintCollection.blueprints.forEach { blueprint ->
            logger.info("Processing blueprint=${blueprint.id}")
            blueprint.services.forEach { serviceMapping ->
                logger.info("Processing serviceMapping=${serviceMapping.id}")

                val tableMapping = blueprintCollection.allTables().find { it.id == serviceMapping.tableMappingId }!!
                val modelMapping = blueprintCollection.allModels().find { it.id == serviceMapping.modelMappingId }!!
                val serviceMethods = blueprintCollection.allControllers()
                    .asSequence()
                    .filter { controllerMapping -> controllerMapping.serviceMappingId == serviceMapping.id }
                    .map { controllerMapping -> controllerMapping.endpoints }
                    .flatten()
                    .map { endpointMapping -> endpointMapping.serviceMethodName }
                    .filter { serviceMethodName -> serviceMethodName != "getAllData" }
                    .toList()

                val model = mapOf(
                    Pair("blueprint", blueprint),
                    Pair("serviceMapping", serviceMapping),
                    Pair("tableMapping", tableMapping),
                    Pair("modelMapping", modelMapping),
                    Pair("serviceMethods", serviceMethods),
                )
                val template = handlebars.compileInline(serviceTemplate)
                results += GeneratorFileContent(
                    getFilename(serviceMapping.packageName!!, serviceMapping.className!!),
                    template.apply(model)
                )
            }
        }

        return results
    }

}
