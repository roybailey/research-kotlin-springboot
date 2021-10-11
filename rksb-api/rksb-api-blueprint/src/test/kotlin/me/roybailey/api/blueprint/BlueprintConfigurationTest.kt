package me.roybailey.api.blueprint

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = ["me.roybailey.api.blueprint"])
open class TestBlueprintSpringApplication

@SpringBootTest(classes = [TestBlueprintSpringApplication::class])
@ActiveProfiles("test", "blueprint")
open class BlueprintConfigurationTest {

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection

    @Autowired
    lateinit var blueprintProperties: BlueprintProperties

    @Autowired
    lateinit var blueprintConfiguration: BlueprintConfiguration


    @Test
    fun testBlueprintConfiguration() {

        assertThat(blueprintProperties.blueprintTemplates).isNotNull
        assertThat(blueprintProperties.blueprintTemplates.size).isGreaterThan(0)
        assertThat(blueprintProperties.blueprintTemplates[0]).isEqualTo("api/codegen-sample-blueprint.json")

        assertThat(blueprintProperties.blueprintBasePackage).isNotNull
        assertThat(blueprintProperties.blueprintBasePackage.length).isGreaterThan(0)

        assertThat(blueprintProperties.blueprintCollectionFilename).isNotNull
        assertThat(blueprintProperties.blueprintCollectionFilename.length).isGreaterThan(0)

        assertThat(blueprintProperties.blueprintDatabaseUrl).isNotNull
        assertThat(blueprintProperties.blueprintDatabaseUrl.length).isGreaterThan(0)

        assertThat(blueprintProperties.blueprintDatabaseUsername).isNotNull
        assertThat(blueprintProperties.blueprintDatabaseUsername.length).isGreaterThan(0)

        assertThat(blueprintProperties.blueprintDatabaseSchema).isNotNull
        assertThat(blueprintProperties.blueprintDatabaseSchema.length).isGreaterThan(0)

        assertThat(blueprintProperties.blueprintDatabaseExcludes).isNotNull
    }


    @Test
    fun testBlueprints() {

        assertThat(blueprintCollection).isNotNull
        assertThat(blueprintCollection.packageName).isNotBlank
        assertThat(blueprintCollection.blueprints.size).isEqualTo(blueprintConfiguration.defaultBlueprintCollection().blueprints.size)

        // use the first codegensample blueprint to verify the configuration loads and resolves as expected
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
            """.trimIndent()
            )
        }
    }
}
