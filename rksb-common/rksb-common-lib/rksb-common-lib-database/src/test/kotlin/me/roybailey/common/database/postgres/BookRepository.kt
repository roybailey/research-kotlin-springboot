package me.roybailey.common.database.postgres

import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {

    fun findByTitle(title: String): List<Book>
}
