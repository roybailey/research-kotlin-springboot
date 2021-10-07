package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.GeneratorTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ServiceCodeGeneratorTest : GeneratorTestBase() {

    @Autowired
    lateinit var serviceCodeGenerator: ServiceCodeGenerator

    val expectedCode = """
package me.roybailey.codegen.api.codegensample

import me.roybailey.api.common.BaseService
import me.roybailey.codegen.jooq.database.Tables.V_TEMP_CODEGEN_SAMPLE
import me.roybailey.codegen.api.codegensample.CodegenSampleModel

import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Component


@Component
class CodegenSampleService(
    protected open val dsl: DSLContext
) : BaseService(
    blueprintId = "codegen-sample-blueprint",
    serviceMappingId = "codegen-sample-service",
    tableMappingId = "codegen-sample-table",
    modelMappingId = "codegen-sample-model"
) {

    fun getAllData(params: Map<String,Any>): List<CodegenSampleModel> {
        val results = dsl
            .select()
            .from(V_TEMP_CODEGEN_SAMPLE)
            .where(getFilterCondition(params, V_TEMP_CODEGEN_SAMPLE))
            .limit(100)
            .fetch()
            .into(CodegenSampleModel::class.java)
        return results;
    }
}
""".trimIndent()


    @Test
    fun testServiceCodeGenerator() {
        val result = serviceCodeGenerator.generate()

        result.forEach {
            println(it.filename)
            println("----------")
            println(it.content)
            println("----------")
        }
        assertThat(result[0].content).isEqualTo(expectedCode)
    }


    /*
    fun condition(request:Map<String,Any>) {
        val result = trueCondition()

        if (params.contains("COLUMN")) {
            result = result.and(Tables.TABLE_NAME.COLUMN_NAME.like("%"+params.get("COLUMN")+"%"))
        }

        if (params.contains("COLUMN")) {
            result = result.and(Tables.TABLE_NAME.COLUMN_NAME.eq(Integer.parseInt(params.get("COLUMN"))))
        }

        if (params.contains("COLUMN")) {
            result = result.and(toDate(Tables.TABLE_NAME.COLUMN_NAME_START, "YYYY-MM-DD").lessOrEqual(toDate(params.get("COLUMN_NAME", "YYYY-MM-DD"))
            result = result.and(toDate(Tables.TABLE_NAME.COLUMN_NAME_END, "YYYY-MM-DD").greaterOrEqual(toDate(params.get("COLUMN_NAME", "YYYY-MM-DD"))
        }

        result
    }
     */
}
