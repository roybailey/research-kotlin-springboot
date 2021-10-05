package me.roybailey.api.common

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
@ConditionalOnProperty(value=["codegen.datasource.enabled"], havingValue = "true", matchIfMissing = false)
open class BlueprintDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("codegen.datasource.blueprints")
    open fun blueprintsDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary // this will override the datasource autoconfiguration and use your own everywhere
    @ConditionalOnProperty(value=["codegen.datasource.enabled"], havingValue = "true", matchIfMissing = false)
    open fun dataSource(): DataSource {
        return blueprintsDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

}
