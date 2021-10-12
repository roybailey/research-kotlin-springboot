package me.roybailey.api.common

import me.roybailey.api.blueprint.*
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Table
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
        parameters: Map<String, Any>,
        table: TableImpl<*>
    ): Condition {
        val filterParams = HashMap<String, Any>(parameters)
        var result: Condition = trueCondition()
        // first search for custom or overridden filters
        tableMapping.filters.forEach { filterMapping ->
            if (filterParams.contains(filterMapping.name)) {
                logger.info("Building filter ${filterMapping.name}=${filterParams[filterMapping.name]}")
                result = when (filterMapping.type) {
                    FilterType.EQUAL -> conditionEquals(
                        result,
                        table,
                        filterMapping.column,
                        filterMapping.name,
                        filterParams
                    )
                    FilterType.LIKE -> conditionLike(
                        result,
                        table,
                        filterMapping.column,
                        filterMapping.name,
                        filterParams
                    )
                    FilterType.BETWEEN -> conditionBetween(result, table, filterMapping, filterParams)
                }
                filterParams.remove(filterMapping.name)
            }
        }
        // second search for generic column filters
        tableMapping.columns.forEach { columnMapping ->
            if (filterParams.contains(columnMapping.column)) {
                logger.info("Building column filter ${columnMapping.column}=${filterParams[columnMapping.column]}")
                result =
                    conditionEquals(result, table, columnMapping, columnMapping.column, filterParams)
                filterParams.remove(columnMapping.column)
            }
        }
        logger.info("Filter assigned as: [$result]")
        return result
    }


    private fun conditionEquals(
        result: Condition,
        table: Table<*>,
        columnMapping: ColumnMapping,
        filterName: String,
        filterParams: HashMap<String, Any>
    ): Condition {
        val tableField = table.field(columnMapping.column)!!
        return when (columnMapping.type) {
            ColumnType.TEXT -> result.and("$tableField = '${getString(filterParams, filterName)}'")
            else -> result.and("$tableField = ${getString(filterParams, filterName)}")
        }
    }


    private fun conditionEquals(
        result: Condition,
        table: Table<*>,
        columnName: String,
        filterName: String,
        filterParams: HashMap<String, Any>
    ): Condition {
        val tableField = table.field(columnName)!!
        return when {
            getString(filterParams, filterName).contains("%") ->
                result.and(tableField.like("%" + getString(filterParams, filterName) + "%"))
            // todo add IN clause handling for multiple values
            else ->
                result.and("$tableField = ${getString(filterParams, filterName)}")
        }
    }


    private fun conditionLike(
        result: Condition,
        table: Table<*>,
        columnName: String,
        filterName: String,
        filterParams: HashMap<String, Any>
    ): Condition {
        val tableField = table.field(columnName)!!
        return result.and(tableField.like("%" + getString(filterParams, filterName) + "%"))
    }


    private fun conditionBetween(
        result: Condition,
        table: Table<*>,
        filterMapping: FilterMapping,
        filterParams: HashMap<String, Any>
    ): Condition {
        // TODO this is one use-case, needs expanding to handle other types
        val tableField1 = table.field(filterMapping.column)!!
        val tableField2 = table.field((filterMapping.params["column2"] as String).toLowerCase())!!
        return result
            .and("$tableField1 <= '${getString(filterParams, filterMapping.name)}'")
            .and("$tableField2 > '${getString(filterParams, filterMapping.name)}'")
    }

}
