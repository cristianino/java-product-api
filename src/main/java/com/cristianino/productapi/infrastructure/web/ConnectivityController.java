package com.cristianino.productapi.infrastructure.web;

import com.cristianino.productapi.infrastructure.service.InventoryConnectivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/connectivity")
@Tag(name = "Connectivity & Health", description = "🔗 Microservice Health Monitoring & Inter-Service Connectivity")
@SecurityRequirement(name = "X-API-Key")
public class ConnectivityController {
    
    private final InventoryConnectivityService inventoryConnectivityService;
    
    public ConnectivityController(InventoryConnectivityService inventoryConnectivityService) {
        this.inventoryConnectivityService = inventoryConnectivityService;
    }
    
    @GetMapping("/inventory")
    @Operation(summary = "Check Inventory Service Connectivity", 
               description = "Verifies connectivity with the Inventory microservice.\n\n" +
                           "**Returns:**\n" +
                           "- `status: UP` - Service is reachable and healthy\n" +
                           "- `status: DOWN` - Service is unreachable or unhealthy\n\n" +
                           "**Use cases:**\n" +
                           "- Health monitoring\n" +
                           "- Load balancer checks\n" +
                           "- Integration testing")
    public ResponseEntity<Map<String, Object>> checkInventoryConnectivity() {
        Map<String, Object> connectivityResult = inventoryConnectivityService.checkInventoryConnectivity();
        
        // Return appropriate HTTP status based on connectivity result
        if ("UP".equals(connectivityResult.get("status"))) {
            return ResponseEntity.ok(connectivityResult);
        } else {
            return ResponseEntity.status(503).body(connectivityResult); // Service Unavailable
        }
    }
    
    @GetMapping("/status")
    @Operation(summary = "Overall Connectivity Status", 
               description = "Comprehensive health check for all microservice dependencies.\n\n" +
                           "**Response includes:**\n" +
                           "- Product API status (always UP if responding)\n" +
                           "- Inventory API connectivity status\n" +
                           "- Overall system health (`UP` or `DEGRADED`)\n" +
                           "- Timestamp for monitoring\n\n" +
                           "**Perfect for:**\n" +
                           "- System dashboards\n" +
                           "- Monitoring alerts\n" +
                           "- Service mesh health checks")
    public ResponseEntity<Map<String, Object>> getConnectivityStatus() {
        Map<String, Object> overallStatus = new HashMap<>();
        
        // Check inventory connectivity
        Map<String, Object> inventoryStatus = inventoryConnectivityService.checkInventoryConnectivity();
        
        // Build overall response
        overallStatus.put("product-api", Map.of(
            "service", "product-api",
            "status", "UP",
            "message", "Product API is running"
        ));
        
        overallStatus.put("inventory-api", inventoryStatus);
        
        // Determine overall health
        boolean allServicesUp = "UP".equals(inventoryStatus.get("status"));
        overallStatus.put("overall-status", allServicesUp ? "UP" : "DEGRADED");
        overallStatus.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(overallStatus);
    }
}