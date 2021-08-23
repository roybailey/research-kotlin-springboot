package me.roybailey.generator

data class ApiSpecification (
    var name: String,
    var tableMapping: List<ApiTableMapping>
)

data class ApiTableMapping (
    var table: String,
    var record: String,
    var domain: String,
    var createSql: String?,
    var columnMapping: List<ApiColumnMapping>,
    var testData: List<ApiTestData>
)

data class ApiColumnMapping (
    var column: String,
    var databaseType: String? = null,
    var type: String? = null,
    var testDataStrategy: String = "sequence",
)

data class ApiTestData (
    var count: Integer
)
