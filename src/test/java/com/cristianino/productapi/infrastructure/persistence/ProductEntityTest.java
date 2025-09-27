package com.cristianino.productapi.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityTest {

    @Test
    void defaultConstructor_CreatesEmptyEntity() {
        // When
        ProductEntity entity = new ProductEntity();

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getPrice());
    }

    @Test
    void nameAndPriceConstructor_CreatesEntityWithoutId() {
        // Given
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");

        // When
        ProductEntity entity = new ProductEntity(name, price);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(price, entity.getPrice());
    }

    @Test
    void allArgsConstructor_CreatesCompleteEntity() {
        // Given
        Long id = 1L;
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");

        // When
        ProductEntity entity = new ProductEntity(id, name, price);

        // Then
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(price, entity.getPrice());
    }

    @Test
    void setters_UpdateFields() {
        // Given
        ProductEntity entity = new ProductEntity();
        Long id = 1L;
        String name = "Updated Product";
        BigDecimal price = new BigDecimal("199.99");

        // When
        entity.setId(id);
        entity.setName(name);
        entity.setPrice(price);

        // Then
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(price, entity.getPrice());
    }

    @Test
    void getters_ReturnCorrectValues() {
        // Given
        Long id = 1L;
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");
        ProductEntity entity = new ProductEntity(id, name, price);

        // When & Then
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(price, entity.getPrice());
    }

    @Test
    void priceHandling_WorksWithDifferentPrecisions() {
        // Given
        ProductEntity entity = new ProductEntity();

        // When & Then
        BigDecimal price1 = new BigDecimal("10.50");
        entity.setPrice(price1);
        assertEquals(price1, entity.getPrice());

        BigDecimal price2 = new BigDecimal("999999.99");
        entity.setPrice(price2);
        assertEquals(price2, entity.getPrice());

        BigDecimal price3 = new BigDecimal("0.01");
        entity.setPrice(price3);
        assertEquals(price3, entity.getPrice());
    }

    @Test
    void nullValues_HandledCorrectly() {
        // Given
        ProductEntity entity = new ProductEntity();

        // When
        entity.setId(null);
        entity.setName(null);
        entity.setPrice(null);

        // Then
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getPrice());
    }
}