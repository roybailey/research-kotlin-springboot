package me.roybailey.api.blueprint

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component("blueprint-database-migration")
@ConditionalOnProperty(value=["blueprint.flyway.enabled"], havingValue = "true", matchIfMissing = false)
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
                blueprintProperties.blueprintDatabaseUrl,
                blueprintProperties.blueprintDatabaseUsername,
                blueprintProperties.blueprintDatabasePassword
            )
            .table("blueprint-schema-history")
            .load()
        val migration = flyway.migrate()
        logger.info("FlyWay Database Migration ${migration.initialSchemaVersion} ${migration.targetSchemaVersion}")
    }

}
