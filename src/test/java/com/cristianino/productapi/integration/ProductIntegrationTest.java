package com.cristianino.productapi.integration;

import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.infrastructure.web.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class ProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_KEY = "your-secret-api-key-here";

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void createProduct_ValidData_ReturnsCreatedProduct() throws Exception {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));
        ProductDto productDto = new ProductDto();
        productDto.setAttributes(attributes);
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(productDto);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.type").value("products"))
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product"))
                .andExpect(jsonPath("$.data.attributes.price").value(99.99));
    }

    @Test
    void createProduct_WithoutApiKey_ReturnsUnauthorized() throws Exception {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));
        ProductDto productDto = new ProductDto();
        productDto.setAttributes(attributes);
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(productDto);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createProduct_InvalidData_ReturnsBadRequest() throws Exception {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("", new BigDecimal("-10.00"));
        ProductDto productDto = new ProductDto();
        productDto.setAttributes(attributes);
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(productDto);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProducts_ReturnsProductsList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.links.self").value("/api/products"))
                .andExpect(jsonPath("$.meta.count").exists());
    }

    @Test
    void getProduct_ExistingId_ReturnsProduct() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/1")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("products"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.links.self").value("/api/products/1"));
    }

    @Test
    void getProduct_NonExistingId_ReturnsNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/999")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_ExistingId_ReturnsUpdatedProduct() throws Exception {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Updated Product", new BigDecimal("199.99"));
        ProductDto productDto = new ProductDto();
        productDto.setAttributes(attributes);
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(productDto);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.name").value("Updated Product"))
                .andExpect(jsonPath("$.data.attributes.price").value(199.99));
    }

    @Test
    void deleteProduct_ExistingId_ReturnsNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/2")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_NonExistingId_ReturnsNotFound() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/999")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isNotFound());
    }
}