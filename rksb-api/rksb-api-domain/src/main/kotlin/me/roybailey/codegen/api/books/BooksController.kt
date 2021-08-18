// !!!!! GENERATED BY me.roybailey.generator.ControllerCodeGenerator !!!!!
package me.roybailey.codegen.api.books

import me.roybailey.codegen.jooq.database.Tables.BOOKS
import me.roybailey.codegen.jooq.database.tables.records.BooksRecord
import me.roybailey.codegen.jooq.database.tables.pojos.Books
import org.jooq.DSLContext
import org.jooq.impl.DSL.select
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class BooksController(private val booksService: BooksService) {

    @GetMapping
    @RequestMapping("/books")
    fun getAllData(): List<Books> {
        return booksService.getAllData();
    }
}
// !!!!! GENERATED BY me.roybailey.generator.ControllerCodeGenerator !!!!!