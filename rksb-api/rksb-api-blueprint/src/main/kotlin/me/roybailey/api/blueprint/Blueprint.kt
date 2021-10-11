package me.roybailey.api.blueprint


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

// database column types are 'normalised' into an internal column type for easier handling
enum class ColumnType(val databaseRegex: String) {
    ID("key"),
    TEXT("varchar|text|character|uuid|xml|json"),
    DOUBLE("double|real|decimal|numeric"),
    INTEGER("integer|bigint|smallint|serial"),
    TIMESTAMP("timestamp|date|time|interval"),
    BOOLEAN("boolean");
}

data class ColumnMapping(
    var column: String,
    var ordinalPosition: Int = 0,
    var databaseType: String? = null,
    var type: ColumnType? = null
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

// field types are mapped from internal column types using a Regex match
enum class FieldType(val javaClass:String, val columnTypeRegex: String) {
    STRING("String","ID|TEXT|STRING"),
    DOUBLE("Double", "DOUBLE"),
    INTEGER("Integer", "INTEGER|NUMBER"),
    TIMESTAMP("java.sql.Timestamp", "TIMESTAMP"),
    BOOLEAN("Boolean", "BOOLEAN");
}

data class FieldMapping(
    var fieldName: String,
    var fieldType: FieldType,
    var jsonName: String? = null,
)
