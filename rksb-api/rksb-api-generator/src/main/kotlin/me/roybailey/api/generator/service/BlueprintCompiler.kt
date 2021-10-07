package me.roybailey.api.generator.service

import com.fasterxml.jackson.databind.ObjectMapper
import me.roybailey.api.blueprint.*
import me.roybailey.api.generator.configuration.GeneratorProperties
import mu.KotlinLogging
import org.jooq.impl.DSL.using
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.stream.Collectors


@Component
open class BlueprintCompiler {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var generatorProperties: GeneratorProperties

    @Autowired
    lateinit var jsonMapper: ObjectMapper


    fun compileBlueprintsTemplates(): BlueprintCollection {

        val blueprintCollection = BlueprintCollection(packageName = blueprintProperties.blueprintsBasePackage)
        val blueprintFiles = blueprintProperties.blueprintsTemplates
        logger.info("Loaded api.blueprints as $blueprintFiles")

        if (blueprintFiles.isEmpty()) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            logger.error("!!!!! NO BLUEPRINTS FOUND !!!!!")
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }

        blueprintCollection.blueprints = blueprintFiles.stream().map { blueprintFile ->
            logger.info { blueprintFile }
            val blueprint = jsonMapper.readValue(
                this.javaClass.classLoader.getResourceAsStream(blueprintFile),
                Blueprint::class.java
            )
            logger.info { blueprint }
            blueprint.source = blueprintFile
            blueprint
        }.collect(Collectors.toList())

        val mapApiColumnMapping =
            loadTableColumns(blueprintCollection.allTables().map { it.tableName.toLowerCase() }.toSet())

        blueprintCollection.blueprints.forEach { blueprint ->
            // resolve derived properties (e.g. packageName)
            logger.info("Resolving blueprint ${blueprint.id}")
            blueprint.packageName = blueprint.packageName ?: "${blueprintCollection.packageName}"

            // resolve derived properties (e.g. namespace, packageName, className)
            blueprint.controllers = blueprint.controllers.map { controllerMapping ->
                logger.info("Resolving controllerMapping ${controllerMapping.id}")
                controllerMapping.namespace = controllerMapping.namespace ?: blueprint.namespace
                controllerMapping.packageName = controllerMapping.packageName
                    ?: "${blueprint.packageName}.api.${controllerMapping.namespace!!.toLowerCase()}"
                controllerMapping.className = controllerMapping.className ?: "${blueprint.namespace}Controller"
                controllerMapping.variableName = controllerMapping.variableName
                    ?: controllerMapping.className!!.decapitalize()
                controllerMapping.endpoints = controllerMapping.endpoints.map { endpointMapping ->
                    logger.info("Resolving endpointMapping ${endpointMapping.apiPath}")
                    endpointMapping.serviceMethodName =
                        endpointMapping.serviceMethodName ?: endpointMapping.apiMethodName
                    endpointMapping
                }
                controllerMapping
            }

            // resolve derived properties (e.g. namespace, packageName, className)
            blueprint.services = blueprint.services.map { serviceMapping ->
                logger.info("Resolving serviceMapping ${serviceMapping.id}")
                serviceMapping.namespace = serviceMapping.namespace ?: blueprint.namespace
                serviceMapping.packageName = serviceMapping.packageName
                    ?: "${blueprint.packageName}.api.${serviceMapping.namespace!!.toLowerCase()}"
                serviceMapping.className = serviceMapping.className ?: "${blueprint.namespace}Service"
                serviceMapping.variableName =
                    serviceMapping.variableName ?: serviceMapping.className!!.decapitalize()
                serviceMapping
            }

            // resolve derived properties (e.g. namespace, packageName, className)
            blueprint.tables.forEach { tableMapping ->
                logger.info("Resolving tableMapping ${tableMapping.id}")
                tableMapping.namespace = tableMapping.namespace ?: blueprint.namespace
                tableMapping.packageName =
                    tableMapping.packageName ?: "${blueprint.packageName}.api.${tableMapping.namespace!!.toLowerCase()}"
                tableMapping.className = tableMapping.className ?: "${blueprint.namespace}Table"
                // lowercase all database names as this is postgres standard
                tableMapping.tableName = tableMapping.tableName.toLowerCase()
                tableMapping.columns.stream()
                    .forEach { columnMapping -> columnMapping.column = columnMapping.column.toLowerCase() }
                tableMapping.filters.stream()
                    .forEach { filterMapping -> filterMapping.column = filterMapping.column.toLowerCase() }

                // map the columns by name
                val columnMap = tableMapping.columns.associateByTo(mutableMapOf(), { it.column }, { it })

                // load column information from database schema
                if (mapApiColumnMapping.containsKey(tableMapping.tableName)) {
                    tableMapping.columns =
                        mergeColumnMappings(mapApiColumnMapping[tableMapping.tableName]!!, columnMap)
                }
            }

            // resolve derived properties (e.g. namespace, packageName, className)
            blueprint.models = blueprint.models.map { modelMapping ->
                logger.info("Resolving modelMapping ${modelMapping.id}")
                modelMapping.namespace = modelMapping.namespace ?: blueprint.namespace
                modelMapping.packageName =
                    modelMapping.packageName ?: "${blueprint.packageName}.api.${modelMapping.namespace!!.toLowerCase()}"
                modelMapping.className = modelMapping.className ?: "${blueprint.namespace}Model"

                val serviceMapping = blueprintCollection.allServices().find { it.modelMappingId == modelMapping.id }!!
                val tableMapping = blueprintCollection.allTables().find { it.id == serviceMapping.tableMappingId }!!
                tableMapping.columns.forEach { columnMapping ->
                    val field = modelMapping.fields.find { it.fieldName == columnMapping.column }
                    if (field == null)
                        modelMapping.fields = modelMapping.fields.plus(
                            FieldMapping(
                                fieldName = columnMapping.column,
                                fieldType = generatorProperties.getFieldType(columnMapping.type),
                                jsonName = columnMapping.column
                            )
                        )
                }
                modelMapping
            }
        }

        return blueprintCollection
    }


    /**
     * Loads column type information into ApiColumnMapping objects directly from database schema
     */
    private fun loadTableColumns(tableNames: Set<String>): Map<String, List<ColumnMapping>> {
        logger.info("loading columns from ${blueprintProperties.blueprintsDatabaseUrl} for schema=${blueprintProperties.blueprintsDatabaseSchema}")
        val jooq = using(
            blueprintProperties.blueprintsDatabaseUrl,
            blueprintProperties.blueprintsDatabaseUsername,
            blueprintProperties.blueprintsDatabasePassword
        )
        val mapApiColumnMapping = mutableMapOf<String, List<ColumnMapping>>()
        val mapColumnTypeMappings = generatorProperties.getColumnTypeMappings()

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
            .where("table_schema='${blueprintProperties.blueprintsDatabaseSchema}'")
            .fetch()

        fetch.forEach { record ->
            val tableName: String = record["table_name"] as String
            val tableSchema: String = record["table_schema"] as String
            val columnName: String = record["column_name"] as String
            val dataType: String = record["data_type"] as String
            val ordinalPosition: Int = record["ordinal_position"] as Int

            if (tableNames.contains(tableName)) {
                if (!mapApiColumnMapping.containsKey(tableName)) {
                    mapApiColumnMapping[tableName] = listOf()
                }
                mapApiColumnMapping[tableName] = mapApiColumnMapping[tableName]!!.plus(
                    ColumnMapping(
                        column = columnName,
                        databaseType = dataType,
                        type = generatorProperties.getColumnType(dataType),
                        ordinalPosition = ordinalPosition
                    )
                )
                logger.info(mapApiColumnMapping[tableName].toString())
            }
        }
        // ensures consistent ordering of column array, to help test comparisons
        mapApiColumnMapping.keys.forEach { key ->
            mapApiColumnMapping[key] = mapApiColumnMapping[key]!!.sortedBy { it.ordinalPosition }
        }
        return mapApiColumnMapping
    }


    /**
     * merges ApiColumnMappings, used to merge static blueprint information with database column analysis
     */
    private fun mergeColumnMappings(
        databaseColumns: List<ColumnMapping>,
        columnMap: MutableMap<String, ColumnMapping>
    ): List<ColumnMapping> = databaseColumns.map { databaseColumn ->
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
