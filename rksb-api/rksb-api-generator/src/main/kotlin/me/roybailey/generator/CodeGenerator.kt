package me.roybailey.generator

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintProperties
import me.roybailey.generator.code.ControllerCodeGenerator
import me.roybailey.generator.code.ServiceCodeGenerator
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class CodeGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    @Autowired
    lateinit var serviceCodeGenerator: ServiceCodeGenerator

    @Autowired
    lateinit var controllerCodeGenerator: ControllerCodeGenerator


    @PostConstruct
    fun generateControllerCode() {

        logger.info("Code Generation - STARTING")
        val basedir = generatorProperties.basedir
        val target = generatorProperties.target
        val basePackageName = "${apiBlueprintProperties.codegenBasePackage}.api"
        val basePackageDirectory = "$basedir/$target/${basePackageName.replace(".", "/")}"
        val tableMappings = apiBlueprints
            .map { apiDefinition -> apiDefinition.tableMapping }
            .toList()
            .flatten()

        logger.info("basedir=$basedir")
        logger.info("target=$target")
        logger.info("basePackageName=$basePackageName")
        logger.info("basePackageDirectory=$basePackageDirectory")
        logger.info("tableMappings=$tableMappings")

        File(basePackageDirectory).deleteRecursively()
        serviceCodeGenerator.generate()
        controllerCodeGenerator.generate()

        logger.info("Code Generation - FINISHED")
    }
}
