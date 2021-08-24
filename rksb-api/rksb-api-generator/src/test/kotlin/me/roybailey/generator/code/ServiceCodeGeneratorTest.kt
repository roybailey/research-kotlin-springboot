package me.roybailey.generator.code

import me.roybailey.generator.ApiTableMapping
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows


class ServiceCodeGeneratorTest {

    val expectedCode = """
package me.roybailey.domain.api.books

import me.roybailey.codegen.jooq.database.Tables.BOOKS
import me.roybailey.codegen.jooq.database.tables.records.BooksRecord
import me.roybailey.codegen.jooq.database.tables.pojos.Books
import org.jooq.*
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Component


@Component
class BooksService(private val dsl: DSLContext) {

    fun condition(request:Map<String,Any>) {
        val result = trueCondition()
        result
    } 

    fun getAllData(): List<Books> {
        val results = dsl
            .select()
            .from(BOOKS)
            .fetch()
            .into(Books::class.java)
        return results;
    }
}
    """.trimIndent()


    @Test
    fun testServiceCodeGenerator() {
       val generatedCode = ServiceCodeGenerator().generateTableService(
           ApiTableMapping(
               table="BOOKS",
               record = "BooksRecord",
               domain="Books",
               columnMapping = listOf(),
               createSql = null,
               testData = listOf()
           ), "me.roybailey.domain.api")

        assertThat(generatedCode).isEqualTo(expectedCode)
    }
}
