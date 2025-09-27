package com.cristianino.productapi.repository;

import com.cristianino.productapi.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct() {
        // Given
        Product product = new Product("Test Product", new BigDecimal("19.99"));

        // When
        Product savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Test Product");
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("19.99"));

        // Verify we can find it
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
    }

    @Test
    void shouldFindAllProducts() {
        // Given
        Product product1 = new Product("Product 1", new BigDecimal("10.00"));
        Product product2 = new Product("Product 2", new BigDecimal("20.00"));
        
        productRepository.save(product1);
        productRepository.save(product2);

        // When
        var products = productRepository.findAll();

        // Then
        assertThat(products).hasSize(2);
    }
}