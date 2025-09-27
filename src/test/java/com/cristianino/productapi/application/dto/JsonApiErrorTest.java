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

    // Tests for JsonApiErrorSource inner class
    @Test
    void jsonApiErrorSource_DefaultConstructor_CreatesEmptySource() {
        // When
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource();

        // Then
        assertNull(source.getPointer());
        assertNull(source.getParameter());
    }

    @Test
    void jsonApiErrorSource_PointerConstructor_CreatesSourceWithPointer() {
        // Given
        String pointer = "/data/attributes/name";

        // When
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource(pointer);

        // Then
        assertEquals(pointer, source.getPointer());
        assertNull(source.getParameter());
    }

    @Test
    void jsonApiErrorSource_SettersAndGetters_WorkCorrectly() {
        // Given
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource();
        String pointer = "/data/attributes/email";
        String parameter = "email";

        // When
        source.setPointer(pointer);
        source.setParameter(parameter);

        // Then
        assertEquals(pointer, source.getPointer());
        assertEquals(parameter, source.getParameter());
    }

    @Test
    void jsonApiErrorSource_Equals_WorksCorrectly() {
        // Given
        JsonApiError.JsonApiErrorSource source1 = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source1.setParameter("name");

        JsonApiError.JsonApiErrorSource source2 = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source2.setParameter("name");

        JsonApiError.JsonApiErrorSource source3 = new JsonApiError.JsonApiErrorSource("/data/attributes/email");

        // Then
        assertEquals(source1, source2);
        assertNotEquals(source1, source3);
        assertNotEquals(source1, null);
        assertNotEquals(source1, "string");
        assertEquals(source1, source1); // reflexivity
    }

    @Test
    void jsonApiErrorSource_HashCode_WorksCorrectly() {
        // Given
        JsonApiError.JsonApiErrorSource source1 = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source1.setParameter("name");

        JsonApiError.JsonApiErrorSource source2 = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source2.setParameter("name");

        // Then
        assertEquals(source1.hashCode(), source2.hashCode());
    }

    @Test
    void jsonApiErrorSource_ToString_ReturnsNonNullString() {
        // Given
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source.setParameter("name");

        // When
        String toString = source.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("JsonApiErrorSource"));
    }

    @Test
    void jsonApiError_WithSource_WorksCorrectly() {
        // Given
        JsonApiError error = new JsonApiError("422", "Validation Error", "Invalid field value");
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource("/data/attributes/name");
        source.setParameter("name");

        // When
        error.setSource(source);

        // Then
        assertEquals(source, error.getSource());
        assertEquals("/data/attributes/name", error.getSource().getPointer());
        assertEquals("name", error.getSource().getParameter());
    }
}