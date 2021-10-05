package me.roybailey.api.common

import javax.annotation.PostConstruct


open class BaseController(
    override val blueprintId: String,
    val controllerMappingId: String
) : AbstractBlueprintComponent(blueprintId) {

    protected lateinit var controllerMapping: ControllerMapping

    @PostConstruct
    fun initController() {
        controllerMapping = blueprint.controllers.find { it.id == controllerMappingId }!!
    }


    fun getApiRequestParameters(apiPath: String): Map<out String, Array<String>> {
        val endpointMapping = this.controllerMapping.endpoints.find { it.apiPath == apiPath }
        return endpointMapping?.let {
            it.apiRequestParameters
        } ?: emptyMap()
    }

}
