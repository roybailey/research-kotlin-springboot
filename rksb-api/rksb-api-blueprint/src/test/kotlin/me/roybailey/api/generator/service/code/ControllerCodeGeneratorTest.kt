package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.BlueprintTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ControllerCodeGeneratorTest : BlueprintTestBase() {

    @Autowired
    lateinit var controllerCodeGenerator: ControllerCodeGenerator

    val expectedCode = """
package me.roybailey.codegen.api.codegensample

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.api.common.ApiResponse
import me.roybailey.codegen.api.codegensample.CodegenSampleService
import me.roybailey.codegen.api.codegensample.CodegenSampleModel


@RestController
@RequestMapping("/codegen-sample")
class CodegenSampleController(
    private val codegenSampleService: CodegenSampleService
) : BaseController(
    blueprintId = "codegen-sample-blueprint",
    controllerMappingId = "codegen-sample-controller"
) {

    
    @GetMapping
    @RequestMapping("/full")
    fun getAllData(request:HttpServletRequest): ApiResponse<CodegenSampleModel> {
        val params = LinkedHashMap(request.parameterMap).apply { 
            putAll(getApiRequestParameters("/full"))
        }
        val results = codegenSampleService.getAllData(params)
        return ApiResponse(results.size, results)
    }
    
    @GetMapping
    @RequestMapping("/lite")
    fun getLiteData(request:HttpServletRequest): ApiResponse<CodegenSampleModel> {
        val params = LinkedHashMap(request.parameterMap).apply { 
            putAll(getApiRequestParameters("/lite"))
        }
        val results = codegenSampleService.getAllData(params)
        return ApiResponse(results.size, results)
    }
    
}
    """.trimIndent()


    @Test
    fun testControllerCodeGenerator() {
        val result = controllerCodeGenerator.generate()

        result.forEach {
            println(it.filename)
            println("----------")
            println(it.content)
            println("----------")
        }
        assertThat(result[0].content).isEqualTo(expectedCode)
    }
}