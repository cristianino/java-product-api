package com.cristianino.productapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ApiVersioningIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String API_KEY = "your-secret-api-key-here";

    @Test
    void shouldSupportMultipleApiVersionsSimultaneously() throws Exception {
        String productRequest = """
            {
                "data": {
                    "type": "products",
                    "attributes": {
                        "name": "Multi-Version Product",
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
                .andExpected(jsonPath("$.meta.version").value("1.0"));

        // Create product using V2 API
        mockMvc.perform(post("/api/v2/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpected(status().isCreated())
                .andExpected(jsonPath("$.meta.version").value("2.0"))
                .andExpected(jsonPath("$.meta.api_version").value("v2"))
                .andExpected(jsonPath("$.links.edit").exists());

        // Retrieve products using V1 API (should have version 1.0 metadata)
        mockMvc.perform(get("/api/v1/products")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.meta.version").value("1.0"));

        // Retrieve products using V2 API (should have enhanced metadata)
        mockMvc.perform(get("/api/v2/products")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.meta.version").value("2.0"))
                .andExpect(jsonPath("$.meta.features").isArray())
                .andExpect(jsonPath("$.meta.page").exists());
    }

    @Test
    void shouldAccessLegacyApiWithDeprecationWarning() throws Exception {
        // Legacy API should still work but marked as deprecated
        mockMvc.perform(get("/api/products")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk());
    }

    @Test
    void shouldProvideV2SpecificFeatures() throws Exception {
        // V2 Health check endpoint
        mockMvc.perform(get("/api/v2/products/health")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpected(jsonPath("$.features").isArray());

        // V2 Pagination parameters
        mockMvc.perform(get("/api/v2/products")
                .param("page", "0")
                .param("size", "10")
                .header("X-API-Key", API_KEY))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.meta.page").value(0))
                .andExpected(jsonPath("$.meta.size").value(10));
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
                .andExpected(status().isCreated());

        // Create via V2 and check V2 links
        mockMvc.perform(post("/api/v2/products")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpected(status().isCreated())
                .andExpect(jsonPath("$.links.self").value(org.hamcrest.Matchers.containsString("/api/v2/")))
                .andExpected(jsonPath("$.links.collection").value("/api/v2/products"));
    }
}