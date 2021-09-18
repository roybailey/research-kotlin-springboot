package me.roybailey.api.blueprint

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.streams.toList

@SpringBootApplication(
    scanBasePackageClasses = [ApiBlueprintConfiguration::class],
    exclude = [FlywayAutoConfiguration::class]
)
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

}
