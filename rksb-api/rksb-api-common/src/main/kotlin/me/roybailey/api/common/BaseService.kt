package me.roybailey.api.common

import me.roybailey.api.blueprint.ApiBlueprint
import me.roybailey.api.blueprint.ApiTableMapping
import mu.KotlinLogging
import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.jooq.impl.TableImpl
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct


open class BaseService(val apiBlueprintName: String, val tableMappingName: String) {

    protected val logger = KotlinLogging.logger {}

    @Autowired
    protected lateinit var apiBlueprintList:List<ApiBlueprint>

    protected lateinit var apiBlueprint:ApiBlueprint
    protected lateinit var apiTableMapping:ApiTableMapping


    @PostConstruct
    fun init() {
        this.apiBlueprint = this.apiBlueprintList.find { it.name == apiBlueprintName }!!
        this.apiTableMapping = apiBlueprint.tableMapping.find { it.table == tableMappingName }!!
    }


    protected fun getString(params: Map<String, Any>, name: String): String {
        return when (params.get(name)) {
            is Array<*> -> (params[name] as Array<*>)[0].toString()
            else -> params[name].toString()
        }
    }


    protected fun getFilterCondition(
        params: Map<String, Any>,
        table: TableImpl<*>
    ): Condition {
        var result: Condition = trueCondition()
        apiTableMapping.filterMapping.forEach { filterMapping ->
            if (params.contains(filterMapping.name)) {
                logger.info("Building filter ${filterMapping.name}")
                result = result.and(
                    table.field(filterMapping.column.toLowerCase())!!.like("%" + getString(params, filterMapping.name) + "%")
                )
            }
        }
        return result
    }

}
