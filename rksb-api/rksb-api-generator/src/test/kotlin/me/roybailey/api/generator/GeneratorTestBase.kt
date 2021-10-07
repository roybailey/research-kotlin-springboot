package me.roybailey.api.generator

import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.blueprint.BlueprintProperties
import me.roybailey.api.generator.configuration.GeneratorConfiguration
import me.roybailey.api.generator.configuration.GeneratorProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles


@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = ["me.roybailey.api.blueprint","me.roybailey.api.generator.configuration","me.roybailey.api.generator.service"])
open class TestGeneratorSpringApplication


@SpringBootTest(classes = [TestGeneratorSpringApplication::class])
@ActiveProfiles("test", "blueprint")
open class GeneratorTestBase {

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var generatorConfiguration: GeneratorConfiguration

}
