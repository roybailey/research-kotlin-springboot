package me.roybailey.api.blueprint

import me.roybailey.common.util.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource


@Configuration
@ConfigurationProperties
@PropertySource(value = ["classpath:application-blueprint.yml"], factory = YamlPropertySourceFactory::class)
open class BlueprintProperties {

    //@Value("\${blueprint-templates}")
    lateinit var blueprintTemplates: List<String>

    @Value("\${blueprint.base-package}")
    lateinit var blueprintBasePackage: String

    @Value("\${blueprint.collection:blueprint-collection.json}")
    lateinit var blueprintCollectionFilename: String

    @Value("\${blueprint.datasource.url:}")
    lateinit var blueprintDatabaseUrl: String

    @Value("\${blueprint.datasource.driver:org.postgresql.Driver}")
    lateinit var blueprintDatabaseDriver: String

    @Value("\${blueprint.datasource.username:}")
    lateinit var blueprintDatabaseUsername: String

    @Value("\${blueprint.datasource.password:}")
    lateinit var blueprintDatabasePassword: String

    @Value("\${blueprint.datasource.schema:}")
    lateinit var blueprintDatabaseSchema: String

    @Value("\${blueprint.datasource.excludes}")
    lateinit var blueprintDatabaseExcludes: String

}
