// !!!!! GENERATED BY me.roybailey.generator.code.ControllerCodeGenerator !!!!!
package me.roybailey.codegen.api.tempcities

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import me.roybailey.api.common.BaseController
import me.roybailey.codegen.jooq.database.tables.pojos.TempCities


@RestController
class CitiesController(private val tempCitiesService: TempCitiesService) : BaseController() {

    @GetMapping
    @RequestMapping("/cities")
    fun getAllData(request:HttpServletRequest): List<TempCities> {
        return tempCitiesService.getAllData(request.parameterMap);
    }
}

// !!!!! GENERATED BY me.roybailey.generator.code.ControllerCodeGenerator !!!!!