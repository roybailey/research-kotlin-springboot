package me.roybailey.api.blueprint

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.jooq.impl.DSL.using
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.IllegalArgumentException
import java.util.stream.Collectors


@Configuration
@ConditionalOnProperty(value = ["api.blueprints.enabled"], havingValue = "true", matchIfMissing = true)
open class ApiBlueprintConfiguration {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Bean
    open fun apiBlueprints(): List<ApiBlueprint> {

        val apiBlueprintFiles = apiBlueprintProperties.blueprints
        logger.info("Loaded api.blueprints as $apiBlueprintFiles")

        if (apiBlueprintFiles.isEmpty()) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            logger.error("!!!!! NO API BLUEPRINTS FOUND !!!!!")
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }

        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val mapApiColumnMapping = loadTableColumns()

        val apiBlueprints = apiBlueprintFiles.stream().map { apiBlueprintFile ->
            logger.info { apiBlueprintFile }
            val apiBlueprint = mapper.readValue(
                this.javaClass.classLoader.getResourceAsStream(apiBlueprintFile),
                ApiBlueprint::class.java
            )
            logger.info { apiBlueprint }

            apiBlueprint.tableMapping
                .forEach { tableMapping ->
                    // lowercase all database names as this is postgres standard
                    tableMapping.table = tableMapping.table.toLowerCase()
                    tableMapping.columnMapping.stream()
                        .forEach { columnMapping -> columnMapping.column = columnMapping.column.toLowerCase() }
                    tableMapping.filterMapping.stream()
                        .forEach { filterMapping -> filterMapping.column = filterMapping.column.toLowerCase() }

                    // map the columns by name
                    val columnMap = tableMapping.columnMapping.associateByTo(mutableMapOf(), { it.column }, { it })

                    // load column information from database schema
                    if (mapApiColumnMapping.containsKey(tableMapping.table)) {
                        tableMapping.columnMapping =
                            mergeColumnMappings(mapApiColumnMapping[tableMapping.table]!!, columnMap)
                    }
                }

            apiBlueprint
        }.collect(Collectors.toList())
        return apiBlueprints
    }


    /**
     * Loads column type information into ApiColumnMapping objects directly from database schema
     */
    private fun loadTableColumns(): Map<String, List<ApiColumnMapping>> {
        logger.info("loading columns from ${apiBlueprintProperties.blueprintsDatabaseUrl} for schema=${apiBlueprintProperties.blueprintsDatabaseSchema}")
        val jooq = using(
            apiBlueprintProperties.blueprintsDatabaseUrl,
            apiBlueprintProperties.blueprintsDatabaseUsername,
            apiBlueprintProperties.blueprintsDatabasePassword
        )
        val mapApiColumnMapping = mutableMapOf<String, List<ApiColumnMapping>>()
        val mapColumnTypeMappings = apiBlueprintProperties.getColumnTypeMappings()

        // SELECT * FROM information_schema.columns WHERE table_schema = 'public' ORDER BY table_name;
        //
        // Available Columns:
        // table_catalog, table_schema, table_name, column_name, ordinal_position, column_default, is_nullable,
        // data_type, character_maximum_length, character_octet_length, numeric_precision, numeric_precision_radix,
        // numeric_scale, datetime_precision, interval_type, interval_precision, character_set_catalog,
        // character_set_schema, character_set_name, collation_catalog, collation_schema, collation_name,
        // domain_catalog, domain_schema, domain_name, udt_catalog, udt_schema, udt_name, scope_catalog, scope_schema,
        // scope_name, maximum_cardinality, dtd_identifier, is_self_referencing, is_identity, identity_generation,
        // identity_start, identity_increment, identity_maximum, identity_minimum, identity_cycle, is_generated,
        // generation_expression, is_updatable
        val fetch = jooq.select()
            .from("information_schema.columns")
            .where("table_schema='${apiBlueprintProperties.blueprintsDatabaseSchema}'")
            .fetch()

        logger.debug(fetch.formatCSV())

        fetch.forEach { record ->
            val tableName: String = record["table_name"] as String
            val tableSchema: String = record["table_schema"] as String
            val columnName: String = record["column_name"] as String
            val dataType: String = record["data_type"] as String
            if (!mapApiColumnMapping.containsKey(tableName))
                mapApiColumnMapping[tableName] = listOf()
            val typeMapping =
                mapColumnTypeMappings.filter { dataType.contains(Regex(it.value, RegexOption.IGNORE_CASE)) }.keys
            if (typeMapping.isEmpty()) {
                throw IllegalArgumentException("Unknown database type [$dataType] found.  Add the matching Regex entry to the properties file")
            }
            if (typeMapping.size > 1) {
                logger.warn("Ambiguous database type $dataType found ($typeMapping).  Consider more restrictive matching Regex in the properties file")
            }
            mapApiColumnMapping[tableName] = mapApiColumnMapping[tableName]!!.plus(
                ApiColumnMapping(
                    column = columnName,
                    databaseType = dataType,
                    type = typeMapping.first()
                )
            )
        }

        logger.info(mapApiColumnMapping.toString())
        return mapApiColumnMapping
    }


    /**
     * merges ApiColumnMappings, used to merge static blueprint information with database column analysis
     */
    private fun mergeColumnMappings(
        databaseColumns: List<ApiColumnMapping>,
        columnMap: MutableMap<String, ApiColumnMapping>
    ): List<ApiColumnMapping> = databaseColumns.map { databaseColumn ->
        val specColumn = columnMap[databaseColumn.column]
        logger.info("Column parsed as [$databaseColumn]")
        logger.info("Column spec'd as [$specColumn]")
        val apiColumnMapping = specColumn ?: databaseColumn
        apiColumnMapping.databaseType = databaseColumn.databaseType
        if (apiColumnMapping.type == null)
            apiColumnMapping.type = databaseColumn.databaseType
        logger.info("Column merged as [$apiColumnMapping]")
        apiColumnMapping
    }

}
