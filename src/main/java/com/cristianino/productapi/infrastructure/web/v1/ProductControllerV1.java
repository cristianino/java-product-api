package com.cristianino.productapi.infrastructure.web.v1;

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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products V1", description = "📦 Product Management API V1.0 - Stable & Production Ready")
@SecurityRequirement(name = "X-API-Key")
public class ProductControllerV1 {
    
    private final ProductUseCase productUseCase;
    
    public ProductControllerV1(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }
    
    @PostMapping
    @Operation(summary = "Create a new product", 
               description = "Creates a new product using **JSON:API format** (V1.0).\n\n" +
                           "**Request Format:**\n" +
                           "```json\n" +
                           "{\n" +
                           "  \"data\": {\n" +
                           "    \"type\": \"products\",\n" +
                           "    \"attributes\": {\n" +
                           "      \"name\": \"Product Name\",\n" +
                           "      \"price\": 99.99\n" +
                           "    }\n" +
                           "  }\n" +
                           "}\n" +
                           "```")
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
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID - Version 1.0")
    public ResponseEntity<JsonApiResponse<ProductDto>> getProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        Optional<ProductDto> product = productUseCase.getProductById(id);
        if (product.isPresent()) {
            Map<String, String> links = Map.of("self", "/api/v1/products/" + id);
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
    @Operation(summary = "Get all products", 
               description = "Retrieves all products in **JSON:API format** (V1.0).\n\n" +
                           "**Features:**\n" +
                           "- Complete product list\n" +
                           "- HATEOAS links\n" +
                           "- Metadata with count\n\n" +
                           "**Future V2.0 will add:** Pagination, filtering, sorting")
    public ResponseEntity<JsonApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productUseCase.getAllProducts();
        Map<String, String> links = Map.of("self", "/api/v1/products");
        Map<String, Object> meta = Map.of(
            "count", products.size(),
            "version", "1.0"
        );
        JsonApiResponse<List<ProductDto>> response = new JsonApiResponse<>(
                products, links, meta);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product - Version 1.0")
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
    @Operation(summary = "Delete product", description = "Deletes a product by its ID - Version 1.0")
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