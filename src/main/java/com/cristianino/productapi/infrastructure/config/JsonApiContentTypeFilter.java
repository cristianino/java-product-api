package com.cristianino.productapi.infrastructure.config;

import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter to validate JSON:API Content-Type header for incoming requests
 */
public class JsonApiContentTypeFilter extends OncePerRequestFilter {
    
    private static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
    private final ObjectMapper objectMapper;
    
    public JsonApiContentTypeFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Skip validation for GET, DELETE, and HEAD requests (no body expected)
        String method = request.getMethod();
        if (HttpMethod.GET.name().equals(method) || 
            HttpMethod.DELETE.name().equals(method) || 
            HttpMethod.HEAD.name().equals(method) ||
            HttpMethod.OPTIONS.name().equals(method)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Skip validation for non-API endpoints
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Skip validation for actuator endpoints
        if (requestURI.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String contentType = request.getContentType();
        
        // Check if Content-Type is present and correct for requests with body
        if (contentType == null || !contentType.startsWith(JSON_API_CONTENT_TYPE)) {
            sendJsonApiError(response, "415", "Unsupported Media Type", 
                "Content-Type must be 'application/vnd.api+json'");
            return;
        }
        
        // Set response Content-Type
        response.setContentType(JSON_API_CONTENT_TYPE);
        
        filterChain.doFilter(request, response);
    }
    
    private void sendJsonApiError(HttpServletResponse response, String status, 
                                  String title, String detail) throws IOException {
        JsonApiError error = new JsonApiError();
        error.setStatus(status);
        error.setCode("INVALID_CONTENT_TYPE");
        error.setTitle(title);
        error.setDetail(detail);
        
        JsonApiResponse<Void> errorResponse = new JsonApiResponse<>(List.of(error));
        
        response.setStatus(Integer.parseInt(status));
        response.setContentType(JSON_API_CONTENT_TYPE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}