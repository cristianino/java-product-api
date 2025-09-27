package com.cristianino.productapi.application.usecase;

import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.domain.model.Product;
import com.cristianino.productapi.domain.service.ProductDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductDomainService productDomainService;

    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        productUseCase = new ProductUseCase(productDomainService);
    }

    @Test
    void createProduct_ValidProductDto_ReturnsProductDto() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));
        ProductDto inputDto = new ProductDto();
        inputDto.setAttributes(attributes);

        Product savedProduct = new Product(1L, "Test Product", new BigDecimal("99.99"));
        when(productDomainService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductDto result = productUseCase.createProduct(inputDto);

        // Then
        assertNotNull(result);
        assertEquals("products", result.getType());
        assertEquals("1", result.getId());
        assertEquals("Test Product", result.getAttributes().getName());
        assertEquals(new BigDecimal("99.99"), result.getAttributes().getPrice());
        
        verify(productDomainService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void createProduct_NullDto_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productUseCase.createProduct(null)
        );
        assertEquals("ProductDto cannot be null", exception.getMessage());
        verify(productDomainService, never()).createProduct(any());
    }

    @Test
    void createProduct_NullAttributes_ThrowsException() {
        // Given
        ProductDto inputDto = new ProductDto();
        inputDto.setAttributes(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productUseCase.createProduct(inputDto)
        );
        assertEquals("ProductDto attributes cannot be null", exception.getMessage());
        verify(productDomainService, never()).createProduct(any());
    }

    @Test
    void getProductById_ExistingId_ReturnsProductDto() {
        // Given
        Long id = 1L;
        Product product = new Product(id, "Test Product", new BigDecimal("99.99"));
        when(productDomainService.getProductById(id)).thenReturn(Optional.of(product));

        // When
        Optional<ProductDto> result = productUseCase.getProductById(id);

        // Then
        assertTrue(result.isPresent());
        ProductDto dto = result.get();
        assertEquals("products", dto.getType());
        assertEquals("1", dto.getId());
        assertEquals("Test Product", dto.getAttributes().getName());
        assertEquals(new BigDecimal("99.99"), dto.getAttributes().getPrice());
        
        verify(productDomainService, times(1)).getProductById(id);
    }

    @Test
    void getProductById_NonExistingId_ReturnsEmpty() {
        // Given
        Long id = 999L;
        when(productDomainService.getProductById(id)).thenReturn(Optional.empty());

        // When
        Optional<ProductDto> result = productUseCase.getProductById(id);

        // Then
        assertFalse(result.isPresent());
        verify(productDomainService, times(1)).getProductById(id);
    }

    @Test
    void getProductById_NullId_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productUseCase.getProductById(null)
        );
        assertEquals("Product ID cannot be null", exception.getMessage());
        verify(productDomainService, never()).getProductById(any());
    }

    @Test
    void getAllProducts_ReturnsListOfProductDtos() {
        // Given
        List<Product> products = List.of(
            new Product(1L, "Product 1", new BigDecimal("10.00")),
            new Product(2L, "Product 2", new BigDecimal("20.00"))
        );
        when(productDomainService.getAllProducts()).thenReturn(products);

        // When
        List<ProductDto> result = productUseCase.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        ProductDto dto1 = result.get(0);
        assertEquals("products", dto1.getType());
        assertEquals("1", dto1.getId());
        assertEquals("Product 1", dto1.getAttributes().getName());
        assertEquals(new BigDecimal("10.00"), dto1.getAttributes().getPrice());
        
        ProductDto dto2 = result.get(1);
        assertEquals("products", dto2.getType());
        assertEquals("2", dto2.getId());
        assertEquals("Product 2", dto2.getAttributes().getName());
        assertEquals(new BigDecimal("20.00"), dto2.getAttributes().getPrice());
        
        verify(productDomainService, times(1)).getAllProducts();
    }

    @Test
    void getAllProducts_EmptyList_ReturnsEmptyList() {
        // Given
        when(productDomainService.getAllProducts()).thenReturn(List.of());

        // When
        List<ProductDto> result = productUseCase.getAllProducts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productDomainService, times(1)).getAllProducts();
    }

    @Test
    void updateProduct_ValidData_ReturnsUpdatedProductDto() {
        // Given
        Long id = 1L;
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Updated Product", new BigDecimal("199.99"));
        ProductDto inputDto = new ProductDto();
        inputDto.setAttributes(attributes);

        Product updatedProduct = new Product(id, "Updated Product", new BigDecimal("199.99"));
        when(productDomainService.updateProduct(eq(id), any(Product.class))).thenReturn(updatedProduct);

        // When
        Optional<ProductDto> result = productUseCase.updateProduct(id, inputDto);

        // Then
        assertTrue(result.isPresent());
        ProductDto dto = result.get();
        assertEquals("products", dto.getType());
        assertEquals("1", dto.getId());
        assertEquals("Updated Product", dto.getAttributes().getName());
        assertEquals(new BigDecimal("199.99"), dto.getAttributes().getPrice());
        
        verify(productDomainService, times(1)).updateProduct(eq(id), any(Product.class));
    }

    @Test
    void updateProduct_NonExistingId_ReturnsEmpty() {
        // Given
        Long id = 999L;
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Updated Product", new BigDecimal("199.99"));
        ProductDto inputDto = new ProductDto();
        inputDto.setAttributes(attributes);

        when(productDomainService.updateProduct(eq(id), any(Product.class))).thenReturn(null);

        // When
        Optional<ProductDto> result = productUseCase.updateProduct(id, inputDto);

        // Then
        assertFalse(result.isPresent());
        verify(productDomainService, times(1)).updateProduct(eq(id), any(Product.class));
    }

    @Test
    void updateProduct_NullId_ThrowsException() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Updated Product", new BigDecimal("199.99"));
        ProductDto inputDto = new ProductDto();
        inputDto.setAttributes(attributes);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productUseCase.updateProduct(null, inputDto)
        );
        assertEquals("Product ID cannot be null", exception.getMessage());
        verify(productDomainService, never()).updateProduct(any(), any());
    }

    @Test
    void deleteProduct_ExistingId_ReturnsTrue() {
        // Given
        Long id = 1L;
        when(productDomainService.deleteProduct(id)).thenReturn(true);

        // When
        boolean result = productUseCase.deleteProduct(id);

        // Then
        assertTrue(result);
        verify(productDomainService, times(1)).deleteProduct(id);
    }

    @Test
    void deleteProduct_NonExistingId_ReturnsFalse() {
        // Given
        Long id = 999L;
        when(productDomainService.deleteProduct(id)).thenReturn(false);

        // When
        boolean result = productUseCase.deleteProduct(id);

        // Then
        assertFalse(result);
        verify(productDomainService, times(1)).deleteProduct(id);
    }

    @Test
    void deleteProduct_NullId_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productUseCase.deleteProduct(null)
        );
        assertEquals("Product ID cannot be null", exception.getMessage());
        verify(productDomainService, never()).deleteProduct(any());
    }
}