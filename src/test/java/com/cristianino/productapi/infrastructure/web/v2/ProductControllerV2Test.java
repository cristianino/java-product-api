package com.cristianino.productapi.infrastructure.web.v2;

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

@WebMvcTest(ProductControllerV2.class)
class ProductControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductUseCase productUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_KEY = "your-secret-api-key-here";

    @Test
    void shouldCreateProductV2WithEnhancedResponse() throws Exception {
        // Given
        ProductDto productDto = new ProductDto(1L, "Test Product V2", new BigDecimal("99.99"));
        when(productUseCase.createProduct(any(ProductDto.class))).thenReturn(productDto);

        String requestBody = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Test Product V2",
                        "price": 99.99
                    }
                }
            }""";

        // When & Then
        mockMvc.perform(post("/api/v2/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product V2"))
                .andExpect(jsonPath("$.links.self").value("/api/v2/products/1"))
                .andExpect(jsonPath("$.links.edit").value("/api/v2/products/1"))
                .andExpect(jsonPath("$.links.collection").value("/api/v2/products"))
                .andExpected(jsonPath("$.meta.version").value("2.0"))
                .andExpect(jsonPath("$.meta.api_version").value("v2"))
                .andExpect(jsonPath("$.meta.created_at").exists());
    }

    @Test
    void shouldGetProductByIdV2WithEnhancedMetadata() throws Exception {
        // Given
        ProductDto productDto = new ProductDto(1L, "Test Product V2", new BigDecimal("99.99"));
        when(productUseCase.getProductById(1L)).thenReturn(Optional.of(productDto));

        // When & Then
        mockMvc.perform(get("/api/v2/products/1")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product V2"))
                .andExpect(jsonPath("$.links.self").value("/api/v2/products/1"))
                .andExpect(jsonPath("$.links.edit").value("/api/v2/products/1"))
                .andExpected(jsonPath("$.links.delete").value("/api/v2/products/1"))
                .andExpected(jsonPath("$.links.collection").value("/api/v2/products"))
                .andExpect(jsonPath("$.meta.version").value("2.0"))
                .andExpect(jsonPath("$.meta.api_version").value("v2"))
                .andExpect(jsonPath("$.meta.retrieved_at").exists());
    }

    @Test
    void shouldGetAllProductsV2WithPagination() throws Exception {
        // Given
        List<ProductDto> products = List.of(
            new ProductDto(1L, "Product 1 V2", new BigDecimal("99.99")),
            new ProductDto(2L, "Product 2 V2", new BigDecimal("199.99"))
        );
        when(productUseCase.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/v2/products")
                .param("page", "0")
                .param("size", "20")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.data").isArray())
                .andExpected(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.links.self").value("/api/v2/products?page=0&size=20"))
                .andExpected(jsonPath("$.links.first").value("/api/v2/products?page=0&size=20"))
                .andExpected(jsonPath("$.meta.version").value("2.0"))
                .andExpect(jsonPath("$.meta.page").value(0))
                .andExpect(jsonPath("$.meta.size").value(20))
                .andExpect(jsonPath("$.meta.total_pages").exists())
                .andExpect(jsonPath("$.meta.features").isArray())
                .andExpected(jsonPath("$.meta.api_version").value("v2"));
    }

    @Test
    void shouldDeleteProductV2WithConfirmation() throws Exception {
        // Given
        when(productUseCase.deleteProduct(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v2/products/1")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.deleted").value(true))
                .andExpected(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.version").value("2.0"))
                .andExpect(jsonPath("$.deleted_at").exists())
                .andExpected(jsonPath("$.message").value("Product successfully deleted"));
    }

    @Test
    void shouldReturnHealthCheckV2() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v2/products/health")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpected(jsonPath("$.version").value("2.0"))
                .andExpected(jsonPath("$.api_version").value("v2"))
                .andExpected(jsonPath("$.features").isArray())
                .andExpected(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn404WithEnhancedErrorV2() throws Exception {
        // Given
        when(productUseCase.getProductById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v2/products/999")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isNotFound());
    }
}