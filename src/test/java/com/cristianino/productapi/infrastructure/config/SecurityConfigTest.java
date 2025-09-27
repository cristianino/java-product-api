package com.cristianino.productapi.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;
import jakarta.servlet.Filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "apiKey", "test-api-key");
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
        // Given & When
        SecurityConfig config = new SecurityConfig();
        
        // Then
        assertNotNull(config);
    }
}