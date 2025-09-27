package com.cristianino.productapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MicroserviceClientConfig {

    @Value("${app.api.key}")
    private String apiKey;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.api+json")
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.api+json")
            .defaultHeader("X-API-Key", apiKey)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)); // 1MB
    }

    @Bean
    public WebClient inventoryServiceClient(WebClient.Builder webClientBuilder,
                                          @Value("${microservices.inventory.base-url:http://localhost:8081}") String baseUrl) {
        return webClientBuilder
            .baseUrl(baseUrl)
            .build();
    }

    @Bean  
    public WebClient genericMicroserviceClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}