package me.roybailey.api.common

import me.roybailey.api.blueprint.*
import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.jooq.impl.TableImpl
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
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
        parameters: Map<String, Any>,
        table: TableImpl<*>
    ): Condition {
        val params = HashMap<String, Any>(parameters)
        var result: Condition = trueCondition()
        // first search for custom or overridden filters
        tableMapping.filters.forEach { filterMapping ->
            if (params.contains(filterMapping.name)) {
                logger.info("Building filter ${filterMapping.name}=${params[filterMapping.name]}")
                result = when (filterMapping.type) {
                    FilterType.EQUAL -> result.and(
                        "${table.field(filterMapping.column.toLowerCase())!!} = ${getString(params, filterMapping.name)}"
                    )
                    FilterType.BETWEEN -> result.and(
                        "${table.field(filterMapping.column.toLowerCase())!!} <= '${getString(params, filterMapping.name)}'"
                    ).and(
                        "${table.field((filterMapping.params["column2"] as String).toLowerCase())!!} > '${getString(params, filterMapping.name)}'"
                    )
                    else ->
                        result.and(
                            table.field(filterMapping.column.toLowerCase())!!
                                .like("%" + getString(params, filterMapping.name) + "%")
                        )
                }
                params.remove(filterMapping.name)
            }
        }
        // second search for generic column filters
        tableMapping.columns.forEach { columnMapping ->
            if (params.contains(columnMapping.column)) {
                logger.info("Building column filter ${columnMapping.column}=${params[columnMapping.column]}")
                result = when (columnMapping.type) {
                    ColumnType.INTEGER ->
                        result.and(
                            "${table.field(columnMapping.column.toLowerCase())!!} = ${getString(params, columnMapping.column)}"
                        )
                    ColumnType.DOUBLE ->
                        result.and(
                            "${table.field(columnMapping.column.toLowerCase())!!} = ${getString(params, columnMapping.column)}"
                        )
                    else ->
                        result.and(
                            table.field(columnMapping.column.toLowerCase())!!
                                .like("%" + getString(params, columnMapping.column) + "%")
                        )
                }
                params.remove(columnMapping.column)
            }
        }
        logger.info("Filter assigned as: [$result]")
        return result
    }

    /*
fun condition(request:Map<String,Any>) {
    val result = trueCondition()

    if (params.contains("COLUMN")) {
        result = result.and(Tables.TABLE_NAME.COLUMN_NAME.like("%"+params.get("COLUMN")+"%"))
    }

    if (params.contains("COLUMN")) {
        result = result.and(Tables.TABLE_NAME.COLUMN_NAME.eq(Integer.parseInt(params.get("COLUMN"))))
    }

    if (params.contains("COLUMN")) {
        result = result.and(toDate(Tables.TABLE_NAME.COLUMN_NAME_START, "YYYY-MM-DD").lessOrEqual(toDate(params.get("COLUMN_NAME", "YYYY-MM-DD"))
        result = result.and(toDate(Tables.TABLE_NAME.COLUMN_NAME_END, "YYYY-MM-DD").greaterOrEqual(toDate(params.get("COLUMN_NAME", "YYYY-MM-DD"))
    }

    result
}
 */

}
