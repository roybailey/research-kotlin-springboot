package me.roybailey.api.common

import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.jooq.impl.TableImpl


open class BaseService(
    override val blueprintId: String,
    override val serviceMappingId: String,
    override val tableMappingId: String,
    override val modelMappingId: String
) : AbstractBlueprintComponent(
    blueprintId,
    serviceMappingId,
    tableMappingId,
    modelMappingId
) {


    private fun getString(params: Map<String, Any>, name: String): String {
        return when (params[name]) {
            is Array<*> -> (params[name] as Array<*>)[0].toString()
            else -> params[name].toString()
        }
    }


    protected fun getFilterCondition(
        params: Map<String, Any>,
        table: TableImpl<*>
    ): Condition {
        var result: Condition = trueCondition()
        tableMapping.filters.forEach { filterMapping ->
            if (params.contains(filterMapping.name)) {
                logger.info("Building filter ${filterMapping.name}")
                result = result.and(
                    table.field(filterMapping.column.toLowerCase())!!
                        .like("%" + getString(params, filterMapping.name) + "%")
                )
            }
        }
        return result
    }


}
