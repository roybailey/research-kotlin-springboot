package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.GeneratorTestBase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ModelCodeGeneratorTest : GeneratorTestBase() {

    val expectedCode = """
package me.roybailey.codegen.api.codegensample

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp


data class CodegenSampleModel (
    
    @JsonProperty("id") var id: Integer,
    
    @JsonProperty("title") var title: String,
    
    @JsonProperty("createdAt") var created_at: Timestamp,
    
    @JsonProperty("updatedAt") var updated_at: Timestamp,
    
    @JsonProperty("description") var description: String,
    
    @JsonProperty("periodFrom") var period_from: String,
    
    @JsonProperty("periodUpto") var period_upto: String,
    
    @JsonProperty("price") var price: Double,
    
    @JsonProperty("discount") var discount: Integer,
    
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

        var something = 0
        ++something
        println(something)
    }
}
