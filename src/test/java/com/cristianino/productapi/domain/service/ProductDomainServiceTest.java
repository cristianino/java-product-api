package com.cristianino.productapi.domain.service;

import com.cristianino.productapi.domain.model.Product;
import com.cristianino.productapi.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDomainServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDomainService productDomainService;

    private Product validProduct;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        validProduct = new Product();
        validProduct.setName("Test Product");
        validProduct.setPrice(new BigDecimal("99.99"));

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");
        savedProduct.setPrice(new BigDecimal("99.99"));
    }

    @Test
    void createProduct_WithValidProduct_ShouldSaveProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productDomainService.createProduct(validProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).save(validProduct);
    }

    @Test
    void createProduct_WithNullName_ShouldThrowException() {
        // Given & When & Then
        assertThatThrownBy(() -> validProduct.setName(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be null or empty");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_WithEmptyName_ShouldThrowException() {
        // Given & When & Then
        assertThatThrownBy(() -> validProduct.setName(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be null or empty");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_WithBlankName_ShouldThrowException() {
        // Given & When & Then
        assertThatThrownBy(() -> validProduct.setName("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be null or empty");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_WithNullPrice_ShouldThrowException() {
        // Given & When & Then
        assertThatThrownBy(() -> validProduct.setPrice(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product price cannot be null");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_WithNegativePrice_ShouldThrowException() {
        // Given & When & Then
        assertThatThrownBy(() -> validProduct.setPrice(new BigDecimal("-10.00")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product price cannot be negative");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_WithZeroPrice_ShouldSaveProduct() {
        // Given
        validProduct.setPrice(BigDecimal.ZERO);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productDomainService.createProduct(validProduct);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).save(validProduct);
    }

    @Test
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(savedProduct));

        // When
        Optional<Product> result = productDomainService.getProductById(productId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(productId);
        verify(productRepository).findById(productId);
    }

    @Test
    void getProductById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productDomainService.getProductById(productId);

        // Then
        assertThat(result).isEmpty();
        verify(productRepository).findById(productId);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal("49.99"));

        List<Product> products = Arrays.asList(savedProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productDomainService.getAllProducts();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(savedProduct, product2);
        verify(productRepository).findAll();
    }

    @Test
    void updateProduct_WithExistingId_ShouldUpdateProduct() {
        // Given
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(new BigDecimal("199.99"));

        Product savedUpdatedProduct = new Product();
        savedUpdatedProduct.setId(productId);
        savedUpdatedProduct.setName("Updated Product");
        savedUpdatedProduct.setPrice(new BigDecimal("199.99"));

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(savedUpdatedProduct);

        // When
        Product result = productDomainService.updateProduct(productId, updatedProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(updatedProduct.getId()).isEqualTo(productId); // Verify ID was set
        verify(productRepository).existsById(productId);
        verify(productRepository).save(updatedProduct);
    }

    @Test
    void updateProduct_WithNonExistingId_ShouldReturnNull() {
        // Given
        Long productId = 999L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When
        Product result = productDomainService.updateProduct(productId, validProduct);

        // Then
        assertThat(result).isNull();
        verify(productRepository).existsById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_WithInvalidProduct_ShouldThrowException() {
        // Given
        Product invalidProduct = new Product();
        invalidProduct.setName("Valid Name");
        invalidProduct.setPrice(new BigDecimal("99.99"));

        // When & Then
        assertThatThrownBy(() -> invalidProduct.setName(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be null or empty");

        verify(productRepository, never()).existsById(any());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WithExistingId_ShouldReturnTrue() {
        // Given
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        // When
        boolean result = productDomainService.deleteProduct(productId);

        // Then
        assertThat(result).isTrue();
        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_WithNonExistingId_ShouldReturnFalse() {
        // Given
        Long productId = 999L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When
        boolean result = productDomainService.deleteProduct(productId);

        // Then
        assertThat(result).isFalse();
        verify(productRepository).existsById(productId);
        verify(productRepository, never()).deleteById(any());
    }
}