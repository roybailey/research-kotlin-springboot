package me.roybailey.generator.code

import me.roybailey.api.blueprint.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ServiceCodeGeneratorTest {

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
    val expectedCode = """
package me.roybailey.domain.api.books

import me.roybailey.api.common.BaseService
import me.roybailey.codegen.jooq.database.Tables.BOOKS
import me.roybailey.codegen.jooq.database.tables.records.BooksRecord
import me.roybailey.codegen.jooq.database.tables.pojos.Books
import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Component


@Component
class BooksService(protected open val dsl: DSLContext) : BaseService("TEST","BOOKS") {

    fun getAllData(params: Map<String,Any>): List<Books> {
        val results = dsl
            .select()
            .from(BOOKS)
            .where(getFilterCondition(params, BOOKS))
            .limit(100)
            .fetch()
            .into(Books::class.java)
        return results;
    }
}
""".trimIndent()


    @Test
    fun testServiceCodeGenerator() {

        val tableMappings = listOf(
            ApiTableMapping(
                id="books",
                table = "BOOKS",
                record = "BooksRecord",
                domain = "Books",
                columnMapping = listOf(
                    ApiColumnMapping(column = "ID", databaseType = "ID", type = "ID"),
                    ApiColumnMapping(column = "TITLE", databaseType = "TEXT", type = "STRING"),
                    ApiColumnMapping(column = "PUBLICATIONDATE", databaseType = "INTEGER", type = "DATE")
                ),
                filterMapping = listOf(
                    ApiFilterMapping(type = ApiFilterType.EQUAL, name = "title", column = "TITLE")
                ),
                testData = listOf()
            )
        )

        val generatedCode = ServiceCodeGenerator().generateTableService(
            apiBlueprint = ApiBlueprint(id = "TEST", tableMapping = tableMappings),
            tableMapping = tableMappings[0],
            basePackageName = "me.roybailey.domain.api"
        )

        assertThat(generatedCode).isEqualTo(expectedCode)
    }
}
