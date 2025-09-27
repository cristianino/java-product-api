package com.cristianino.productapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product API - Multi-Version")
                .version("2.0.0")
                .description("A Spring Boot 3 microservice for managing products with versioned APIs and JSON:API specification")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@cristianino.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development server"),
                new Server().url("https://api.cristianino.com").description("Production server")
            ))
            .components(new Components()
                .addSecuritySchemes("X-API-Key", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-Key")
                    .description("API Key for authentication")));
    }
    
    @Bean
    public GroupedOpenApi publicApiV1() {
        return GroupedOpenApi.builder()
                .group("v1-products")
                .displayName("Products API V1.0")
                .pathsToMatch("/api/v1/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi publicApiV2() {
        return GroupedOpenApi.builder()
                .group("v2-products")
                .displayName("Products API V2.0 (Current)")
                .pathsToMatch("/api/v2/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi legacyApi() {
        return GroupedOpenApi.builder()
                .group("legacy")
                .displayName("Legacy API (Deprecated)")
                .pathsToMatch("/api/products/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi connectivityApi() {
        return GroupedOpenApi.builder()
                .group("connectivity")
                .displayName("Connectivity & Health")
                .pathsToMatch("/api/connectivity/**", "/actuator/**")
                .build();
    }
}