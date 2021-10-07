package me.roybailey.api.generator.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties
open class GeneratorProperties {

    @Value("\${project.basedir:./target}")
    lateinit var basedir: String

    @Value("\${project.version:0.0.0-SNAPSHOT}")
    lateinit var version: String

    @Value("\${project.target:/src/main/kotlin}")
    lateinit var target: String

    @Value("\${codegen.write-files.enabled:true}")
    var codegenWriteFilesEnabled: Boolean = true

    @Value("\${codegen.blueprint-collection.target}")
    lateinit var codegenBlueprintCollection: String

    lateinit var columnTypeMappings: List<String>

    fun getColumnTypeMappings(): Map<String, String> = columnTypeMappings.map {
        val pair = it.split(",")
        pair[0] to pair[1]
    }.toMap()

    fun getColumnType(dataType: String?): String {
        if (dataType == null) {
            throw IllegalArgumentException("NULL database type found when mapping to column-type-mapping properties")
        }
        val columnTypeMapping =
            getColumnTypeMappings().filter { dataType.contains(Regex(it.value, RegexOption.IGNORE_CASE)) }.keys
        if (columnTypeMapping.isEmpty()) {
            throw IllegalArgumentException("Unknown database type [$dataType] found.  Add the matching Regex entry to the column-type-mapping properties")
        }
        if (columnTypeMapping.size > 1) {
            throw IllegalArgumentException("Ambiguous database type $dataType found ($columnTypeMapping).  Consider more restrictive matching Regex in the column-type-mapping properties")
        }
        return columnTypeMapping.first()
    }

    lateinit var fieldTypeMappings: List<String>

    fun getFieldTypeMappings(): Map<String, String> = fieldTypeMappings.map {
        val pair = it.split(",")
        pair[0] to pair[1]
    }.toMap()

    fun getFieldType(columnType: String?): String {
        if (columnType == null) {
            throw IllegalArgumentException("[NULL] column type when mapping to field-type-mapping properties")
        }
        val fieldTypeMapping =
            getFieldTypeMappings().filter { columnType.contains(Regex(it.value, RegexOption.IGNORE_CASE)) }.keys
        if (fieldTypeMapping.isEmpty()) {
            throw IllegalArgumentException("Unknown column type [$columnType]. Add the matching Regex entry to the field-type-mapping properties")
        }
        if (fieldTypeMapping.size > 1) {
            throw IllegalArgumentException("Ambiguous column type [$columnType] ($fieldTypeMapping).  Consider more restrictive matching Regex in the field-type-mapping properties")
        }
        return fieldTypeMapping.first()
    }
}
