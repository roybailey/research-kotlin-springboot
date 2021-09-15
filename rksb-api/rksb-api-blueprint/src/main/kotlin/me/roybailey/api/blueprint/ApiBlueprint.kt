package me.roybailey.api.blueprint

data class ApiBlueprint (
    var name: String,
    var tableMapping: List<ApiTableMapping>
)

data class ApiTableMapping (
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
