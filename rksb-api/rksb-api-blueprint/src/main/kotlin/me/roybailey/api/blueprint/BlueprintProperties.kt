package me.roybailey.api.blueprint

import me.roybailey.common.util.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource


@Configuration
@ConfigurationProperties
@PropertySource(value = ["classpath:application-blueprints.yml"], factory = YamlPropertySourceFactory::class)
open class BlueprintProperties {

    //@Value("\${blueprints-templates}")
    lateinit var blueprintsTemplates: List<String>

    @Value("\${blueprints.base-package}")
    lateinit var blueprintsBasePackage: String

    @Value("\${blueprints.collection:blueprint-collection.json}")
    lateinit var blueprintsCollectionFilename: String

    @Value("\${blueprints.datasource.url:}")
    lateinit var blueprintsDatabaseUrl: String

    @Value("\${blueprints.datasource.driver:org.postgresql.Driver}")
    lateinit var blueprintsDatabaseDriver: String

    @Value("\${blueprints.datasource.username:}")
    lateinit var blueprintsDatabaseUsername: String

    @Value("\${blueprints.datasource.password:}")
    lateinit var blueprintsDatabasePassword: String

    @Value("\${blueprints.datasource.schema:}")
    lateinit var blueprintsDatabaseSchema: String

    @Value("\${blueprints.datasource.excludes}")
    lateinit var blueprintsDatabaseExcludes: String

}
