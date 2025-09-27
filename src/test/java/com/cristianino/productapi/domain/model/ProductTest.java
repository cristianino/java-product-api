package com.cristianino.productapi.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void constructor_ValidNameAndPrice_CreatesProduct() {
        // Given
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");

        // When
        Product product = new Product(name, price);

        // Then
        assertNotNull(product);
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertNull(product.getId());
    }

    @Test
    void constructor_WithId_CreatesProduct() {
        // Given
        Long id = 1L;
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");

        // When
        Product product = new Product(id, name, price);

        // Then
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void constructor_InvalidName_ThrowsException(String invalidName) {
        // Given
        BigDecimal price = new BigDecimal("99.99");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Product(invalidName, price)
        );
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_NullPrice_ThrowsException() {
        // Given
        String name = "Test Product";

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Product(name, null)
        );
        assertEquals("Product price cannot be null", exception.getMessage());
    }

    @Test
    void constructor_NegativePrice_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Product(null, "Test Product", BigDecimal.valueOf(-10.0))
        );
        assertEquals("Product price cannot be negative", exception.getMessage());
    }

    @Test
    void constructor_ZeroPrice_CreatesProduct() {
        // Given
        String name = "Free Product";
        BigDecimal zeroPrice = BigDecimal.ZERO;

        // When
        Product product = new Product(name, zeroPrice);

        // Then
        assertNotNull(product);
        assertEquals(name, product.getName());
        assertEquals(zeroPrice, product.getPrice());
    }

    @Test
    void equals_SameProducts_ReturnsTrue() {
        // Given
        Product product1 = new Product(1L, "Test Product", new BigDecimal("99.99"));
        Product product2 = new Product(1L, "Test Product", new BigDecimal("99.99"));

        // When & Then
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void equals_DifferentId_ReturnsFalse() {
        // Given
        Product product1 = new Product(1L, "Test Product", new BigDecimal("99.99"));
        Product product2 = new Product(2L, "Test Product", new BigDecimal("99.99"));

        // When & Then
        assertNotEquals(product1, product2);
    }

    @Test
    void equals_DifferentName_ReturnsFalse() {
        // Given
        Product product1 = new Product(1L, "Product A", new BigDecimal("99.99"));
        Product product2 = new Product(1L, "Product B", new BigDecimal("99.99"));

        // When & Then
        assertNotEquals(product1, product2);
    }

    @Test
    void equals_DifferentPrice_ReturnsFalse() {
        // Given
        Product product1 = new Product(1L, "Test Product", new BigDecimal("99.99"));
        Product product2 = new Product(1L, "Test Product", new BigDecimal("89.99"));

        // When & Then
        assertNotEquals(product1, product2);
    }

    @Test
    void equals_NullObject_ReturnsFalse() {
        // Given
        Product product = new Product(1L, "Test Product", new BigDecimal("99.99"));

        // When & Then
        assertNotEquals(product, null);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        // Given
        Product product = new Product(1L, "Test Product", new BigDecimal("99.99"));
        String differentObject = "Not a product";

        // When & Then
        assertNotEquals(product, differentObject);
    }

    @Test
    void equals_SameReference_ReturnsTrue() {
        // Given
        Product product = new Product(1L, "Test Product", new BigDecimal("99.99"));

        // When & Then
        assertEquals(product, product);
    }

    @Test
    void toString_ContainsAllFields() {
        // Given
        Product product = new Product(1L, "Test Product", new BigDecimal("99.99"));

        // When
        String toString = product.toString();

        // Then
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("99.99"));
    }

    @Test
    void setters_UpdateFields() {
        // Given
        Product product = new Product("Original", new BigDecimal("10.00"));

        // When
        product.setName("Updated Product");
        product.setPrice(new BigDecimal("20.00"));

        // Then
        assertEquals("Updated Product", product.getName());
        assertEquals(new BigDecimal("20.00"), product.getPrice());
    }

    @Test
    void setName_InvalidName_ThrowsException() {
        // Given
        Product product = new Product("Valid Name", new BigDecimal("10.00"));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> product.setName("")
        );
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void setPrice_InvalidPrice_ThrowsException() {
        // Given
        Product product = new Product(1L, "Test Product", BigDecimal.valueOf(10.0));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> product.setPrice(BigDecimal.valueOf(-5.0))
        );
        assertEquals("Product price cannot be negative", exception.getMessage());
    }
}