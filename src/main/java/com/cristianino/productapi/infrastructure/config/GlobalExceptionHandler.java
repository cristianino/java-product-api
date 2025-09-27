package com.cristianino.productapi.infrastructure.config;

import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.JsonApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<JsonApiError> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : "unknown";
            String errorMessage = error.getDefaultMessage();
            
            JsonApiError jsonApiError = new JsonApiError();
            jsonApiError.setStatus("400");
            jsonApiError.setCode("VALIDATION_ERROR");
            jsonApiError.setTitle("Validation Failed");
            jsonApiError.setDetail(errorMessage);
            
            // Set source pointer according to JSON:API spec
            JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource();
            source.setPointer("/data/attributes/" + fieldName);
            jsonApiError.setSource(source);
            
            errors.add(jsonApiError);
        });
        
        JsonApiResponse<Void> response = new JsonApiResponse<>(errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<JsonApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        JsonApiError error = new JsonApiError();
        error.setStatus("400");
        error.setCode("BAD_REQUEST");
        error.setTitle("Bad Request");
        error.setDetail(ex.getMessage());
        
        List<JsonApiError> errors = List.of(error);
        JsonApiResponse<Void> response = new JsonApiResponse<>(errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        JsonApiError error = new JsonApiError();
        error.setStatus("400");
        error.setCode("INVALID_JSON");
        error.setTitle("Invalid JSON");
        error.setDetail("The request body contains invalid JSON");
        
        List<JsonApiError> errors = List.of(error);
        JsonApiResponse<Void> response = new JsonApiResponse<>(errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        JsonApiError error = new JsonApiError();
        error.setStatus("400");
        error.setCode("TYPE_MISMATCH");
        error.setTitle("Type Mismatch");
        error.setDetail(String.format("Invalid value '%s' for parameter '%s'", 
            ex.getValue(), ex.getName()));
        
        JsonApiError.JsonApiErrorSource source = new JsonApiError.JsonApiErrorSource();
        source.setParameter(ex.getName());
        error.setSource(source);
        
        List<JsonApiError> errors = List.of(error);
        JsonApiResponse<Void> response = new JsonApiResponse<>(errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        
        JsonApiError error = new JsonApiError();
        error.setStatus("500");
        error.setCode("INTERNAL_ERROR");
        error.setTitle("Internal Server Error");
        error.setDetail("An unexpected error occurred");
        
        List<JsonApiError> errors = List.of(error);
        JsonApiResponse<Void> response = new JsonApiResponse<>(errors);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}