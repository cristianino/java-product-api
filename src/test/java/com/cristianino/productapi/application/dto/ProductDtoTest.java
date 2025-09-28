package com.cristianino.productapi.application.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoTest {

    @Test
    void productDto_DefaultConstructor_CreatesEmptyDto() {
        // When
        ProductDto dto = new ProductDto();

        // Then
        assertNotNull(dto);
        assertEquals("products", dto.getType());
        assertNull(dto.getId());
        assertNull(dto.getAttributes());
    }

    @Test
    void productDto_WithAttributes_SetsAttributesCorrectly() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));
        ProductDto dto = new ProductDto();

        // When
        dto.setAttributes(attributes);

        // Then
        assertNotNull(dto.getAttributes());
        assertEquals("Test Product", dto.getAttributes().getName());
        assertEquals(new BigDecimal("99.99"), dto.getAttributes().getPrice());
    }

    @Test
    void productDto_SetId_UpdatesId() {
        // Given
        ProductDto dto = new ProductDto();

        // When
        dto.setId("123");

        // Then
        assertEquals("123", dto.getId());
    }

    @Test
    void productAttributes_ConstructorWithValidData_CreatesAttributes() {
        // Given
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");

        // When
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes(name, price);

        // Then
        assertNotNull(attributes);
        assertEquals(name, attributes.getName());
        assertEquals(price, attributes.getPrice());
    }

    @Test
    void productAttributes_DefaultConstructor_CreatesEmptyAttributes() {
        // When
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes();

        // Then
        assertNotNull(attributes);
        assertNull(attributes.getName());
        assertNull(attributes.getPrice());
    }

    @Test
    void productAttributes_Setters_UpdateFields() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes();

        // When
        attributes.setName("Updated Product");
        attributes.setPrice(new BigDecimal("199.99"));

        // Then
        assertEquals("Updated Product", attributes.getName());
        assertEquals(new BigDecimal("199.99"), attributes.getPrice());
    }

    @Test
    void productAttributes_EqualsAndHashCode_WorksCorrectly() {
        // Given
        ProductDto.ProductAttributes attributes1 = new ProductDto.ProductAttributes("Product", new BigDecimal("10.00"));
        ProductDto.ProductAttributes attributes2 = new ProductDto.ProductAttributes("Product", new BigDecimal("10.00"));
        ProductDto.ProductAttributes attributes3 = new ProductDto.ProductAttributes("Different", new BigDecimal("20.00"));

        // When & Then
        assertEquals(attributes1, attributes2);
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    void productAttributes_ToString_ContainsFields() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));

        // When
        String toString = attributes.toString();

        // Then
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("99.99"));
    }

    @Test
    void productDto_EqualsAndHashCode_WorksCorrectly() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Product", new BigDecimal("10.00"));
        
        ProductDto dto1 = new ProductDto();
        dto1.setId("1");
        dto1.setAttributes(attributes);
        
        ProductDto dto2 = new ProductDto();
        dto2.setId("1");
        dto2.setAttributes(attributes);
        
        ProductDto dto3 = new ProductDto();
        dto3.setId("2");
        dto3.setAttributes(attributes);

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void productDto_ToString_ContainsFields() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));
        ProductDto dto = new ProductDto();
        dto.setId("1");
        dto.setAttributes(attributes);

        // When
        String toString = dto.toString();

        // Then
        assertTrue(toString.contains("products"));
        assertTrue(toString.contains("1"));
    }

    @Test
    void productAttributes_Equals_SameValues_ReturnsTrue() {
        // Given
        ProductDto.ProductAttributes attr1 = new ProductDto.ProductAttributes("Product", new BigDecimal("50.00"));
        ProductDto.ProductAttributes attr2 = new ProductDto.ProductAttributes("Product", new BigDecimal("50.00"));

        // When & Then
        assertEquals(attr1, attr2);
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    void productAttributes_Equals_DifferentValues_ReturnsFalse() {
        // Given
        ProductDto.ProductAttributes attr1 = new ProductDto.ProductAttributes("Product1", new BigDecimal("50.00"));
        ProductDto.ProductAttributes attr2 = new ProductDto.ProductAttributes("Product2", new BigDecimal("60.00"));

        // When & Then
        assertNotEquals(attr1, attr2);
    }

    @Test
    void productAttributes_Equals_WithNull_ReturnsFalse() {
        // Given
        ProductDto.ProductAttributes attr = new ProductDto.ProductAttributes("Product", new BigDecimal("50.00"));

        // When & Then
        assertNotEquals(attr, null);
        assertNotEquals(null, attr);
    }

    @Test
    void productDto_Equals_SameValues_ReturnsTrue() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test", new BigDecimal("99.99"));
        ProductDto dto1 = new ProductDto("1", "Test", new BigDecimal("99.99"));
        ProductDto dto2 = new ProductDto("1", "Test", new BigDecimal("99.99"));

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void productDto_Equals_DifferentValues_ReturnsFalse() {
        // Given
        ProductDto dto1 = new ProductDto("1", "Test1", new BigDecimal("99.99"));
        ProductDto dto2 = new ProductDto("2", "Test2", new BigDecimal("199.99"));

        // When & Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void productDto_Equals_WithNull_ReturnsFalse() {
        // Given
        ProductDto dto = new ProductDto("1", "Test", new BigDecimal("99.99"));

        // When & Then
        assertNotEquals(dto, null);
    }

    @Test
    void productAttributes_ToString_ContainsAllFields() {
        // Given
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes("Test Product", new BigDecimal("99.99"));

        // When
        String toString = attributes.toString();

        // Then
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("99.99"));
    }
}