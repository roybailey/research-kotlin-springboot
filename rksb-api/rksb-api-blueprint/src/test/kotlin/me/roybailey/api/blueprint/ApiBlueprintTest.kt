package me.roybailey.api.blueprint

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiBlueprintConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.streams.toList

@SpringBootApplication(scanBasePackageClasses = [ApiBlueprintConfiguration::class])
open class TestSpringApplication

@SpringBootTest(classes = [TestSpringApplication::class])
@ActiveProfiles("test")
open class ApiBlueprintTest {

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    @Autowired
    lateinit var apiBlueprintConfiguration: ApiBlueprintConfiguration

    @Test
    fun testApiBlueprints() {

        assertThat(apiBlueprints).isNotNull
        assertThat(apiBlueprints.size).isEqualTo(2)
        apiBlueprints.stream().filter { it.name == "books" }.toList()[0].let { apiSpec ->
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
