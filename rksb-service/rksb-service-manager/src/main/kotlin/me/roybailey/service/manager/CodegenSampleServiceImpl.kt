package me.roybailey.service.manager

import me.roybailey.codegen.api.codegensample.CodegenSampleModel
import me.roybailey.codegen.api.codegensample.CodegenSampleService
import me.roybailey.codegen.jooq.database.Tables
import org.jooq.DSLContext
import org.jooq.impl.DSL.count
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component


@Primary
@Component
class CodegenSampleServiceImpl : CodegenSampleService() {

    override fun getLiteData(params: Map<String, Any>): List<CodegenSampleModel> {
        val total = dsl
            .select(count())
            .from(Tables.V_TEMP_CODEGEN_SAMPLE)
            .where(getFilterCondition(params, Tables.V_TEMP_CODEGEN_SAMPLE))
            .fetch()
            .into(Integer::class.java)
        val results = dsl
            .select()
            .from(Tables.V_TEMP_CODEGEN_SAMPLE)
            .where(getFilterCondition(params, Tables.V_TEMP_CODEGEN_SAMPLE))
            .limit(100)
            .fetch()
            .into(CodegenSampleModel::class.java)
        return results;
    }
}
