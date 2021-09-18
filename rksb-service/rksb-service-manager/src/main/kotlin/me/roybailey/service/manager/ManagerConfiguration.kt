package me.roybailey.service.manager

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
open class ManagerConfiguration {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    open fun dsl(): DSLContext {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }

}
