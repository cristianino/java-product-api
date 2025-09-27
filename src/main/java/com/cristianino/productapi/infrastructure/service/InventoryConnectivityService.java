package com.cristianino.productapi.infrastructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.HashMap;

@Service
public class InventoryConnectivityService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryConnectivityService.class);
    
    @Value("${microservices.inventory.base-url}")
    private String inventoryBaseUrl;
    
    @Value("${microservices.inventory.health-endpoint}")
    private String healthEndpoint;
    
    private final RestTemplate restTemplate;
    
    public InventoryConnectivityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Map<String, Object> checkInventoryConnectivity() {
        Map<String, Object> result = new HashMap<>();
        String fullUrl = inventoryBaseUrl + healthEndpoint;
        
        try {
            logger.info("Checking connectivity to Inventory service at: {}", fullUrl);
            
            // Make the HTTP call to the inventory service health endpoint
            Map<String, Object> response = restTemplate.getForObject(fullUrl, Map.class);
            
            result.put("service", "inventory-api");
            result.put("status", "UP");
            result.put("url", fullUrl);
            result.put("response", response);
            result.put("message", "Successfully connected to Inventory service");
            
            logger.info("Successfully connected to Inventory service");
            
        } catch (RestClientException e) {
            logger.error("Failed to connect to Inventory service at {}: {}", fullUrl, e.getMessage());
            
            result.put("service", "inventory-api");
            result.put("status", "DOWN");
            result.put("url", fullUrl);
            result.put("error", e.getMessage());
            result.put("message", "Failed to connect to Inventory service");
        }
        
        return result;
    }
}