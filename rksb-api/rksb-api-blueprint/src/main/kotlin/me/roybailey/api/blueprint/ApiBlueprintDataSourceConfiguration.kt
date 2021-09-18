package me.roybailey.api.blueprint

import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
@ConditionalOnProperty(value=["api.datasource.enabled"], havingValue = "true", matchIfMissing = false)
open class ApiBlueprintDataSourceConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    @Primary
    @ConfigurationProperties("api.datasource.blueprints")
    open fun blueprintsDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary // this will override the datasource autoconfiguration and use your own everywhere
    @ConditionalOnProperty(value=["api.datasource.enabled"], havingValue = "true", matchIfMissing = false)
    open fun dataSource(): DataSource {
        return blueprintsDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource::class.java).build()
    }

}
