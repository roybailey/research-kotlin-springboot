package me.roybailey.api.generator

import me.roybailey.api.blueprint.*
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import java.lang.Thread.sleep
import kotlin.system.exitProcess


@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = ["me.roybailey.api.blueprint","me.roybailey.api.generator"])
open class TestGeneratorSpringApplication


@SpringBootTest(classes = [TestGeneratorSpringApplication::class])
@ActiveProfiles("test", "blueprint")
open class BlueprintTestBase {

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintConfiguration: BlueprintConfiguration

}
