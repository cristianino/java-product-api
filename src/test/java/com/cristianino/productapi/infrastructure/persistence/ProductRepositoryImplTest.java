package com.cristianino.productapi.infrastructure.persistence;

import com.cristianino.productapi.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductRepositoryImplTest {

    @Mock
    private ProductJpaRepository jpaRepository;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_NewProduct_ReturnsProductWithId() {
        // Given
        Product inputProduct = new Product(null, "Test Product", new BigDecimal("99.99"));
        ProductEntity savedEntity = new ProductEntity(1L, "Test Product", new BigDecimal("99.99"));
        
        when(jpaRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        // When
        Product result = productRepository.save(inputProduct);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        
        verify(jpaRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void save_ExistingProduct_ReturnsUpdatedProduct() {
        // Given
        Product inputProduct = new Product(1L, "Updated Product", new BigDecimal("199.99"));
        ProductEntity savedEntity = new ProductEntity(1L, "Updated Product", new BigDecimal("199.99"));
        
        when(jpaRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        // When
        Product result = productRepository.save(inputProduct);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals(new BigDecimal("199.99"), result.getPrice());
        
        verify(jpaRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void findById_ExistingProduct_ReturnsProduct() {
        // Given
        Long productId = 1L;
        ProductEntity entity = new ProductEntity(productId, "Test Product", new BigDecimal("99.99"));
        
        when(jpaRepository.findById(productId)).thenReturn(Optional.of(entity));

        // When
        Optional<Product> result = productRepository.findById(productId);

        // Then
        assertTrue(result.isPresent());
        Product product = result.get();
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        
        verify(jpaRepository, times(1)).findById(productId);
    }

    @Test
    void findById_NonExistingProduct_ReturnsEmpty() {
        // Given
        Long productId = 1L;
        
        when(jpaRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productRepository.findById(productId);

        // Then
        assertFalse(result.isPresent());
        
        verify(jpaRepository, times(1)).findById(productId);
    }

    @Test
    void findAll_MultipleProducts_ReturnsAllProducts() {
        // Given
        ProductEntity entity1 = new ProductEntity(1L, "Product 1", new BigDecimal("10.00"));
        ProductEntity entity2 = new ProductEntity(2L, "Product 2", new BigDecimal("20.00"));
        List<ProductEntity> entities = Arrays.asList(entity1, entity2);
        
        when(jpaRepository.findAll()).thenReturn(entities);

        // When
        List<Product> result = productRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        Product product1 = result.get(0);
        assertEquals(1L, product1.getId());
        assertEquals("Product 1", product1.getName());
        assertEquals(new BigDecimal("10.00"), product1.getPrice());
        
        Product product2 = result.get(1);
        assertEquals(2L, product2.getId());
        assertEquals("Product 2", product2.getName());
        assertEquals(new BigDecimal("20.00"), product2.getPrice());
        
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void findAll_NoProducts_ReturnsEmptyList() {
        // Given
        when(jpaRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Product> result = productRepository.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deleteById_CallsJpaRepositoryDeleteById() {
        // Given
        Long productId = 1L;

        // When
        productRepository.deleteById(productId);

        // Then
        verify(jpaRepository, times(1)).deleteById(productId);
    }

    @Test
    void existsById_ExistingProduct_ReturnsTrue() {
        // Given
        Long productId = 1L;
        
        when(jpaRepository.existsById(productId)).thenReturn(true);

        // When
        boolean result = productRepository.existsById(productId);

        // Then
        assertTrue(result);
        
        verify(jpaRepository, times(1)).existsById(productId);
    }

    @Test
    void existsById_NonExistingProduct_ReturnsFalse() {
        // Given
        Long productId = 1L;
        
        when(jpaRepository.existsById(productId)).thenReturn(false);

        // When
        boolean result = productRepository.existsById(productId);

        // Then
        assertFalse(result);
        
        verify(jpaRepository, times(1)).existsById(productId);
    }
}