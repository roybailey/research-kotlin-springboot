package me.roybailey.generator

data class ApiDefinition (
    val tableMapping: List<ApiTableMapping>
)

data class ApiTableMapping (
    val table: String,
    val record: String,
    val domain: String,
)

