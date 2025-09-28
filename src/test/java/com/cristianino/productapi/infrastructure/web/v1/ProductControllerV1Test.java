package com.cristianino.productapi.infrastructure.web.v1;

import com.cristianino.productapi.application.usecase.ProductUseCase;
import com.cristianino.productapi.application.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControllerV1.class)
class ProductControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductUseCase productUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_KEY = "test-api-key";

    @Test
    void shouldCreateProductV1() throws Exception {
        // Given
        ProductDto productDto = new ProductDto("1", "Test Product", new BigDecimal("99.99"));
        when(productUseCase.createProduct(any(ProductDto.class))).thenReturn(productDto);

        String requestBody = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Test Product",
                        "price": 99.99
                    }
                }
            }""";

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product"))
                .andExpect(jsonPath("$.data.attributes.price").value(99.99))
                .andExpect(jsonPath("$.meta.version").value("1.0"));
    }

    @Test
    void shouldGetProductByIdV1() throws Exception {
        // Given
        ProductDto productDto = new ProductDto("1", "Test Product", new BigDecimal("99.99"));
        when(productUseCase.getProductById(1L)).thenReturn(Optional.of(productDto));

        // When & Then
        mockMvc.perform(get("/api/v1/products/1")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product"))
                .andExpect(jsonPath("$.links.self").value("/api/v1/products/1"))
                .andExpect(jsonPath("$.meta.version").value("1.0"));
    }

    @Test
    void shouldGetAllProductsV1() throws Exception {
        // Given
        List<ProductDto> products = List.of(
            new ProductDto("1", "Product 1", new BigDecimal("99.99")),
            new ProductDto("2", "Product 2", new BigDecimal("199.99"))
        );
        when(productUseCase.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.links.self").value("/api/v1/products"))
                .andExpect(jsonPath("$.meta.version").value("1.0"))
                .andExpect(jsonPath("$.meta.count").value(2));
    }

    @Test
    void shouldUpdateProductV1() throws Exception {
        // Given
        ProductDto updatedProduct = new ProductDto("1", "Updated Product", new BigDecimal("149.99"));
        when(productUseCase.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(Optional.of(updatedProduct));

        String requestBody = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Updated Product",
                        "price": 149.99
                    }
                }
            }""";

        // When & Then
        mockMvc.perform(put("/api/v1/products/1")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.name").value("Updated Product"));
    }

    @Test
    void shouldDeleteProductV1() throws Exception {
        // Given
        when(productUseCase.deleteProduct(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v1/products/1")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenProductNotFoundV1() throws Exception {
        // Given
        when(productUseCase.getProductById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/products/999")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isNotFound());
    }
}