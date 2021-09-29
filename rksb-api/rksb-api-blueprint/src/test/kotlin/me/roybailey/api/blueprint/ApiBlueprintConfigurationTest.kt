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
open class TestApiBlueprintSpringApplication

@SpringBootTest(classes = [TestApiBlueprintSpringApplication::class])
@ActiveProfiles("test")
open class ApiBlueprintConfigurationTest {

    @Autowired
    lateinit var apiBlueprints: List<ApiBlueprint>

    @Autowired
    lateinit var apiBlueprintProperties: ApiBlueprintProperties

    @Autowired
    lateinit var apiBlueprintConfiguration: ApiBlueprintConfiguration


    @Test
    fun testApiBlueprintConfiguration() {

        assertThat(apiBlueprintProperties.blueprints).isNotNull
        assertThat(apiBlueprintProperties.blueprints.size).isGreaterThan(0)

        assertThat(apiBlueprintProperties.blueprintsDatabaseUrl).isNotNull
        assertThat(apiBlueprintProperties.blueprintsDatabaseUrl.length).isGreaterThan(0)

        assertThat(apiBlueprintProperties.blueprintsDatabaseUsername).isNotNull
        assertThat(apiBlueprintProperties.blueprintsDatabaseUsername.length).isGreaterThan(0)

        assertThat(apiBlueprintProperties.blueprintsDatabaseSchema).isNotNull
        assertThat(apiBlueprintProperties.blueprintsDatabaseSchema.length).isGreaterThan(0)

        assertThat(apiBlueprintProperties.codegenBasePackage).isNotNull
        assertThat(apiBlueprintProperties.codegenBasePackage.length).isGreaterThan(0)

        assertThat(apiBlueprintProperties.columnTypeMappings.size).isGreaterThan(0)
    }


    @Test
    fun testApiBlueprints() {

        assertThat(apiBlueprints).isNotNull
        assertThat(apiBlueprints.size).isEqualTo(apiBlueprintProperties.blueprints.size)
        assertThat(apiBlueprints.size).isEqualTo(apiBlueprintConfiguration.apiBlueprints().size)
    }


    @Test
    fun testApiBlueprintColumnTypeMappingsParser() {

        val mapColumnTypeMappings = apiBlueprintProperties.getColumnTypeMappings()
        val keyMatch = mapColumnTypeMappings.filter { "primary key".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val doubleMatch = mapColumnTypeMappings.filter { "double precision".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val varcharMatch = mapColumnTypeMappings.filter { "variable character".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val integerMatch = mapColumnTypeMappings.filter { "integer".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val timestampMatch = mapColumnTypeMappings.filter { "timestamp".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }
        val unknownMatch = mapColumnTypeMappings.filter { "unknown".contains(Regex(it.value, RegexOption.IGNORE_CASE)) }

        assertThat(keyMatch.keys.first()).isEqualTo("ID")
        assertThat(doubleMatch.keys.first()).isEqualTo("DOUBLE")
        assertThat(varcharMatch.keys.first()).isEqualTo("TEXT")
        assertThat(integerMatch.keys.first()).isEqualTo("INTEGER")
        assertThat(timestampMatch.keys.first()).isEqualTo("TIMESTAMP")
        assertThat(unknownMatch.size).isEqualTo(0)
    }
}
