package me.roybailey.api.blueprint

import me.roybailey.common.util.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource


@Configuration
@ConfigurationProperties(prefix = "api")
@PropertySource(value = ["classpath:application-blueprints.yml"], factory = YamlPropertySourceFactory::class)
open class ApiBlueprintProperties {

    lateinit var blueprints: List<String>

    @Value("\${api.datasource.blueprints.url}")
    lateinit var blueprintsDatabaseUrl: String

    @Value("\${api.datasource.blueprints.driver:org.postgresql.Driver}")
    lateinit var blueprintsDatabaseDriver: String

    @Value("\${api.datasource.blueprints.username}")
    lateinit var blueprintsDatabaseUsername: String

    @Value("\${api.datasource.blueprints.password}")
    lateinit var blueprintsDatabasePassword: String

    @Value("\${api.datasource.blueprints.schema}")
    lateinit var blueprintsDatabaseSchema: String

    @Value("\${api.datasource.blueprints.excludes}")
    lateinit var blueprintsDatabaseExcludes: String

    @Value("\${api.codegen.base-package}")
    lateinit var codegenBasePackage: String

}
