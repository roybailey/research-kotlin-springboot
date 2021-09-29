package me.roybailey.api.generator.service.code

import me.roybailey.api.generator.BlueprintTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class ControllerCodeGeneratorTest : BlueprintTestBase() {

    @Autowired
    lateinit var controllerCodeGenerator: ControllerCodeGenerator

    val expectedCode = """
package me.roybailey.codegen.api

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.codegen.service.CodegenSampleService
import me.roybailey.codegen.model.CodegenSampleModel


@RestController("/codegen-sample")
class CodegenSampleController(
    private val codegenSampleService: CodegenSampleService
) : BaseController(
    blueprintId = "codegen-sample-blueprint",
    serviceMappingId = "codegen-sample-service",
    tableMappingId = "codegen-sample-table",
    modelMappingId = "codegen-sample-model"
) {

    
    @GetMapping
    @RequestMapping("/full")
    fun getAllData(request:HttpServletRequest): List<CodegenSampleModel> {
        val params = LinkedHashMap(request.parameterMap).apply { 
            // putAll(apiRequestParameters)
        }
        return codegenSampleService.getAllData(params)
    }
    
    @GetMapping
    @RequestMapping("/lite")
    fun getLiteData(request:HttpServletRequest): List<CodegenSampleModel> {
        val params = LinkedHashMap(request.parameterMap).apply { 
            // putAll(apiRequestParameters)
        }
        return codegenSampleService.getAllData(params)
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
