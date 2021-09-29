package me.roybailey.api.generator

import me.roybailey.api.blueprint.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootApplication(
    scanBasePackageClasses = [BlueprintConfiguration::class],
    scanBasePackages = ["me.roybailey.api.generator.configuration","me.roybailey.api.generator.service"],
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JooqAutoConfiguration::class,
        R2dbcAutoConfiguration::class,
        FlywayAutoConfiguration::class
    ],
)
open class TestGeneratorSpringApplication


@SpringBootTest(classes = [TestGeneratorSpringApplication::class])
@ActiveProfiles("test")
open class BlueprintTestBase {

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintConfiguration: BlueprintConfiguration

}
