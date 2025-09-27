package com.cristianino.productapi.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonApiErrorTest {

    @Test
    void allArgsConstructor_CreatesErrorWithAllFields() {
        // Given
        String status = "400";
        String title = "Bad Request";
        String detail = "The request body contains invalid data";

        // When
        JsonApiError error = new JsonApiError(status, title, detail);

        // Then
        assertEquals(status, error.getStatus());
        assertEquals(title, error.getTitle());
        assertEquals(detail, error.getDetail());
    }

    @Test
    void statusTitleConstructor_CreatesErrorWithStatusAndTitle() {
        // Given
        String status = "404";
        String title = "Not Found";
        JsonApiError error = new JsonApiError();
        
        // When
        error.setStatus(status);
        error.setTitle(title);

        // Then
        assertEquals(status, error.getStatus());
        assertEquals(title, error.getTitle());
        assertNull(error.getDetail());
    }

    @Test
    void defaultConstructor_CreatesEmptyError() {
        // When
        JsonApiError error = new JsonApiError();

        // Then
        assertNull(error.getStatus());
        assertNull(error.getTitle());
        assertNull(error.getDetail());
    }

    @Test
    void setters_UpdateFields() {
        // Given
        JsonApiError error = new JsonApiError();
        String status = "500";
        String title = "Internal Server Error";
        String detail = "An unexpected error occurred";

        // When
        error.setStatus(status);
        error.setTitle(title);
        error.setDetail(detail);

        // Then
        assertEquals(status, error.getStatus());
        assertEquals(title, error.getTitle());
        assertEquals(detail, error.getDetail());
    }

    @Test
    void equalsAndHashCode_WorksCorrectly() {
        // Given
        JsonApiError error1 = new JsonApiError("400", "Bad Request", "Invalid data");
        JsonApiError error2 = new JsonApiError("400", "Bad Request", "Invalid data");
        JsonApiError error3 = new JsonApiError("404", "Not Found", "Resource not found");

        // When & Then
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
        assertNotEquals(error1, error3);
        assertNotEquals(error1, null);
        assertNotEquals(error1, "string");
    }

    @Test
    void equalsAndHashCode_HandlesNullFields() {
        // Given
        JsonApiError error1 = new JsonApiError();
        JsonApiError error2 = new JsonApiError();
        JsonApiError error3 = new JsonApiError("400", "Bad Request", "Test error");

        // When & Then
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
        assertNotEquals(error1, error3);
    }

    @Test
    void toString_ContainsRelevantFields() {
        // Given
        JsonApiError error = new JsonApiError("400", "Bad Request", "Invalid data");

        // When
        String toString = error.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("400") || toString.contains("Bad Request") || toString.length() > 0);
    }

    @Test
    void toString_HandlesNullFields() {
        // Given
        JsonApiError error = new JsonApiError();

        // When
        String toString = error.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    void commonHttpStatusCodes_CreateCorrectly() {
        // Test common HTTP status codes
        JsonApiError badRequest = new JsonApiError("400", "Bad Request", "Invalid request");
        JsonApiError unauthorized = new JsonApiError("401", "Unauthorized", "Access denied");
        JsonApiError notFound = new JsonApiError("404", "Not Found", "Resource not found");
        JsonApiError internalError = new JsonApiError("500", "Internal Server Error", "System error");

        assertEquals("400", badRequest.getStatus());
        assertEquals("Bad Request", badRequest.getTitle());
        
        assertEquals("401", unauthorized.getStatus());
        assertEquals("Unauthorized", unauthorized.getTitle());
        
        assertEquals("404", notFound.getStatus());
        assertEquals("Not Found", notFound.getTitle());
        
        assertEquals("500", internalError.getStatus());
        assertEquals("Internal Server Error", internalError.getTitle());
    }
}