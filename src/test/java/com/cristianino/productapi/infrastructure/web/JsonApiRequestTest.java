package com.cristianino.productapi.infrastructure.web;

import com.cristianino.productapi.application.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonApiRequestTest {

    @Test
    void defaultConstructor_CreatesEmptyRequest() {
        // When
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>();

        // Then
        assertNotNull(request);
        assertNull(request.getData());
    }

    @Test
    void dataConstructor_CreatesRequestWithData() {
        // Given
        ProductDto product = new ProductDto();
        product.setId("1");
        ProductDto.ProductAttributes attributes = new ProductDto.ProductAttributes();
        attributes.setName("Test Product");
        product.setAttributes(attributes);

        // When
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(product);

        // Then
        assertNotNull(request);
        assertEquals(product, request.getData());
    }

    @Test
    void setData_UpdatesDataField() {
        // Given
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>();
        ProductDto product = new ProductDto();
        product.setId("1");

        // When
        request.setData(product);

        // Then
        assertEquals(product, request.getData());
    }

    @Test
    void genericType_WorksWithDifferentTypes() {
        // Given
        String stringData = "test";
        Integer integerData = 42;

        // When
        JsonApiRequest<String> stringRequest = new JsonApiRequest<>(stringData);
        JsonApiRequest<Integer> integerRequest = new JsonApiRequest<>(integerData);

        // Then
        assertEquals(stringData, stringRequest.getData());
        assertEquals(integerData, integerRequest.getData());
    }

    @Test
    void nullData_HandledCorrectly() {
        // When
        JsonApiRequest<ProductDto> request = new JsonApiRequest<>(null);

        // Then
        assertNull(request.getData());
    }
}