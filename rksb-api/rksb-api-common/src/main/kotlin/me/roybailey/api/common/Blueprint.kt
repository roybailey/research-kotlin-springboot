package me.roybailey.api.common

data class BlueprintCollection(
    var packageName: String,
    var blueprints: List<Blueprint> = emptyList()
) {

    fun allControllers() = blueprints
        .map { blueprint -> blueprint.controllers }
        .toList()
        .flatten()

    fun allServices() = blueprints
        .map { blueprint -> blueprint.services }
        .toList()
        .flatten()

    fun allTables() = blueprints
        .map { blueprint -> blueprint.tables }
        .toList()
        .flatten()

    fun allModels() = blueprints
        .map { blueprint -> blueprint.models }
        .toList()
        .flatten()
}

data class Blueprint(
    var id: String,
    var source: String,
    var namespace: String,
    var packageName: String,
    var controllers: List<ControllerMapping> = emptyList(),
    var services: List<ServiceMapping> = emptyList(),
    var tables: List<TableMapping> = emptyList(),
    var models: List<ModelMapping> = emptyList(),
)

data class ControllerMapping(
    var id: String,
    var namespace: String,
    var packageName: String,
    var className: String,
    var variableName: String,
    var serviceMappingId: String,
    var apiPath: String,
    var endpoints: List<EndpointMapping>
)

data class EndpointMapping(
    var apiPath: String,
    var apiRequestParameters: Map<String, Array<String>> = emptyMap(),
    var apiMethodName: String = "getAllData",
    var serviceMethodName: String,
)

data class ServiceMapping(
    var id: String,
    var namespace: String,
    var packageName: String,
    var className: String,
    var variableName: String,
    var tableMappingId: String,
    var modelMappingId: String,
)

data class TableMapping(
    var id: String,
    var namespace: String,
    var packageName: String,
    var className: String,
    var tableName: String,
    var columns: List<ColumnMapping> = emptyList(),
    var filters: List<FilterMapping> = emptyList(),
)

data class ColumnMapping(
    var column: String,
    var ordinalPosition: Int = 0,
    var databaseType: String,
    var type: String
)

enum class FilterType {
    EQUAL, LIKE, BETWEEN
}

data class FilterMapping(
    var type: FilterType,
    var name: String,
    var column: String,
    var params: Map<String, Any> = emptyMap()
)

data class ModelMapping(
    var id: String,
    var namespace: String,
    var packageName: String,
    var className: String,
    var fields: List<FieldMapping>,
)

data class FieldMapping(
    var fieldName: String,
    var fieldType: String,
    var jsonName: String,
)
