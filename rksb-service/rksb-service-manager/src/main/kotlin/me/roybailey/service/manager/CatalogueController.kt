package me.roybailey.service.manager

import me.roybailey.api.blueprint.BlueprintCollection
import me.roybailey.api.common.ApiResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.codegen.api.codegensample.CodegenSampleService
import me.roybailey.codegen.api.codegensample.CodegenSampleModel
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse


@RestController
class CatalogueController {

    @Autowired
    lateinit var blueprintCollection: BlueprintCollection


    @GetMapping
    fun getIndex(request: HttpServletRequest, response: HttpServletResponse) {
        response.sendRedirect("/catalogue")
    }


    @GetMapping
    @RequestMapping("/catalogue")
    fun getCatalogue(request: HttpServletRequest): ApiResponse<CatalogueModel> {
        val listEndpoints: List<CatalogueModel> = blueprintCollection.allControllers().map { controllerMapping ->
            controllerMapping.endpoints.map { endpointMapping ->
                CatalogueModel(
                    request.requestURL.toString().replace("/catalogue", "")
                    + (controllerMapping.apiPath ?: "")
                    + endpointMapping.apiPath
                )
            }
        }.flatten()
        return ApiResponse(listEndpoints.size, listEndpoints)
    }
}
