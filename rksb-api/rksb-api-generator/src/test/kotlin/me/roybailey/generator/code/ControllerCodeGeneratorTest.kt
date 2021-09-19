package me.roybailey.generator.code

import me.roybailey.api.blueprint.ApiMapping
import me.roybailey.api.blueprint.ApiTableMapping
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ControllerCodeGeneratorTest {

    val expectedCode = """
package me.roybailey.domain.api.books

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.codegen.jooq.database.tables.pojos.Books


@RestController
class BooksController(private val booksService: BooksService) : BaseController() {

    @GetMapping
    @RequestMapping("/books")
    fun getAllData(request:HttpServletRequest): List<Books> {
        return booksService.getAllData(request.parameterMap);
    }
}
    """.trimIndent()


    @Test
    fun testControllerCodeGenerator() {
       val generatedCode = ControllerCodeGenerator().generateTableController(
           ApiMapping(
               id="books",
               namespace = "Books",
               tableMappingId="books",
               apiPath = "/books"
           ),
           ApiTableMapping(
               id="books",
               table="BOOKS",
               record = "BooksRecord",
               domain="Books",
               columnMapping = listOf(),
               filterMapping = listOf(),
               testData = listOf()
           ),
           "me.roybailey.domain.api"
       )

        assertThat(generatedCode).isEqualTo(expectedCode)
    }
}
