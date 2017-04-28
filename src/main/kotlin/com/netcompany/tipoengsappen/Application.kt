package com.netcompany.tipoengsappen

import com.netcompany.tipoengsappen.auth.DiscoveryDocument
import com.netcompany.tipoengsappen.auth.OpenIdConnectAuth
import com.netcompany.tipoengsappen.rest.SparkService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application {
    @Bean
    open fun init(services: Array<SparkService>) = CommandLineRunner {
        services.forEach(SparkService::init)
    }

    @Bean
    open fun openIdConnectAuth(
            @Value("\${open-id-connect.redirect-uri}") redirectUri: String,
            @Value("\${open-id-connect.client-id}") clientId: String,
            @Value("\${open-id-connect.secret}") secret: String,
            @Value("\${open-id-connect.discovery-document-uri}") discoveryDocumentUri: String): OpenIdConnectAuth {
        return OpenIdConnectAuth(clientId, secret, redirectUri, DiscoveryDocument(discoveryDocumentUri))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
