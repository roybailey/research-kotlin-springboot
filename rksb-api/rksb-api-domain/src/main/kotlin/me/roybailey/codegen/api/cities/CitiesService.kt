// !!!!! GENERATED BY me.roybailey.generator.ServiceCodeGenerator !!!!!
package me.roybailey.codegen.api.cities

import me.roybailey.codegen.jooq.database.Tables.CITIES
import me.roybailey.codegen.jooq.database.tables.records.CitiesRecord
import me.roybailey.codegen.jooq.database.tables.pojos.Cities
import org.jooq.DSLContext
import org.springframework.stereotype.Component


@Component
class CitiesService(private val dsl: DSLContext) {

    fun getAllData(): List<Cities> {
        val results = dsl
            .select()
            .from(CITIES)
            .fetch()
            .into(Cities::class.java)
        return results;
    }
}
// !!!!! GENERATED BY me.roybailey.generator.ServiceCodeGenerator !!!!!
