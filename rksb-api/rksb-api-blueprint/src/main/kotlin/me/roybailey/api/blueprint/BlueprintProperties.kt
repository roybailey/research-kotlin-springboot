package me.roybailey.api.blueprint

import me.roybailey.common.util.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.lang.IllegalArgumentException


@Configuration
@ConfigurationProperties
@PropertySource(value = ["classpath:application-blueprints.yml"], factory = YamlPropertySourceFactory::class)
open class BlueprintProperties {

    lateinit var blueprints: List<String>

    @Value("\${codegen.datasource.blueprints.url}")
    lateinit var blueprintsDatabaseUrl: String

    @Value("\${codegen.datasource.blueprints.driver:org.postgresql.Driver}")
    lateinit var blueprintsDatabaseDriver: String

    @Value("\${codegen.datasource.blueprints.username}")
    lateinit var blueprintsDatabaseUsername: String

    @Value("\${codegen.datasource.blueprints.password}")
    lateinit var blueprintsDatabasePassword: String

    @Value("\${codegen.datasource.blueprints.schema}")
    lateinit var blueprintsDatabaseSchema: String

    @Value("\${codegen.datasource.blueprints.excludes}")
    lateinit var blueprintsDatabaseExcludes: String

    @Value("\${codegen.base-package}")
    lateinit var codegenBasePackage: String

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