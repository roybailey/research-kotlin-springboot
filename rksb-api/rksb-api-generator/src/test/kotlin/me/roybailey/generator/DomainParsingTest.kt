package me.roybailey.generator

import me.roybailey.common.test.UnitTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.streams.toList


class DomainParsingTest : UnitTestBase() {

    @Test
    fun testParseApiSpecification() {

        val config = Configuration().also {
            it.basedir = moduleTestDataFolder
        }
        val apiSpecs = config.apiSpecification()
        assertThat(apiSpecs).isNotNull
        assertThat(apiSpecs.size).isEqualTo(2)
        apiSpecs.stream().filter { it.name == "books" }.toList()[0].let { apiSpec ->
            assertThat(apiSpec.tableMapping.size).isEqualTo(1)
            apiSpec.tableMapping[0].let { tableMapping ->
                assertThat(tableMapping.columnMapping.size).isEqualTo(5)
                val columnMap = tableMapping.columnMapping.associateBy({ it.column }, { it })
                assertThat(columnMap.containsKey("PUBLICATIONDATE")).isTrue
                assertThat(columnMap["PUBLICATIONDATE"]!!.testDataStrategy).isEqualTo("datesequence")
            }
        }
    }
}
