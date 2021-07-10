package me.roybailey.demo.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate


@Configuration
class ApplicationConfiguration {

    @Value("\${starwars.api.planets.uri}")
    lateinit var starwarsApiPlanetUri: String

    @Bean
    fun restTemplateBuilder() = RestTemplateBuilder()

    @Bean
    fun starwarsRestPlanets(builder: RestTemplateBuilder): RestTemplate =
        builder.rootUri(starwarsApiPlanetUri)
            .additionalInterceptors(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                // request.getHeaders().add("Bearer", "token");
                execution.execute(request, body!!);
            })
            .build()

}
