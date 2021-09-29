package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.BlueprintTestBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ModelCodeGeneratorTest : BlueprintTestBase() {

    val expectedCode = """
package me.roybailey.codegen.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp


data class CodegenSampleModel (
    
    @JsonProperty("id") var id: Integer,
    
    @JsonProperty("name") var name: String,
    
    @JsonProperty("created_at") var created_at: Timestamp,
    
    @JsonProperty("updated_at") var updated_at: Timestamp,
    
    @JsonProperty("description") var description: String,
    
    @JsonProperty("periodfrom") var periodfrom: String,
    
    @JsonProperty("periodupto") var periodupto: String,
    
    @JsonProperty("price") var price: Double,
    
    @JsonProperty("discount") var discount: Double,
    
)
""".trimIndent()

    @Autowired
    lateinit var modelCodeGenerator: ModelCodeGenerator

    @Test
    fun testModelCodeGenerator() {
        val result = modelCodeGenerator.generate()

        result.forEach {
            println(it.filename)
            println("----------")
            println(it.content)
            println("----------")
        }
        Assertions.assertThat(result[0].content).isEqualTo(expectedCode)
    }
}
