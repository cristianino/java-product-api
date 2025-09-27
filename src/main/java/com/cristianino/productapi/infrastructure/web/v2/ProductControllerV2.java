package com.cristianino.productapi.infrastructure.web.v2;

import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.application.usecase.ProductUseCase;
import com.cristianino.productapi.infrastructure.web.JsonApiRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/products")
@Tag(name = "Products V2", description = "Product management operations - Version 2.0 (Enhanced)")
@SecurityRequirement(name = "X-API-Key")
public class ProductControllerV2 {
    
    private final ProductUseCase productUseCase;
    
    public ProductControllerV2(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }
    
    @PostMapping
    @Operation(summary = "Create a new product (V2)", 
               description = "Creates a new product with enhanced validation and response metadata - Version 2.0")
    public ResponseEntity<JsonApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody JsonApiRequest<ProductDto> request) {
        try {
            ProductDto createdProduct = productUseCase.createProduct(request.getData());
            
            // Enhanced response with additional metadata for V2
            Map<String, String> links = Map.of(
                "self", "/api/v2/products/" + createdProduct.getId(),
                "edit", "/api/v2/products/" + createdProduct.getId(),
                "collection", "/api/v2/products"
            );
            
            Map<String, Object> meta = Map.of(
                "version", "2.0",
                "created_at", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
                "api_version", "v2"
            );
            
            JsonApiResponse<ProductDto> response = new JsonApiResponse<>(createdProduct, links, meta);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            JsonApiError error = new JsonApiError("400", "VALIDATION_ERROR", e.getMessage());
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID (V2)", 
               description = "Retrieves a product with enhanced metadata and links - Version 2.0")
    public ResponseEntity<JsonApiResponse<ProductDto>> getProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        Optional<ProductDto> product = productUseCase.getProductById(id);
        if (product.isPresent()) {
            // Enhanced links for V2
            Map<String, String> links = Map.of(
                "self", "/api/v2/products/" + id,
                "edit", "/api/v2/products/" + id,
                "delete", "/api/v2/products/" + id,
                "collection", "/api/v2/products"
            );
            
            Map<String, Object> meta = Map.of(
                "version", "2.0",
                "retrieved_at", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
                "api_version", "v2"
            );
            
            JsonApiResponse<ProductDto> response = new JsonApiResponse<>(
                    product.get(), links, meta);
            return ResponseEntity.ok(response);
        } else {
            JsonApiError error = new JsonApiError("404", "RESOURCE_NOT_FOUND", "Product not found");
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all products with pagination (V2)", 
               description = "Retrieves products with enhanced pagination and filtering - Version 2.0")
    public ResponseEntity<JsonApiResponse<List<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-based)") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Items per page") int size,
            @RequestParam(required = false) @Parameter(description = "Filter by name") String name) {
        
        List<ProductDto> products = productUseCase.getAllProducts();
        
        // Enhanced pagination metadata for V2
        Map<String, String> links = Map.of(
            "self", "/api/v2/products?page=" + page + "&size=" + size,
            "first", "/api/v2/products?page=0&size=" + size,
            "last", "/api/v2/products?page=" + Math.max(0, (products.size() / size)) + "&size=" + size
        );
        
        Map<String, Object> meta = Map.of(
            "count", products.size(),
            "version", "2.0",
            "page", page,
            "size", size,
            "total_pages", Math.max(1, (int) Math.ceil((double) products.size() / size)),
            "retrieved_at", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
            "api_version", "v2",
            "features", List.of("pagination", "filtering", "enhanced_metadata")
        );
        
        JsonApiResponse<List<ProductDto>> response = new JsonApiResponse<>(
                products, links, meta);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product (V2)", 
               description = "Updates a product with enhanced validation and response - Version 2.0")
    public ResponseEntity<JsonApiResponse<ProductDto>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody JsonApiRequest<ProductDto> request) {
        try {
            Optional<ProductDto> updatedProduct = productUseCase.updateProduct(id, request.getData());
            if (updatedProduct.isPresent()) {
                Map<String, String> links = Map.of(
                    "self", "/api/v2/products/" + id,
                    "collection", "/api/v2/products"
                );
                
                Map<String, Object> meta = Map.of(
                    "version", "2.0",
                    "updated_at", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
                    "api_version", "v2"
                );
                
                JsonApiResponse<ProductDto> response = new JsonApiResponse<>(
                    updatedProduct.get(), links, meta);
                return ResponseEntity.ok(response);
            } else {
                JsonApiError error = new JsonApiError("404", "RESOURCE_NOT_FOUND", "Product not found");
                JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            JsonApiError error = new JsonApiError("400", "VALIDATION_ERROR", e.getMessage());
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product (V2)", 
               description = "Deletes a product with confirmation response - Version 2.0")
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        boolean deleted = productUseCase.deleteProduct(id);
        if (deleted) {
            // Enhanced delete response for V2
            Map<String, Object> response = Map.of(
                "deleted", true,
                "id", id,
                "version", "2.0",
                "deleted_at", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
                "message", "Product successfully deleted"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                "deleted", false,
                "id", id,
                "version", "2.0",
                "error", "Product not found"
            );
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "API Version Health Check (V2)", 
               description = "Health check endpoint specific to API V2")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "version", "2.0",
            "api_version", "v2",
            "timestamp", LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(),
            "features", List.of("pagination", "filtering", "enhanced_metadata", "improved_error_handling")
        );
        return ResponseEntity.ok(health);
    }
}