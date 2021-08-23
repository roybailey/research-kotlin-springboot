// !!!!! GENERATED BY me.roybailey.generator.ServiceCodeGenerator !!!!!
package me.roybailey.codegen.api.tempcities

import me.roybailey.codegen.jooq.database.Tables.TEMP_CITIES
import me.roybailey.codegen.jooq.database.tables.records.TempCitiesRecord
import me.roybailey.codegen.jooq.database.tables.pojos.TempCities
import org.jooq.DSLContext
import org.springframework.stereotype.Component


@Component
class TempCitiesService(private val dsl: DSLContext) {

    fun getAllData(): List<TempCities> {
        val results = dsl
            .select()
            .from(TEMP_CITIES)
            .fetch()
            .into(TempCities::class.java)
        return results;
    }
}
// !!!!! GENERATED BY me.roybailey.generator.ServiceCodeGenerator !!!!!