package com.cristianino.productapi.application.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonApiLinksBuilderTest {

    @Test
    void builder_CreatesNewInstance() {
        // When
        JsonApiLinksBuilder builder = JsonApiLinksBuilder.builder();
        
        // Then
        assertNotNull(builder);
    }

    @Test
    void self_AddselfLink() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .self("/api/products/1")
                .build();

        // Then
        assertEquals("/api/products/1", links.get("self"));
    }

    @Test
    void collection_AddsCollectionLink() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .collection("/api/products")
                .build();

        // Then
        assertEquals("/api/products", links.get("collection"));
    }

    @Test
    void related_AddsRelatedLink() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .related("/api/products/1/reviews")
                .build();

        // Then
        assertEquals("/api/products/1/reviews", links.get("related"));
    }

    @Test
    void paginationLinks_AddsPaginationLinks() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .first("/api/products?page=1")
                .prev("/api/products?page=1")
                .next("/api/products?page=3")
                .last("/api/products?page=10")
                .build();

        // Then
        assertEquals("/api/products?page=1", links.get("first"));
        assertEquals("/api/products?page=1", links.get("prev"));
        assertEquals("/api/products?page=3", links.get("next"));
        assertEquals("/api/products?page=10", links.get("last"));
    }

    @Test
    void custom_AddsCustomLink() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .custom("edit", "/api/products/1/edit")
                .build();

        // Then
        assertEquals("/api/products/1/edit", links.get("edit"));
    }

    @Test
    void multipleLinks_AddsAllLinks() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .self("/api/products/1")
                .collection("/api/products")
                .related("/api/products/1/reviews")
                .custom("edit", "/api/products/1/edit")
                .build();

        // Then
        assertEquals(4, links.size());
        assertEquals("/api/products/1", links.get("self"));
        assertEquals("/api/products", links.get("collection"));
        assertEquals("/api/products/1/reviews", links.get("related"));
        assertEquals("/api/products/1/edit", links.get("edit"));
    }

    @Test
    void build_ReturnsImmutableCopy() {
        // Given
        JsonApiLinksBuilder builder = JsonApiLinksBuilder.builder()
                .self("/api/products/1");

        // When
        Map<String, String> links1 = builder.build();
        Map<String, String> links2 = builder.collection("/api/products").build();

        // Then
        assertEquals(1, links1.size());
        assertEquals(2, links2.size());
        assertNotSame(links1, links2);
    }

    @Test
    void absoluteUrl_PreservesAbsoluteUrls() {
        // When
        Map<String, String> links = JsonApiLinksBuilder.builder()
                .self("https://api.example.com/products/1")
                .build();

        // Then
        assertEquals("https://api.example.com/products/1", links.get("self"));
    }
}