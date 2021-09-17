package me.roybailey.api.blueprint

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
    fun testApiBlueprintConfiguration() {

        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprints).isNotNull
        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprints.size).isGreaterThan(0)

        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseUrl).isNotNull
        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseUrl.length).isGreaterThan(0)

        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseUsername).isNotNull
        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseUsername.length).isGreaterThan(0)

        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseSchema).isNotNull
        assertThat(apiBlueprintConfiguration.apiBlueprintProperties.blueprintsDatabaseSchema.length).isGreaterThan(0)
    }


    @Test
    fun testApiBlueprints() {

        assertThat(apiBlueprints).isNotNull
        assertThat(apiBlueprints.size).isEqualTo(apiBlueprintConfiguration.apiBlueprintProperties.blueprints.size)
    }


    @Test
    fun testBlueprintParsing() {
        apiBlueprints.stream().filter { it.name == "books" }.toList()[0].let { apiBlueprint ->
            assertThat(apiBlueprint.tableMapping.size).isEqualTo(1)
            apiBlueprint.tableMapping[0].let { tableMapping ->
                assertThat(tableMapping.columnMapping.size).isEqualTo(5)
                val columnMap = tableMapping.columnMapping.associateBy({ it.column }, { it })
                assertThat(columnMap.containsKey("publicationdate")).isTrue
                assertThat(columnMap["publicationdate"]!!.testDataStrategy).isEqualTo("datesequence")
            }
        }
    }
}
