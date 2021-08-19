package me.roybailey.common.database.postgres

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringBootTest
@ActiveProfiles("postgres")
class TestContainerPostgresTest {

    companion object {

        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("localhost")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }

    }

    @Test
    @Disabled
    fun testPostgresTestContainerInitialised() {

    }
}
