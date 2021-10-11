package me.roybailey.api.blueprint

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
@ConditionalOnProperty(value=["blueprint.datasource.enabled"], havingValue = "true", matchIfMissing = false)
open class BlueprintDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("blueprint.datasource")
    open fun blueprintDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary // this will override the datasource autoconfiguration and use your own everywhere
    @ConditionalOnProperty(value=["blueprint.datasource.enabled"], havingValue = "true", matchIfMissing = false)
    open fun dataSource(): DataSource {
        return blueprintDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

}
