package me.roybailey.generator

data class ApiSpecification (
    val tableMapping: List<ApiTableMapping>
)

data class ApiTableMapping (
    val table: String,
    val record: String,
    val domain: String,
    val columnMapping: List<ApiColumnMapping>,
    val testData: List<ApiTestData>
)

data class ApiColumnMapping (
    val column: String,
    val type: String,
    val testDataStrategy: String = "sequence",
)

data class ApiTestData (
    val count: Integer
)
