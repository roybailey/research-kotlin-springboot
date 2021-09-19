package me.roybailey.api.blueprint

data class ApiBlueprint (
    var id: String,
    var tableMapping: List<ApiTableMapping>,
    var apiMapping: List<ApiMapping>? = emptyList()
)

data class ApiMapping (
    var id: String,
    var namespace: String,
    var tableMappingId: String,
    var apiPath: String,
    var apiRequestParameters: Map<String,String> = emptyMap()
)

data class ApiTableMapping (
    var id: String,
    var table: String,
    var record: String,
    var domain: String,
    var createSql: String? = null,
    var columnMapping: List<ApiColumnMapping>,
    var filterMapping: List<ApiFilterMapping> = emptyList(),
    var testData: List<ApiTestData>
)

data class ApiColumnMapping (
    var column: String,
    var databaseType: String? = null,
    var type: String? = null,
    var testDataStrategy: String = "sequence",
)

enum class ApiFilterType {
    EQUAL, LIKE, BETWEEN
}

data class ApiFilterMapping (
    var type: ApiFilterType,
    var name: String,
    var column: String,
    var params: Map<String,Any> = emptyMap()
)

data class ApiTestData (
    var count: Integer
)
