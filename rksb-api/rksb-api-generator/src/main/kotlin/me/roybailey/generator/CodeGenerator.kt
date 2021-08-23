package me.roybailey.generator

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class CodeGenerator {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var properties: ConfigurationProperties

    @Autowired
    lateinit var apiSpecifications: List<ApiSpecification>

    @Autowired
    lateinit var serviceCodeGenerator: ServiceCodeGenerator

    @Autowired
    lateinit var controllerCodeGenerator: ControllerCodeGenerator


    @PostConstruct
    fun generateControllerCode() {

        logger.info("Code Generation - STARTING")
        val basedir = properties.basedir
        val target = properties.target
        val basePackageName = "${properties.basePackageName}.api"
        val basePackageDirectory = "$basedir/$target/${basePackageName.replace(".", "/")}"
        val tableMappings = apiSpecifications
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
