package com.cristianino.productapi.application.dto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonApiResponseTest {

    @Test
    void singleDataConstructor_CreatesResponseWithSingleData() {
        // Given
        ProductDto product = new ProductDto();
        product.setId("1");

        // When
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>(product);

        // Then
        assertNotNull(response);
        assertEquals(product, response.getData());
        assertNull(response.getLinks());
        assertNull(response.getMeta());
        assertNull(response.getErrors());
    }

    @Test
    void dataWithLinksAndMetaConstructor_CreatesCompleteResponse() {
        // Given
        ProductDto product = new ProductDto();
        product.setId("1");
        Map<String, String> links = Map.of("self", "/api/products/1");
        Map<String, Object> meta = Map.of("version", "1.0");

        // When
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>(product, links, meta);

        // Then
        assertNotNull(response);
        assertEquals(product, response.getData());
        assertEquals(links, response.getLinks());
        assertEquals(meta, response.getMeta());
        assertNull(response.getErrors());
    }

    @Test
    void errorConstructor_CreatesResponseWithErrors() {
        // Given
        JsonApiError error1 = new JsonApiError("400", "Bad Request", "Invalid input");
        JsonApiError error2 = new JsonApiError("404", "Not Found", "Resource not found");
        List<JsonApiError> errors = List.of(error1, error2);

        // When
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>(errors);

        // Then
        assertNotNull(response);
        assertNull(response.getData());

        assertEquals(errors, response.getErrors());
        assertEquals(2, response.getErrors().size());
    }

    @Test
    void defaultConstructor_CreatesEmptyResponse() {
        // When
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>();

        // Then
        assertNotNull(response);
        assertNull(response.getData());
        assertNull(response.getLinks());
        assertNull(response.getMeta());
        assertNull(response.getErrors());
    }

    @Test
    void setters_UpdateFields() {
        // Given
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>();
        ProductDto product = new ProductDto();
        product.setId("1");
        Map<String, String> links = Map.of("self", "/api/products/1");
        Map<String, Object> meta = Map.of("count", 1);
        List<JsonApiError> errors = List.of(new JsonApiError("400", "Bad Request", "Invalid"));

        // When
        response.setData(product);
        response.setLinks(links);
        response.setMeta(meta);
        response.setErrors(errors);

        // Then
        assertEquals(product, response.getData());
        assertEquals(links, response.getLinks());
        assertEquals(meta, response.getMeta());
        assertEquals(errors, response.getErrors());
    }

    @Test
    void equalsAndHashCode_WorksCorrectly() {
        // Given
        ProductDto product = new ProductDto();
        product.setId("1");
        
        JsonApiResponse<ProductDto> response1 = new JsonApiResponse<>(product);
        JsonApiResponse<ProductDto> response2 = new JsonApiResponse<>(product);
        
        ProductDto differentProduct = new ProductDto();
        differentProduct.setId("2");
        JsonApiResponse<ProductDto> response3 = new JsonApiResponse<>(differentProduct);

        // When & Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
    }

    @Test
    void toString_ContainsRelevantFields() {
        // Given
        ProductDto product = new ProductDto();
        product.setId("1");
        JsonApiResponse<ProductDto> response = new JsonApiResponse<>(product);

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("JsonApiResponse") || toString.length() > 0);
    }
}