package me.roybailey.api.common

import me.roybailey.api.blueprint.*
import mu.KotlinLogging
import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.jooq.impl.TableImpl
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct


abstract class AbstractBlueprintComponent(
    open val blueprintId: String,
    open val serviceMappingId: String,
    open val tableMappingId: String,
    open val modelMappingId: String
) {

    protected val logger = KotlinLogging.logger {}

    @Autowired
    protected lateinit var blueprintCollection: BlueprintCollection

    protected lateinit var blueprint: Blueprint
    protected lateinit var serviceMapping: ServiceMapping
    protected lateinit var tableMapping: TableMapping
    protected lateinit var modelMapping: ModelMapping


    @PostConstruct
    fun init() {
        this.blueprint = this.blueprintCollection.blueprints.find { it.id == blueprintId }!!
        this.serviceMapping = blueprintCollection.allServices().find { it.id == serviceMappingId }!!
        this.tableMapping = blueprintCollection.allTables().find { it.id == tableMappingId }!!
        this.modelMapping = blueprintCollection.allModels().find { it.id == modelMappingId }!!
    }

}
