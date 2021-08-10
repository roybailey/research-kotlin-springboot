package me.roybailey.common.database.postgres

import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

// under test source because it's only needed for testing
@SpringBootApplication
open class PostgresTestSpringBootApplication {

    val logger = KotlinLogging.logger {}

    @Bean
    open fun demo(repository: BookRepository): CommandLineRunner? {
        return CommandLineRunner { args ->
            // save a few Books
            (100L..105L).forEach {
                val bookToSave = Book(it, "ISBN$it", "Book $it")
                logger.info { "Saving book $bookToSave" }
                repository.save(bookToSave)
            }

            // fetch all Books
            logger.info("Books found with findAll():")
            logger.info("-------------------------------")
            for (book in repository.findAll()) {
                logger.info { "Found Book $book" }
            }
            logger.info("")

            // fetch an individual Book by ID
            val book: Book = repository.findById(1L).get()
            logger.info("Book found with findById(1L):")
            logger.info("--------------------------------")
            logger.info(book.toString())
            logger.info("")

            // fetch Books by last name
            logger.info("Book found with findByLastName('Bauer'):")
            logger.info("--------------------------------------------")
            repository.findByTitle("Book 101").forEach {
                    bk -> logger.info(bk.toString())
            }
            logger.info("")
        }
    }    
}
