package com.cristianino.productapi.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiKeyAuthenticationFilterTest {

    private static final String VALID_API_KEY = "test-api-key";
    private static final String INVALID_API_KEY = "invalid-key";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private ApiKeyAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new ApiKeyAuthenticationFilter(VALID_API_KEY);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidApiKey_SetsAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-API-Key")).thenReturn(VALID_API_KEY);

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        assertEquals("api-client", auth.getPrincipal());
        assertNull(auth.getCredentials());
        assertTrue(auth.getAuthorities().isEmpty());

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidApiKey_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-API-Key")).thenReturn(INVALID_API_KEY);

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_NoApiKey_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-API-Key")).thenReturn(null);

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_EmptyApiKey_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("X-API-Key")).thenReturn("");

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void constructor_SetsValidApiKey() {
        // When
        ApiKeyAuthenticationFilter testFilter = new ApiKeyAuthenticationFilter("my-api-key");

        // Then
        assertNotNull(testFilter);
        // We can't directly test the private field, but the functionality is tested in other tests
    }

    @Test
    void doFilterInternal_AlwaysCallsFilterChain() throws ServletException, IOException {
        // Test with valid key
        when(request.getHeader("X-API-Key")).thenReturn(VALID_API_KEY);
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);

        // Reset mock
        reset(filterChain);

        // Test with invalid key
        when(request.getHeader("X-API-Key")).thenReturn(INVALID_API_KEY);
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}