package com.cristianino.productapi.infrastructure.config;

import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter to validate that JSON:API requests use the correct Content-Type header
 */
public class JsonApiContentTypeValidationFilter extends OncePerRequestFilter {
    
    private static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
    private final ObjectMapper objectMapper;
    
    public JsonApiContentTypeValidationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        // Only validate Content-Type for JSON:API endpoints with request body
        if (shouldValidateContentType(requestPath, method)) {
            String contentType = request.getContentType();
            
            if (contentType == null || !contentType.startsWith(JSON_API_CONTENT_TYPE)) {
                sendJsonApiError(response, 
                    "415", 
                    "Unsupported Media Type", 
                    "Content-Type must be 'application/vnd.api+json' for JSON:API requests");
                return;
            }
        }
        
        // Set JSON:API Content-Type for all API responses
        if (requestPath.startsWith("/api/")) {
            response.setContentType(JSON_API_CONTENT_TYPE);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean shouldValidateContentType(String requestPath, String method) {
        // Validate Content-Type for POST and PUT requests to /api/ endpoints
        return requestPath.startsWith("/api/") && 
               (HttpMethod.POST.matches(method) || HttpMethod.PUT.matches(method));
    }
    
    private void sendJsonApiError(HttpServletResponse response, 
                                 String status, 
                                 String title, 
                                 String detail) throws IOException {
        
        JsonApiError error = new JsonApiError();
        error.setStatus(status);
        error.setCode("INVALID_CONTENT_TYPE");
        error.setTitle(title);
        error.setDetail(detail);
        
        JsonApiResponse<Void> errorResponse = new JsonApiResponse<>(List.of(error));
        
        response.setStatus(Integer.parseInt(status));
        response.setContentType(JSON_API_CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}