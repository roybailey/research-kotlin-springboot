package me.roybailey.api.generator.configuration

import me.roybailey.api.blueprint.ColumnType
import me.roybailey.api.blueprint.FieldType
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


    fun getColumnType(dataType: String?): ColumnType {
        if (dataType == null) {
            throw IllegalArgumentException("NULL database type found when mapping to column-type-mapping properties")
        }
        val columnTypeMapping =
            ColumnType.values().filter { dataType.contains(Regex(it.databaseRegex, RegexOption.IGNORE_CASE)) }
        if (columnTypeMapping.isEmpty()) {
            throw IllegalArgumentException("Unknown database type [$dataType] found.  Add the matching Regex entry to the column-type-mapping properties")
        }
        if (columnTypeMapping.size > 1) {
            throw IllegalArgumentException("Ambiguous database type $dataType found ($columnTypeMapping).  Consider more restrictive matching Regex in the column-type-mapping properties")
        }
        return columnTypeMapping.first()
    }


    fun getFieldType(columnType: ColumnType?): FieldType {
        if (columnType == null) {
            throw IllegalArgumentException("[NULL] column type when mapping to field-type-mapping properties")
        }
        val fieldTypeMapping =
            FieldType.values().filter { fieldType ->
                columnType.toString().contains(Regex(fieldType.columnTypeRegex, RegexOption.IGNORE_CASE))
            }
        if (fieldTypeMapping.isEmpty()) {
            throw IllegalArgumentException("Unknown column type [$columnType]. Add the matching Regex entry to the field-type-mapping properties")
        }
        if (fieldTypeMapping.size > 1) {
            throw IllegalArgumentException("Ambiguous column type [$columnType] ($fieldTypeMapping).  Consider more restrictive matching Regex in the field-type-mapping properties")
        }
        return fieldTypeMapping.first()
    }
}
