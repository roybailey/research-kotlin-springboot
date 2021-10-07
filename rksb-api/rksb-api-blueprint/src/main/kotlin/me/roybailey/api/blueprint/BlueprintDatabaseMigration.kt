package me.roybailey.api.blueprint

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml
import java.util.concurrent.Callable
import javax.annotation.PostConstruct


@Component("blueprints-database-migration")
@ConditionalOnProperty(value=["blueprints.flyway.enabled"], havingValue = "true", matchIfMissing = false)
class BlueprintDatabaseMigration {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @PostConstruct
    fun init() {
        logger.info("############################################################")
        val flyway = Flyway
            .configure()
            .baselineOnMigrate(true)
            .baselineVersion("0001")
            .locations("classpath:ddl")
            .dataSource(
                blueprintProperties.blueprintsDatabaseUrl,
                blueprintProperties.blueprintsDatabaseUsername,
                blueprintProperties.blueprintsDatabasePassword
            )
            .table("blueprints-schema-history")
            .load()
        val migration = flyway.migrate()
        logger.info("FlyWay Database Migration ${migration.initialSchemaVersion} ${migration.targetSchemaVersion}")
    }

}
