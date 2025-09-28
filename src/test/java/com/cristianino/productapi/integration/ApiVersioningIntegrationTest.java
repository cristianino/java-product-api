package com.cristianino.productapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ApiVersioningIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String API_KEY = "test-api-key";

    @Test
    void shouldSupportCurrentApiVersions() throws Exception {
        String productRequest = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Test Product",
                        "price": 199.99
                    }
                }
            }""";

        // Create product using V1 API
        mockMvc.perform(post("/api/v1/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meta.version").value("1.0"));

        // Create product using default API
        mockMvc.perform(post("/api/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpect(status().isCreated());

        // Retrieve products using V1 API
        mockMvc.perform(get("/api/v1/products")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.version").value("1.0"));
    }

    @Test
    void shouldAccessDefaultApi() throws Exception {
        // Default API should work normally
        mockMvc.perform(get("/api/products")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMaintainVersionConsistencyInLinks() throws Exception {
        String productRequest = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Link Test Product",
                        "price": 99.99
                    }
                }
            }""";

        // Create via V1 and check V1 links
        mockMvc.perform(post("/api/v1/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links.self").value(org.hamcrest.Matchers.containsString("/api/v1/")));

        // Create via default API
        mockMvc.perform(post("/api/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links.self").value(org.hamcrest.Matchers.containsString("/api/products/")));
    }

    @Test
    void shouldBeReadyForV2Implementation() throws Exception {
        // Verify that V1 structure is solid for V2 extension
        mockMvc.perform(get("/api/v1/products")
                .header("X-API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.version").value("1.0"))
                .andExpect(jsonPath("$.links").exists())
                .andExpect(jsonPath("$.data").exists());
    }
}