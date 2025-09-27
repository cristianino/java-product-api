package com.cristianino.productapi.infrastructure.web;

import com.cristianino.productapi.application.dto.JsonApiResponse;
import com.cristianino.productapi.application.dto.JsonApiError;
import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.application.usecase.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products Default", description = "📦 Product Management API - Default/Legacy Endpoints (Same as V1.0)")
@SecurityRequirement(name = "X-API-Key")
public class ProductController {
    
    private final ProductUseCase productUseCase;
    
    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }
    
    @PostMapping
    @Operation(summary = "Create a new product (Default API)", 
               description = "Creates a new product using default endpoints.\n\n" +
                           "**Note:** For new integrations, consider using `/api/v1/products` instead.\n\n" +
                           "Same functionality as V1.0 API without version prefix.")
    public ResponseEntity<JsonApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody JsonApiRequest<ProductDto> request) {
        try {
            ProductDto createdProduct = productUseCase.createProduct(request.getData());
            JsonApiResponse<ProductDto> response = new JsonApiResponse<>(createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            JsonApiError error = new JsonApiError("400", "Bad Request", e.getMessage());
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<JsonApiResponse<ProductDto>> getProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        Optional<ProductDto> product = productUseCase.getProductById(id);
        if (product.isPresent()) {
            Map<String, String> links = Map.of("self", "/api/products/" + id);
            JsonApiResponse<ProductDto> response = new JsonApiResponse<>(
                    product.get(), links, null);
            return ResponseEntity.ok(response);
        } else {
            JsonApiError error = new JsonApiError("404", "Not Found", "Product not found");
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<JsonApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productUseCase.getAllProducts();
        Map<String, String> links = Map.of("self", "/api/products");
        Map<String, Object> meta = Map.of("count", products.size());
        JsonApiResponse<List<ProductDto>> response = new JsonApiResponse<>(
                products, links, meta);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<JsonApiResponse<ProductDto>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody JsonApiRequest<ProductDto> request) {
        try {
            Optional<ProductDto> updatedProduct = productUseCase.updateProduct(id, request.getData());
            if (updatedProduct.isPresent()) {
                JsonApiResponse<ProductDto> response = new JsonApiResponse<>(updatedProduct.get());
                return ResponseEntity.ok(response);
            } else {
                JsonApiError error = new JsonApiError("404", "Not Found", "Product not found");
                JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            JsonApiError error = new JsonApiError("400", "Bad Request", e.getMessage());
            JsonApiResponse<ProductDto> errorResponse = new JsonApiResponse<>(List.of(error));
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        boolean deleted = productUseCase.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}