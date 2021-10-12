package me.roybailey.api.generator

import com.fasterxml.jackson.databind.ObjectMapper
import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.blueprint.ColumnType
import me.roybailey.api.blueprint.FieldType
import me.roybailey.common.util.unixEOL
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

open class GeneratorConfigurationTest : GeneratorTestBase() {

    @Autowired
    lateinit var mapper: ObjectMapper


    fun assertPropertyHasValue(property: String) {
        assertThat(property).isNotNull
        assertThat(property.length).isGreaterThan(0)
        assertThat(property).doesNotContain("\${")
    }

    fun assertPropertyHasValue(property: List<String>) {
        assertThat(property).isNotNull
        assertThat(property.size).isGreaterThan(0)
        assertThat(property[0]).doesNotContain("\${")
    }

    @Test
    fun testGeneratorConfiguration() {

        assertPropertyHasValue(generatorProperties.basedir)
        assertPropertyHasValue(generatorProperties.target)
        assertPropertyHasValue(generatorProperties.codegenBlueprintCollection)
    }


    @Test
    fun testBlueprints() {

        assertThat(blueprintCollection).isNotNull
        assertThat(blueprintCollection.packageName).isNotBlank
        assertThat(blueprintCollection.blueprints.size).isEqualTo(blueprintProperties.blueprintTemplates.size)

        // use the first codegen-sample blueprint to verify the configuration loads and resolves as expected
        BlueprintCollection(
            packageName = blueprintCollection.packageName,
            blueprints = listOf(blueprintCollection.blueprints[0])
        ).run {
            val codegenSampleResolved = mapper.writeValueAsString(this)
            println(codegenSampleResolved)
            assertThat(codegenSampleResolved.unixEOL()).isEqualTo(
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
        "column" : "period_from",
        "ordinalPosition" : 6,
        "databaseType" : "character varying",
        "type" : "TEXT"
      }, {
        "column" : "period_upto",
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
        "databaseType" : "integer",
        "type" : "INTEGER"
      } ],
      "filters" : [ {
        "type" : "LIKE",
        "name" : "description",
        "column" : "description",
        "params" : { }
      }, {
        "type" : "BETWEEN",
        "name" : "period",
        "column" : "period_from",
        "params" : {
          "column2" : "PERIOD_UPTO"
        }
      } ]
    } ],
    "models" : [ {
      "id" : "codegen-sample-model",
      "namespace" : "CodegenSample",
      "packageName" : "me.roybailey.codegen.api.codegensample",
      "className" : "CodegenSampleModel",
      "fields" : [ {
        "fieldName" : "id",
        "fieldType" : "INTEGER",
        "jsonName" : "id"
      }, {
        "fieldName" : "title",
        "fieldType" : "STRING",
        "jsonName" : "title"
      }, {
        "fieldName" : "created_at",
        "fieldType" : "TIMESTAMP",
        "jsonName" : "createdAt"
      }, {
        "fieldName" : "updated_at",
        "fieldType" : "TIMESTAMP",
        "jsonName" : "updatedAt"
      }, {
        "fieldName" : "description",
        "fieldType" : "STRING",
        "jsonName" : "description"
      }, {
        "fieldName" : "period_from",
        "fieldType" : "STRING",
        "jsonName" : "periodFrom"
      }, {
        "fieldName" : "period_upto",
        "fieldType" : "STRING",
        "jsonName" : "periodUpto"
      }, {
        "fieldName" : "price",
        "fieldType" : "DOUBLE",
        "jsonName" : "price"
      }, {
        "fieldName" : "discount",
        "fieldType" : "INTEGER",
        "jsonName" : "discount"
      } ]
    } ]
  } ]
}
            """.trimIndent().unixEOL()
            )
        }
    }


    @Test
    fun testColumnMappings() {
        assertThat(generatorProperties.getColumnType("primary key")).isEqualTo(ColumnType.ID)
        assertThat(generatorProperties.getColumnType("text")).isEqualTo(ColumnType.TEXT)
        assertThat(generatorProperties.getColumnType("variable character")).isEqualTo(ColumnType.TEXT)
        assertThat(generatorProperties.getColumnType("uuid")).isEqualTo(ColumnType.TEXT)
        assertThat(generatorProperties.getColumnType("xml")).isEqualTo(ColumnType.TEXT)
        assertThat(generatorProperties.getColumnType("json")).isEqualTo(ColumnType.TEXT)
        assertThat(generatorProperties.getColumnType("double precision")).isEqualTo(ColumnType.DOUBLE)
        assertThat(generatorProperties.getColumnType("numeric")).isEqualTo(ColumnType.DOUBLE)
        assertThat(generatorProperties.getColumnType("real")).isEqualTo(ColumnType.DOUBLE)
        assertThat(generatorProperties.getColumnType("integer")).isEqualTo(ColumnType.INTEGER)
        try {
            assertThat(generatorProperties.getColumnType("UNKNOWN"))
            fail("UNKNOWN database column type failed to throw IllegalArgumentException")
        } catch (iae: IllegalArgumentException) {
            // expected and correct
        }
    }


    @Test
    fun testFieldMappings() {
        assertThat(generatorProperties.getFieldType(ColumnType.ID)).isEqualTo(FieldType.STRING)
        assertThat(generatorProperties.getFieldType(ColumnType.TIMESTAMP)).isEqualTo(FieldType.TIMESTAMP)
    }


}
