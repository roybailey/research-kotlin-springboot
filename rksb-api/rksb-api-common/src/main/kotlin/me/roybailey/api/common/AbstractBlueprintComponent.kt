package me.roybailey.api.common

import me.roybailey.api.blueprint.Blueprint
import me.roybailey.api.blueprint.BlueprintCollection
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct


abstract class AbstractBlueprintComponent(open val blueprintId: String) {

    protected val logger = KotlinLogging.logger {}

    @Autowired
    protected lateinit var blueprintCollection: BlueprintCollection

    protected lateinit var blueprint: Blueprint


    @PostConstruct
    fun init() {
        this.blueprint = this.blueprintCollection.blueprints.find { it.id == blueprintId }!!
    }

}
