package me.roybailey.api.generator

import com.fasterxml.jackson.databind.ObjectMapper
import me.roybailey.api.blueprint.BlueprintCollection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

open class GeneratorConfigurationTest : GeneratorTestBase() {

    @Autowired
    lateinit var mapper: ObjectMapper


    fun assertPropertyHasValue(property:String) {
        assertThat(property).isNotNull
        assertThat(property.length).isGreaterThan(0)
        assertThat(property).doesNotContain("\${")
    }

    fun assertPropertyHasValue(property:List<String>) {
        assertThat(property).isNotNull
        assertThat(property.size).isGreaterThan(0)
        assertThat(property[0]).doesNotContain("\${")
    }

    @Test
    fun testGeneratorConfiguration() {

        assertPropertyHasValue(generatorProperties.basedir)
        assertPropertyHasValue(generatorProperties.target)
        assertPropertyHasValue(generatorProperties.codegenBlueprintCollection)
        assertPropertyHasValue(generatorProperties.columnTypeMappings)
        assertPropertyHasValue(generatorProperties.fieldTypeMappings)
    }


    @Test
    fun testBlueprints() {

        assertThat(blueprintCollection).isNotNull
        assertThat(blueprintCollection.packageName).isNotBlank
        assertThat(blueprintCollection.blueprints.size).isEqualTo(blueprintProperties.blueprintsTemplates.size)

        // use the first codegen-sample blueprint to verify the configuration loads and resolves as expected
        BlueprintCollection(
            packageName = blueprintCollection.packageName,
            blueprints = listOf(blueprintCollection.blueprints[0])
        ).run {
            val codegenSampleResolved = mapper.writeValueAsString(this)
            println(codegenSampleResolved)
            assertThat(codegenSampleResolved.replace("\r\n","\n")).isEqualTo(
                """
                {
                  "packageName" : "me.roybailey.codegen",
                  "blueprints" : [ {
                    "id" : "codegen-sample-blueprint",
                    "source" : "api/codegen-sample-blueprint.json",
                    "namespace" : "CodegenSample",
                    "packageName" : "me.roybailey.codegen",
                    "controllers" : [ {
                      "id" : "codegen-sample-controller",
                      "namespace" : "CodegenSample",
                      "packageName" : "me.roybailey.codegen.api.codegensample",
                      "className" : "CodegenSampleController",
                      "variableName" : "codegenSampleController",
                      "serviceMappingId" : "codegen-sample-service",
                      "apiPath" : "/codegen-sample",
                      "endpoints" : [ {
                        "apiPath" : "/full",
                        "apiRequestParameters" : { },
                        "apiMethodName" : "getAllData",
                        "serviceMethodName" : "getAllData"
                      }, {
                        "apiPath" : "/lite",
                        "apiRequestParameters" : {
                          "title" : [ "cc" ]
                        },
                        "apiMethodName" : "getLiteData",
                        "serviceMethodName" : "getLiteData"
                      } ]
                    } ],
                    "services" : [ {
                      "id" : "codegen-sample-service",
                      "namespace" : "CodegenSample",
                      "packageName" : "me.roybailey.codegen.api.codegensample",
                      "className" : "CodegenSampleService",
                      "variableName" : "codegenSampleService",
                      "tableMappingId" : "codegen-sample-table",
                      "modelMappingId" : "codegen-sample-model"
                    } ],
                    "tables" : [ {
                      "id" : "codegen-sample-table",
                      "namespace" : "CodegenSample",
                      "packageName" : "me.roybailey.codegen.api.codegensample",
                      "className" : "CodegenSampleTable",
                      "tableName" : "v_temp_codegen_sample",
                      "columns" : [ {
                        "column" : "id",
                        "ordinalPosition" : 1,
                        "databaseType" : "integer",
                        "type" : "INTEGER"
                      }, {
                        "column" : "title",
                        "ordinalPosition" : 2,
                        "databaseType" : "character varying",
                        "type" : "TEXT"
                      }, {
                        "column" : "created_at",
                        "ordinalPosition" : 3,
                        "databaseType" : "timestamp with time zone",
                        "type" : "TIMESTAMP"
                      }, {
                        "column" : "updated_at",
                        "ordinalPosition" : 4,
                        "databaseType" : "timestamp with time zone",
                        "type" : "TIMESTAMP"
                      }, {
                        "column" : "description",
                        "ordinalPosition" : 5,
                        "databaseType" : "text",
                        "type" : "TEXT"
                      }, {
                        "column" : "periodfrom",
                        "ordinalPosition" : 6,
                        "databaseType" : "character varying",
                        "type" : "TEXT"
                      }, {
                        "column" : "periodupto",
                        "ordinalPosition" : 7,
                        "databaseType" : "character varying",
                        "type" : "TEXT"
                      }, {
                        "column" : "price",
                        "ordinalPosition" : 8,
                        "databaseType" : "double precision",
                        "type" : "DOUBLE"
                      }, {
                        "column" : "discount",
                        "ordinalPosition" : 9,
                        "databaseType" : "double precision",
                        "type" : "DOUBLE"
                      } ],
                      "filters" : [ {
                        "type" : "LIKE",
                        "name" : "title",
                        "column" : "title",
                        "params" : { }
                      } ]
                    } ],
                    "models" : [ {
                      "id" : "codegen-sample-model",
                      "namespace" : "CodegenSample",
                      "packageName" : "me.roybailey.codegen.api.codegensample",
                      "className" : "CodegenSampleModel",
                      "fields" : [ {
                        "fieldName" : "id",
                        "fieldType" : "Integer",
                        "jsonName" : "id"
                      }, {
                        "fieldName" : "title",
                        "fieldType" : "String",
                        "jsonName" : "title"
                      }, {
                        "fieldName" : "created_at",
                        "fieldType" : "Timestamp",
                        "jsonName" : "created_at"
                      }, {
                        "fieldName" : "updated_at",
                        "fieldType" : "Timestamp",
                        "jsonName" : "updated_at"
                      }, {
                        "fieldName" : "description",
                        "fieldType" : "String",
                        "jsonName" : "description"
                      }, {
                        "fieldName" : "periodfrom",
                        "fieldType" : "String",
                        "jsonName" : "periodfrom"
                      }, {
                        "fieldName" : "periodupto",
                        "fieldType" : "String",
                        "jsonName" : "periodupto"
                      }, {
                        "fieldName" : "price",
                        "fieldType" : "Double",
                        "jsonName" : "price"
                      }, {
                        "fieldName" : "discount",
                        "fieldType" : "Double",
                        "jsonName" : "discount"
                      } ]
                    } ]
                  } ]
                }
            """.trimIndent()
            )
        }
    }


    @Test
    fun testBlueprintColumnTypeMappingsParser() {

        val mapColumnTypeMappings = generatorProperties.getColumnTypeMappings()
        val keyMatch = mapColumnTypeMappings.filter { "primary key".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val doubleMatch =
            mapColumnTypeMappings.filter { "double precision".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val varcharMatch =
            mapColumnTypeMappings.filter { "variable character".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val integerMatch = mapColumnTypeMappings.filter { "integer".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val timestampMatch =
            mapColumnTypeMappings.filter { "timestamp".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val unknownMatch = mapColumnTypeMappings.filter { "unknown".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }

        assertThat(keyMatch.keys.first()).isEqualTo("ID")
        assertThat(doubleMatch.keys.first()).isEqualTo("DOUBLE")
        assertThat(varcharMatch.keys.first()).isEqualTo("TEXT")
        assertThat(integerMatch.keys.first()).isEqualTo("INTEGER")
        assertThat(timestampMatch.keys.first()).isEqualTo("TIMESTAMP")
        assertThat(unknownMatch.size).isEqualTo(0)
    }
}
