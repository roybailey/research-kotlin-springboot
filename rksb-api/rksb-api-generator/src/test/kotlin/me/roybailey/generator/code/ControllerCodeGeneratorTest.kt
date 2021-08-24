package me.roybailey.generator.code

import me.roybailey.generator.ApiTableMapping
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows


class ControllerCodeGeneratorTest {

    val expectedCode = """
package me.roybailey.domain.api.books

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.codegen.jooq.database.tables.pojos.Books


@RestController
class BooksController(private val booksService: BooksService) {

    @GetMapping
    @RequestMapping("/books")
    fun getAllData(): List<Books> {
        return booksService.getAllData();
    }
}
    """.trimIndent()


    @Test
    fun testControllerCodeGenerator() {
       val generatedCode = ControllerCodeGenerator().generateTableController(
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
