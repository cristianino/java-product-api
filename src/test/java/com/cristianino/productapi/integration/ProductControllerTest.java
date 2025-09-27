package com.cristianino.productapi.integration;

import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.infrastructure.web.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_KEY = "test-api-key";

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
    void getAllProducts_ReturnsProductsList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                        .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.links.self").value("/api/products"))
                .andExpect(jsonPath("$.meta.count").exists());
    }
}