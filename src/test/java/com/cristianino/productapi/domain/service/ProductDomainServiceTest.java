package com.cristianino.productapi.domain.service;

import com.cristianino.productapi.domain.model.Product;
import com.cristianino.productapi.domain.port.ProductRepository;
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
class ProductDomainServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductDomainService productDomainService;

    @BeforeEach
    void setUp() {
        productDomainService = new ProductDomainService(productRepository);
    }

    @Test
    void createProduct_ValidProduct_ReturnsCreatedProduct() {
        // Given
        Product product = new Product("Test Product", new BigDecimal("99.99"));
        Product savedProduct = new Product(1L, "Test Product", new BigDecimal("99.99"));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productDomainService.createProduct(product);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void createProduct_InvalidName_ThrowsException() {
        // Given
        Product product = new Product("", new BigDecimal("99.99"));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productDomainService.createProduct(product)
        );
        assertEquals("Product name cannot be null or empty", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProduct_NegativePrice_ThrowsException() {
        // Given
        Product product = new Product("Test Product", new BigDecimal("-10.00"));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productDomainService.createProduct(product)
        );
        assertEquals("Product price must be non-negative", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() {
        // Given
        Long id = 1L;
        Product product = new Product(id, "Test Product", new BigDecimal("99.99"));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // When
        Optional<Product> result = productDomainService.getProductById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getProductById_NonExistingId_ReturnsEmpty() {
        // Given
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productDomainService.getProductById(id);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getAllProducts_ReturnsAllProducts() {
        // Given
        List<Product> products = List.of(
            new Product(1L, "Product 1", new BigDecimal("10.00")),
            new Product(2L, "Product 2", new BigDecimal("20.00"))
        );
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productDomainService.getAllProducts();

        // Then
        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void updateProduct_ExistingProduct_ReturnsUpdatedProduct() {
        // Given
        Long id = 1L;
        Product product = new Product("Updated Product", new BigDecimal("199.99"));
        Product savedProduct = new Product(id, "Updated Product", new BigDecimal("199.99"));
        
        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productDomainService.updateProduct(id, product);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals(new BigDecimal("199.99"), result.getPrice());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_NonExistingProduct_ReturnsNull() {
        // Given
        Long id = 999L;
        Product product = new Product("Updated Product", new BigDecimal("199.99"));
        when(productRepository.existsById(id)).thenReturn(false);

        // When
        Product result = productDomainService.updateProduct(id, product);

        // Then
        assertNull(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_ExistingProduct_ReturnsTrue() {
        // Given
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = productDomainService.deleteProduct(id);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteProduct_NonExistingProduct_ReturnsFalse() {
        // Given
        Long id = 999L;
        when(productRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = productDomainService.deleteProduct(id);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, never()).deleteById(any());
    }
}