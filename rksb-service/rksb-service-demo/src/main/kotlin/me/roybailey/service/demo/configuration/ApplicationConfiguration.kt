package me.roybailey.service.demo.configuration

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource


@Configuration
open class ApplicationConfiguration {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    open fun dsl(): DSLContext {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }

    @Value("\${starwars.api.planets.uri}")
    lateinit var starwarsApiPlanetUri: String

    @Bean
    open fun restTemplateBuilder() = RestTemplateBuilder()

    @Bean
    open fun starwarsRestPlanets(builder: RestTemplateBuilder): RestTemplate =
        builder.rootUri(starwarsApiPlanetUri)
            .additionalInterceptors(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                // request.getHeaders().add("Bearer", "token");
                execution.execute(request, body!!);
            })
            .build()

}
