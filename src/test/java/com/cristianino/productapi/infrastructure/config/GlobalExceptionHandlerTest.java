package com.cristianino.productapi.infrastructure.config;

import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.JsonApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void handleValidationErrors_ReturnsJsonApiErrorResponse() {
        // Given
        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "target");
        bindingResult.addError(new FieldError("target", "name", "Name is required"));
        bindingResult.addError(new FieldError("target", "price", "Price must be positive"));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<JsonApiResponse<Void>> response = exceptionHandler.handleValidationErrors(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getErrors().size());
        
        JsonApiError firstError = response.getBody().getErrors().get(0);
        assertEquals("400", firstError.getStatus());
        assertEquals("VALIDATION_ERROR", firstError.getCode());
        assertEquals("Validation Failed", firstError.getTitle());
        assertNotNull(firstError.getSource());
        assertTrue(firstError.getSource().getPointer().contains("/data/attributes/"));
    }

    @Test
    void handleIllegalArgumentException_ReturnsJsonApiErrorResponse() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input data");

        // When
        ResponseEntity<JsonApiResponse<Void>> response = exceptionHandler.handleIllegalArgumentException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        
        JsonApiError error = response.getBody().getErrors().get(0);
        assertEquals("400", error.getStatus());
        assertEquals("BAD_REQUEST", error.getCode());
        assertEquals("Bad Request", error.getTitle());
        assertEquals("Invalid input data", error.getDetail());
    }

    @Test
    void handleHttpMessageNotReadable_ReturnsJsonApiErrorResponse() {
        // Given
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);

        // When
        ResponseEntity<JsonApiResponse<Void>> response = exceptionHandler.handleHttpMessageNotReadable(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        
        JsonApiError error = response.getBody().getErrors().get(0);
        assertEquals("400", error.getStatus());
        assertEquals("INVALID_JSON", error.getCode());
        assertEquals("Invalid JSON", error.getTitle());
    }

    @Test
    void handleTypeMismatch_ReturnsJsonApiErrorResponse() {
        // Given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        exception = new MethodArgumentTypeMismatchException("invalid", Long.class, "id", null, null);

        // When
        ResponseEntity<JsonApiResponse<Void>> response = exceptionHandler.handleTypeMismatch(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        
        JsonApiError error = response.getBody().getErrors().get(0);
        assertEquals("400", error.getStatus());
        assertEquals("TYPE_MISMATCH", error.getCode());
        assertEquals("Type Mismatch", error.getTitle());
        assertNotNull(error.getSource());
    }

    @Test
    void handleGenericException_ReturnsJsonApiErrorResponse() {
        // Given
        Exception exception = new Exception("Unexpected error");

        // When
        ResponseEntity<JsonApiResponse<Void>> response = exceptionHandler.handleGenericException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        
        JsonApiError error = response.getBody().getErrors().get(0);
        assertEquals("500", error.getStatus());
        assertEquals("INTERNAL_ERROR", error.getCode());
        assertEquals("Internal Server Error", error.getTitle());
        assertEquals("An unexpected error occurred", error.getDetail());
    }
}