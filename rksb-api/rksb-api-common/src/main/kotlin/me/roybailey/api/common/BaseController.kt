package me.roybailey.api.common


open class BaseController(
    override val blueprintId: String,
    override val serviceMappingId: String,
    override val tableMappingId: String,
    override val modelMappingId: String
) : AbstractBlueprintComponent(
    blueprintId,
    serviceMappingId,
    tableMappingId,
    modelMappingId
)
