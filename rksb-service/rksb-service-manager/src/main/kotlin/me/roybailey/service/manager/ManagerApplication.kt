package me.roybailey.service.manager

import me.roybailey.api.blueprint.ApiBlueprintConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(
// we dont want any of the spring auto datasource as we use the properties directly with jooq only
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JooqAutoConfiguration::class,
        R2dbcAutoConfiguration::class,
        FlywayAutoConfiguration::class
    ],
    scanBasePackageClasses = [ApiBlueprintConfiguration::class, ManagerConfiguration::class],
    scanBasePackages = ["me.roybailey.api", "me.roybailey.codegen"]
)
open class ManagerApplication

fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}
