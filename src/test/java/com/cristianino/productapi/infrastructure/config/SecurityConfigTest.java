package com.cristianino.productapi.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "apiKey", "test-api-key");
    }

    @Test
    void filterChain_CreatesSecurityFilterChain() throws Exception {
        // Given
        HttpSecurity http = new HttpSecurity(null, null, null);

        // When & Then
        SecurityFilterChain filterChain = securityConfig.filterChain(http);
        
        // Basic assertion that the filter chain is created
        assertNotNull(filterChain);
    }

    @Test
    void securityConfig_HasCorrectApiKeyValue() {
        // When
        String apiKey = (String) ReflectionTestUtils.getField(securityConfig, "apiKey");

        // Then
        assertEquals("test-api-key", apiKey);
    }

    @Test
    void securityConfig_CanBeInstantiated() {
        // When & Then
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }
}