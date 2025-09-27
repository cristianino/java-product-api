package com.cristianino.productapi.infrastructure.web;

import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.application.usecase.ProductUseCase;
import com.cristianino.productapi.infrastructure.web.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerUnitTest {

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createProduct_Success() throws Exception {
        // Given
        ProductDto inputProduct = createTestProductDto("Test Product", new BigDecimal("99.99"));
        ProductDto createdProduct = createTestProductDto("Test Product", new BigDecimal("99.99"));
        createdProduct.setId("1");

        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(inputProduct);

        when(productUseCase.createProduct(any(ProductDto.class))).thenReturn(createdProduct);

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product"))
                .andExpect(jsonPath("$.data.attributes.price").value(99.99));

        verify(productUseCase, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void createProduct_BadRequest() throws Exception {
        // Given
        ProductDto inputProduct = createTestProductDto("Test Product", new BigDecimal("99.99"));
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(inputProduct);

        when(productUseCase.createProduct(any(ProductDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid product data"));

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].status").value("400"))
                .andExpect(jsonPath("$.errors[0].title").value("Bad Request"));

        verify(productUseCase, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void getProduct_Found() throws Exception {
        // Given
        Long productId = 1L;
        ProductDto product = createTestProductDto("Test Product", new BigDecimal("99.99"));
        product.setId("1");

        when(productUseCase.getProductById(productId)).thenReturn(Optional.of(product));

        // When & Then
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.attributes.name").value("Test Product"));

        verify(productUseCase, times(1)).getProductById(productId);
    }

    @Test
    void getProduct_NotFound() throws Exception {
        // Given
        Long productId = 1L;

        when(productUseCase.getProductById(productId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productUseCase, times(1)).getProductById(productId);
    }

    @Test
    void getAllProducts_Success() throws Exception {
        // Given
        ProductDto product1 = createTestProductDto("Product 1", new BigDecimal("10.00"));
        product1.setId("1");
        ProductDto product2 = createTestProductDto("Product 2", new BigDecimal("20.00"));
        product2.setId("2");
        List<ProductDto> products = Arrays.asList(product1, product2);

        when(productUseCase.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.meta.count").value(2));

        verify(productUseCase, times(1)).getAllProducts();
    }

    @Test
    void updateProduct_Success() throws Exception {
        // Given
        Long productId = 1L;
        ProductDto inputProduct = createTestProductDto("Updated Product", new BigDecimal("199.99"));
        ProductDto updatedProduct = createTestProductDto("Updated Product", new BigDecimal("199.99"));
        updatedProduct.setId("1");

        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(inputProduct);

        when(productUseCase.updateProduct(eq(productId), any(ProductDto.class)))
                .thenReturn(Optional.of(updatedProduct));

        // When & Then
        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.attributes.name").value("Updated Product"));

        verify(productUseCase, times(1)).updateProduct(eq(productId), any(ProductDto.class));
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        // Given
        Long productId = 1L;
        ProductDto inputProduct = createTestProductDto("Updated Product", new BigDecimal("199.99"));
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(inputProduct);

        when(productUseCase.updateProduct(eq(productId), any(ProductDto.class)))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(productUseCase, times(1)).updateProduct(eq(productId), any(ProductDto.class));
    }

    @Test
    void updateProduct_BadRequest() throws Exception {
        // Given
        Long productId = 1L;
        ProductDto inputProduct = createTestProductDto("Updated Product", new BigDecimal("199.99"));
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(inputProduct);

        when(productUseCase.updateProduct(eq(productId), any(ProductDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // When & Then
        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productUseCase, times(1)).updateProduct(eq(productId), any(ProductDto.class));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        // Given
        Long productId = 1L;

        when(productUseCase.deleteProduct(productId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productUseCase, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        // Given
        Long productId = 1L;

        when(productUseCase.deleteProduct(productId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productUseCase, times(1)).deleteProduct(productId);
    }

    private ProductDto createTestProductDto(String name, BigDecimal price) {
        ProductDto product = new ProductDto();
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes();
        attributes.setName(name);
        attributes.setPrice(price);
        product.setAttributes(attributes);
        return product;
    }
}