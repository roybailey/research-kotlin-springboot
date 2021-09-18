package me.roybailey.api.blueprint

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.yaml.snakeyaml.Yaml
import java.util.concurrent.Callable


class FlywayMigration(
    var url: String? = null,
    var username: String? = null,
    var password: String? = null
) : Callable<Boolean> {

    private val logger = KotlinLogging.logger {}

    init {
        if (url == null) {
            val propertiesFile = "application-blueprints.yml"
            val yaml = Yaml()
            val inputStream = this.javaClass.classLoader.getResourceAsStream(propertiesFile)
            val properties: Map<String, Any> = yaml.load(inputStream)
            logger.info("Flyway Migration Loaded $propertiesFile")
            properties.forEach { logger.info("$it") }
            logger.info("Flyway Migration Parsing DataSource Properties api.datasource.blueprints")
            val datasourceProperties = ((properties["api"] as Map<*, *>)["datasource"] as Map<*,*>)["blueprints"] as Map<*,*>
            datasourceProperties.forEach { logger.info("$it") }
            url = datasourceProperties["url"]?.toString()
            username = datasourceProperties["username"]?.toString()
            password = datasourceProperties["password"]?.toString()
        }
    }


    override fun call(): Boolean {
        logger.info("############################################################")
        val flyway = Flyway
            .configure()
            .baselineOnMigrate(true)
            .baselineVersion("0001")
            .locations("classpath:ddl")
            .dataSource(url, username, password)
            .load()
        val migration = flyway.migrate()
        logger.info("FlyWay Database Migration ${migration.initialSchemaVersion} ${migration.targetSchemaVersion}")
        return true
    }

}
