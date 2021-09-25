package me.roybailey.generator.code

import com.github.jknack.handlebars.Handlebars
import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintProperties
import me.roybailey.api.blueprint.ApiTableMapping
import me.roybailey.generator.GeneratorProperties
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files.createDirectories
import java.nio.file.Path
import java.util.concurrent.Callable


@Component
class ServiceCodeGenerator : Callable<Boolean> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    val serviceTemplate = """ 
package {{PACKAGE_NAME}}

import me.roybailey.api.common.BaseService
import me.roybailey.codegen.jooq.database.Tables.{{TABLE_NAME}}
import me.roybailey.codegen.jooq.database.tables.records.{{RECORD_NAME}}
import me.roybailey.codegen.jooq.database.tables.pojos.{{DOMAIN_NAME}}
import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Component


@Component
class {{DOMAIN_NAME}}Service(protected open val dsl: DSLContext) : BaseService("{{apiBlueprint.id}}","{{tableMapping.table}}") {

    fun getAllData(params: Map<String,Any>): List<{{DOMAIN_NAME}}> {
        val results = dsl
            .select()
            .from({{TABLE_NAME}})
            .where(getFilterCondition(params, {{TABLE_NAME}}))
            .limit(100)
            .fetch()
            .into({{DOMAIN_NAME}}::class.java)
        return results;
    }
}
    """.trimIndent().trim()

    override fun call(): Boolean = generate()

    fun generate(writeToFile: Boolean = true) : Boolean {

        logger.info("Service Code Generation - STARTING")
        val basedir = generatorProperties.basedir
        val target = generatorProperties.target
        val basePackageName = "${apiBlueprintProperties.codegenBasePackage}.api"
        val basePackageDirectory = "$basedir/$target/${basePackageName.replace(".", "/")}"

        logger.info("basedir=$basedir")
        logger.info("target=$target")
        logger.info("basePackageName=$basePackageName")
        logger.info("basePackageDirectory=$basePackageDirectory")

        apiBlueprints.forEach { apiBlueprint ->
            apiBlueprint.tableMapping.forEach { tableMapping: ApiTableMapping ->
                val domainLowercase = tableMapping.domain.toLowerCase()
                val domainPackageDirectory = "$basePackageDirectory/$domainLowercase"

                logger.info("Processing apiBlueprint=$apiBlueprint tableMappings=$tableMapping domain=${tableMapping.domain} packageDirectory=$domainPackageDirectory")
                createDirectories(Path.of(domainPackageDirectory))

                val output = generateTableService(apiBlueprint, tableMapping, basePackageName)

                if (writeToFile) {
                    val service = File("$domainPackageDirectory/${tableMapping.domain}Service.kt")
                    service.printWriter().use { out ->
                        out.print("// !!!!! GENERATED BY ${this.javaClass.name} !!!!!\n")
                        out.print(output)
                        out.print("\n\n// !!!!! GENERATED BY ${this.javaClass.name} !!!!!\n")
                    }
                }
            }
        }

        logger.info("Service  Code Generation - FINISHED")
        return true
    }


    fun generateTableService(
        apiBlueprint: ApiBlueprint,
        tableMapping: ApiTableMapping,
        basePackageName: String
    ): String {
        val domainLowercase = tableMapping.domain.toLowerCase()
        val domainPackageName = "$basePackageName.$domainLowercase"

        val model = mapOf(
            Pair("COLUMN_MAPPING", tableMapping.columnMapping),
            Pair("PACKAGE_NAME", domainPackageName),
            Pair("DOMAIN_NAME", tableMapping.domain),
            Pair("DOMAIN_BASEURL", domainLowercase),
            Pair("TABLE_NAME", tableMapping.table.toUpperCase()),
            Pair("RECORD_NAME", tableMapping.record),
            Pair("apiBlueprint", apiBlueprint),
            Pair("tableMapping", tableMapping)
        )
        val handlebars = Handlebars()
        val template = handlebars.compileInline(serviceTemplate)
        return template.apply(model)
    }
}
