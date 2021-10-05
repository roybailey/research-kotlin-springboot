package me.roybailey.api.common

import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.jooq.impl.TableImpl
import javax.annotation.PostConstruct


open class BaseService(
    override val blueprintId: String,
    val serviceMappingId: String,
    val tableMappingId: String,
    val modelMappingId: String
) : AbstractBlueprintComponent(blueprintId) {

    protected lateinit var serviceMapping: ServiceMapping
    protected lateinit var tableMapping: TableMapping
    protected lateinit var modelMapping: ModelMapping


    @PostConstruct
    fun initService() {
        this.serviceMapping = blueprintCollection.allServices().find { it.id == serviceMappingId }!!
        this.tableMapping = blueprintCollection.allTables().find { it.id == tableMappingId }!!
        this.modelMapping = blueprintCollection.allModels().find { it.id == modelMappingId }!!
    }


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
