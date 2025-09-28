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
                .title("Product API - Versioned Microservice")
                .version("1.0.0")
                .description("## 🚀 Spring Boot 3 Microservice\n\n" +
                    "A production-ready microservice for product management with **JSON:API specification** and **versioning strategy**.\n\n" +
                    "### 📋 Current Implementation\n" +
                    "- ✅ **V1.0 API**: `/api/v1/products` - Stable and recommended\n" +
                    "- ✅ **Default API**: `/api/products` - For simple integrations\n" +
                    "- 🔗 **Connectivity**: Microservice health checks\n\n" +
                    "### 🚀 V2.0 Roadmap\n" +
                    "- 📋 **Planned**: Enhanced pagination, filtering, and bulk operations\n" +
                    "- 📚 **Guide**: Complete implementation guide available in repository\n\n" +
                    "### 🔐 Authentication\n" +
                    "All endpoints require `X-API-Key` header with value: `your-secret-api-key-here`")
                .contact(new Contact()
                    .name("API Development Team")
                    .email("api-support@cristianino.com")
                    .url("https://github.com/cristianino/java-product-api")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development Server"),
                new Server().url("https://api.cristianino.com").description("Production Server (Future)")
            ))
            .components(new Components()
                .addSecuritySchemes("X-API-Key", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-Key")
                    .description("API Key required for all endpoints. Use: your-secret-api-key-here")));
    }
    
    @Bean
    public GroupedOpenApi productsApiV1() {
        return GroupedOpenApi.builder()
                .group("v1-products")
                .displayName("📦 Products API V1.0 (Current & Recommended)")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                        .title("Products API V1.0")
                        .version("1.0.0")
                        .description("### ✅ Current Stable Version\n\n" +
                            "**JSON:API compliant** endpoints for product management.\n\n" +
                            "**Features:**\n" +
                            "- 📝 Complete CRUD operations\n" +
                            "- 🔗 HATEOAS links\n" +
                            "- ✅ Production ready and tested\n" +
                            "- 🔒 API Key authentication\n\n" +
                            "**Base URL:** `/api/v1/products`\n\n" +
                            "**Authentication:** Include `X-API-Key: your-secret-api-key-here` in headers."));
                })
                .build();
    }
    
    @Bean
    public GroupedOpenApi productsApiDefault() {
        return GroupedOpenApi.builder()
                .group("default-products")
                .displayName("📦 Products API (Default/Legacy)")
                .pathsToMatch("/api/products/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                        .title("Products API - Default")
                        .version("1.0.0")
                        .description("### 🔄 Default/Legacy Endpoints\n\n" +
                            "Same functionality as V1.0 without version prefix for **simple integrations** and **backward compatibility**.\n\n" +
                            "**Features:**\n" +
                            "- 📝 Same as V1.0 functionality\n" +
                            "- 🔄 Backward compatibility\n" +
                            "- 🚀 Easy integration for basic use cases\n\n" +
                            "**Base URL:** `/api/products`\n\n" +
                            "**Recommendation:** Use V1.0 API for new integrations."));
                })
                .build();
    }
    
    @Bean
    public GroupedOpenApi connectivityHealthApi() {
        return GroupedOpenApi.builder()
                .group("connectivity")
                .displayName("🔗 Connectivity & Health Monitoring")
                .pathsToMatch("/api/connectivity/**", "/actuator/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                        .title("Connectivity & Health API")
                        .version("1.0.0")
                        .description("### 🏥 Microservice Health & Connectivity\n\n" +
                            "Endpoints for monitoring microservice health and inter-service connectivity.\n\n" +
                            "**Features:**\n" +
                            "- 🏥 Application health checks\n" +
                            "- 🔗 Microservice connectivity verification\n" +
                            "- 📊 Service dependency monitoring\n" +
                            "- ⚡ Real-time status information\n\n" +
                            "**Perfect for:**\n" +
                            "- Load balancer health checks\n" +
                            "- Service mesh monitoring\n" +
                            "- Integration testing\n" +
                            "- DevOps dashboards"));
                })
                .build();
    }
    
    @Bean
    public GroupedOpenApi futureApiV2() {
        return GroupedOpenApi.builder()
                .group("v2-future")
                .displayName("🚀 V2.0 API (Future Roadmap)")
                .pathsToMatch("/api/v2/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                        .title("Products API V2.0 - Development Roadmap")
                        .version("2.0.0-PLANNED")
                        .description("### 📋 Future Version 2.0 - Planned Implementation\n\n" +
                            "**This version is NOT YET IMPLEMENTED** - Documentation for future development.\n\n" +
                            "**Planned Enhanced Features:**\n" +
                            "- 📄 **Advanced Pagination**: `?page=0&size=20&sort=name,asc`\n" +
                            "- 🔍 **Rich Filtering**: `?name=laptop&minPrice=100&maxPrice=500`\n" +
                            "- ⏱️ **Enhanced Metadata**: Timestamps, performance metrics\n" +
                            "- 🔗 **Improved HATEOAS**: Complete link relationships\n" +
                            "- 📦 **Bulk Operations**: Batch create, update, delete\n" +
                            "- 🏥 **Version Health**: `/api/v2/products/health`\n\n" +
                            "**Implementation Guide:** See `API_VERSIONING_GUIDE.md` in repository\n\n" +
                            "**Status:** 🔧 Ready for development - Complete architecture prepared"));
                })
                .build();
    }
}