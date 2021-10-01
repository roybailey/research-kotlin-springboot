package me.roybailey.api.blueprint

data class BlueprintCollection(
    var targetFolder: String = "./target/generated-source",
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
    var source: String?, // will be the blueprint file
    var namespace: String,
    var packageName: String?, // default to {{../packageName}}
    var controllers: List<ControllerMapping> = emptyList(),
    var services: List<ServiceMapping> = emptyList(),
    var tables: List<TableMapping> = emptyList(),
    var models: List<ModelMapping> = emptyList(),
)

data class ControllerMapping(
    var id: String,
    var namespace: String?,      // defaults to {{../namespace}}
    var packageName: String?,    // default to {{../packageName}}.api.{{namespace}}
    var className: String?,      // defaults to {{namespace}}Controller
    var variableName: String?,   // defaults to {{namespace}}Controller
    var serviceMappingId: String,
    var apiPath: String?,
    var endpoints: List<EndpointMapping>
)

data class EndpointMapping(
    var apiPath: String,
    var apiRequestParameters: Map<String, Array<String>> = emptyMap(),
    var apiMethodName: String = "getAllData",
    var serviceMethodName: String?,  // defaults to {{apiMethodName}}
)

data class ServiceMapping(
    var id: String,
    var namespace: String?,      // defaults to {{../namespace}}
    var packageName: String?,    // default to {{../packageName}}.api.{{namespace}}
    var className: String?,      // defaults to {{namespace}}Service
    var variableName: String?,   // defaults to {{namespace}}Service
    var tableMappingId: String,
    var modelMappingId: String,
)

data class TableMapping(
    var id: String,
    var namespace: String?,      // defaults to {{../namespace}}
    var packageName: String?,    // default to {{../packageName}}.api.{{namespace}}
    var className: String?,      // defaults to {{namespace}}Table
    var tableName: String,
    var columns: List<ColumnMapping> = emptyList(),
    var filters: List<FilterMapping> = emptyList(),
)

data class ColumnMapping(
    var column: String,
    var ordinalPosition: Int = 0,
    var databaseType: String? = null,
    var type: String? = null
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
    var namespace: String?,   // defaults to {{../namespace}}
    var packageName: String?, // default to {{../packageName}}.api.{{namespace}}
    var className: String?,   // defaults to {{namespace}}Model
    var fields: List<FieldMapping>,
)

data class FieldMapping(
    var fieldName: String,
    var fieldType: String,
    var jsonName: String? = null,
)
