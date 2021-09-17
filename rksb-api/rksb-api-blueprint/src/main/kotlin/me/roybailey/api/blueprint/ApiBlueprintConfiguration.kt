package me.roybailey.api.blueprint

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.jooq.impl.DSL.using
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.stream.Collectors


@Configuration
open class ApiBlueprintConfiguration {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var properties: ApiBlueprintProperties

    @Bean
    open fun apiBlueprints(): List<ApiBlueprint> {

        val apiBlueprintFiles = properties.blueprints
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

                    // load column information from static create sql script (deprecated)
                    if (tableMapping.createSql != null) {
                        val databaseColumns = parseDatabaseColumns(tableMapping, columnMap, tableMapping.createSql!!)
                        tableMapping.columnMapping = mergeColumnMappings(databaseColumns, columnMap)
                    }

                    // load column information from database schema
                    if (mapApiColumnMapping.containsKey(tableMapping.table)) {
                        tableMapping.columnMapping = mergeColumnMappings(mapApiColumnMapping[tableMapping.table]!!, columnMap)
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
        logger.info("loading columns from ${properties.blueprintsDatabaseUrl} for schema=${properties.blueprintsDatabaseSchema}")
        val jooq = using(properties.blueprintsDatabaseUrl, properties.blueprintsDatabaseUsername, properties.blueprintsDatabasePassword)
        val mapApiColumnMapping = mutableMapOf<String, List<ApiColumnMapping>>()

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
            .where("table_schema='${properties.blueprintsDatabaseSchema}'")
            .fetch()

        logger.info(fetch.formatCSV())

        fetch.forEach { record ->
            val tableName: String = record["table_name"] as String
            val tableSchema: String = record["table_schema"] as String
            val columnName: String = record["column_name"] as String
            val dataType: String = record["data_type"] as String
            if (!mapApiColumnMapping.containsKey(tableName))
                mapApiColumnMapping[tableName] = listOf()
            mapApiColumnMapping[tableName] = mapApiColumnMapping[tableName]!!.plus(
                ApiColumnMapping(
                    column = columnName,
                    databaseType = dataType
                )
            )
        }

        logger.info(mapApiColumnMapping.toString())
        return mapApiColumnMapping
    }


    @Deprecated("replaced with database column loading directly from database")
    private fun parseDatabaseColumns(
        tableMapping: ApiTableMapping,
        columnMap: MutableMap<String, ApiColumnMapping>,
        createSql: String
    ): List<ApiColumnMapping> {
        val createSql =
            String(this.javaClass.classLoader.getResourceAsStream(tableMapping.createSql)!!.readAllBytes())

        logger.info("Parsing createSql\n$createSql")
        val parsedColumns = createSql
            .substring(createSql.indexOf('(') + 1, createSql.lastIndexOf(')'))
            .replace("\n", " ")
            .split(",")
            .map { column -> column.trim() }
            .map { column ->
                Pair(
                    column.substring(0, column.indexOf(' ')).trim().toUpperCase(),
                    column.substring(column.indexOf(' ')).trim().toUpperCase()
                )
            }
            .map { pair ->
                ApiColumnMapping(
                    pair.first,
                    pair.second,
                    when {
                        pair.second.contains(Regex("key", RegexOption.IGNORE_CASE)) -> "ID"
                        pair.second.contains(Regex("varchar|text", RegexOption.IGNORE_CASE)) -> "TEXT"
                        pair.second.contains(Regex("double", RegexOption.IGNORE_CASE)) -> "DOUBLE"
                        pair.second.contains(Regex("integer", RegexOption.IGNORE_CASE)) -> "INTEGER"
                        else -> pair.second.toUpperCase()
                    }
                )
            }
            .toList()
        return parsedColumns
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
