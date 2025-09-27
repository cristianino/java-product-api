# API Versioning Strategy Guide

This document describes the API versioning strategy for the Java Product API microservice and provides a complete roadmap for implementing V2.0.

## 🎯 Versioning Approach

### URI Path Versioning
We use **URI Path Versioning** as our primary strategy:
- ✅ **Clear and explicit** version identification
- ✅ **Easy to understand** and implement
- ✅ **Compatible** with all HTTP tools and clients
- ✅ **Allows concurrent versions** without conflicts

### Version Format
- **Pattern:** `/api/v{major}/resource`
- **Examples:** `/api/v1/products`, `/api/v2/products` (future)
- **Semantic:** Major version only in URI (e.g., v1, v2, v3)

## 📋 Current Implementation

### Version 1.0 (Current & Stable)
**Status:** ✅ **Active & Recommended**  
**Base Path:** `/api/v1/`

**Features:**
- Standard CRUD operations
- JSON:API specification compliance
- Basic metadata and links
- Stable and proven functionality
- Ready foundation for V2.0 development

**Response Format:**
```json
{
  "data": {...},
  "links": {"self": "/api/v1/products/1"},
  "meta": {"version": "1.0", "count": 10}
}
```

### Default Version
**Status:** ✅ **Active**  
**Base Path:** `/api/products`

**Purpose:**
- Default endpoints for backward compatibility
- Simple integration for basic use cases
- Same functionality as V1.0 without version prefix

## 🚀 V2.0 Development Roadmap

### Version 2.0 (Planned Implementation)
**Status:** 📋 **Planned - Ready for Development**  
**Base Path:** `/api/v2/` (future)

### Proposed V2.0 Features

#### 1. Enhanced Pagination & Filtering
```bash
GET /api/v2/products?page=0&size=20&name=laptop&minPrice=100&maxPrice=500
```

**Response with Enhanced Metadata:**
```json
{
  "data": [...],
  "links": {
    "self": "/api/v2/products?page=0&size=20",
    "first": "/api/v2/products?page=0&size=20",
    "last": "/api/v2/products?page=5&size=20",
    "next": "/api/v2/products?page=1&size=20",
    "prev": null
  },
  "meta": {
    "version": "2.0",
    "api_version": "v2",
    "page": 0,
    "size": 20,
    "total_items": 120,
    "total_pages": 6,
    "retrieved_at": "2025-09-27T17:30:22.123Z",
    "features": ["pagination", "filtering", "enhanced_metadata", "bulk_operations"]
  }
}
```

#### 2. Rich HATEOAS Links
```json
{
  "links": {
    "self": "/api/v2/products/1",
    "edit": "/api/v2/products/1",
    "delete": "/api/v2/products/1",
    "collection": "/api/v2/products",
    "related": "/api/v2/products/1/inventory"
  }
}
```

#### 3. Enhanced Error Handling
```json
{
  "errors": [
    {
      "id": "unique-error-id",
      "status": "400",
      "code": "VALIDATION_ERROR",
      "title": "Validation Failed",
      "detail": "Product price must be greater than 0",
      "source": {
        "pointer": "/data/attributes/price"
      },
      "meta": {
        "timestamp": "2025-09-27T17:30:22.123Z",
        "request_id": "req-123"
      }
    }
  ]
}
```

#### 4. Version-Specific Health Endpoint
```bash
GET /api/v2/products/health
```

```json
{
  "status": "UP",
  "version": "2.0",
  "api_version": "v2",
  "timestamp": "2025-09-27T17:30:22.123Z",
  "features": [
    "pagination",
    "filtering", 
    "enhanced_metadata",
    "bulk_operations",
    "inventory_integration"
  ],
  "performance": {
    "avg_response_time": "45ms",
    "cache_hit_rate": "85%"
  }
}
```

#### 5. Bulk Operations
```bash
# Bulk create
POST /api/v2/products/batch

# Bulk update
PATCH /api/v2/products/batch

# Bulk delete
DELETE /api/v2/products/batch
```

## 🛠️ V2.0 Implementation Guide

### Step 1: Controller Architecture Setup

Create the V2.0 controller structure:
```
src/main/java/.../infrastructure/web/
├── ProductController.java           # Default endpoints
├── v1/ProductControllerV1.java     # Version 1.0 (current)
├── v2/ProductControllerV2.java     # Version 2.0 (implement this)
└── ConnectivityController.java     # Unversioned utilities
```

### Step 2: V2.0 Controller Template

```java
@RestController
@RequestMapping("/api/v2/products")
@Tag(name = "Products V2", description = "Product management operations - Version 2.0 (Enhanced)")
@SecurityRequirement(name = "X-API-Key")
public class ProductControllerV2 {
    
    private final ProductUseCase productUseCase;
    
    public ProductControllerV2(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }
    
    @GetMapping
    @Operation(summary = "Get products with pagination (V2)")
    public ResponseEntity<JsonApiResponse<List<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        
        // Implementation with pagination logic
        // Enhanced metadata
        // Rich HATEOAS links
    }
    
    @GetMapping("/health")
    @Operation(summary = "V2 API Health Check")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        // V2-specific health information
    }
    
    // Additional V2 endpoints...
}
```

### Step 3: Enhanced DTOs for V2

Consider creating V2-specific DTOs if needed:
```java
// Enhanced product response for V2
public class ProductV2Response extends ProductDto {
    private LocalDateTime lastUpdated;
    private String category;
    private List<String> tags;
    // Additional V2 fields
}
```

### Step 4: OpenAPI Configuration Update

Update `OpenApiConfig.java` to include V2 group:
```java
@Bean
public GroupedOpenApi publicApiV2() {
    return GroupedOpenApi.builder()
            .group("v2-products")
            .displayName("Products API V2.0 (Enhanced)")
            .pathsToMatch("/api/v2/**")
            .build();
}
```

### Step 5: Testing Strategy

Create comprehensive test suites:
- `ProductControllerV2Test.java` - Unit tests for V2
- Update `ApiVersioningIntegrationTest.java` - Integration tests
- Performance tests for pagination
- Backward compatibility tests

## 📊 Version Comparison

| Feature | Legacy | V1.0 | V2.0 |
|---------|--------|------|------|
| **CRUD Operations** | ✅ | ✅ | ✅ |
| **JSON:API Format** | ✅ | ✅ | ✅ |
| **Basic Links** | ✅ | ✅ | ✅ |
| **Pagination** | ❌ | ❌ | ✅ |
| **Enhanced Metadata** | ❌ | ❌ | ✅ |
| **Rich Error Codes** | ❌ | ❌ | ✅ |
| **HATEOAS Links** | ❌ | ❌ | ✅ |
| **Health Endpoint** | ❌ | ❌ | ✅ |
| **Filtering** | ❌ | ❌ | ✅ |
| **Timestamps** | ❌ | ❌ | ✅ |

## 🚀 Usage Examples

### Version 2.0 (Recommended)
```bash
# Get products with pagination
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v2/products?page=0&size=10"

# Create product (enhanced response)
curl -X POST "http://localhost:8080/api/v2/products" \
     -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     -d '{
       "data": {
         "type": "products",
         "attributes": {"name": "New Product", "price": 99.99}
       }
     }'

# Health check
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v2/products/health"
```

### Version 1.0 (Stable)
```bash
# Standard operations
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v1/products"

curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v1/products/1"
```

## 📈 Migration Guidelines

### For New Integrations
1. **Always use V2.0** for new projects
2. **Leverage enhanced features** like pagination and filtering
3. **Use rich metadata** for better client experience

### For Existing Integrations

#### From Legacy to V1
1. Change base URL from `/api/products` to `/api/v1/products`
2. No other changes required - same response format
3. Test thoroughly in development environment

#### From V1 to V2
1. Update base URL from `/api/v1/products` to `/api/v2/products`
2. **Optional:** Implement pagination (`?page=0&size=20`)
3. **Optional:** Use enhanced metadata and links
4. Update error handling for new error codes

### Migration Checklist
- [ ] Identify all current API endpoints in use
- [ ] Test new version in development environment
- [ ] Update API client configurations
- [ ] Implement gradual rollout strategy
- [ ] Monitor both versions during transition
- [ ] Update documentation and team knowledge

## 🔄 Backward Compatibility Promise

### What We Guarantee
- ✅ **V1 endpoints will remain functional** for at least 24 months
- ✅ **Response schemas will not break** within the same major version
- ✅ **New fields may be added** but existing fields won't be removed
- ✅ **HTTP status codes will remain consistent**

### What May Change
- ⚠️ **Performance optimizations** may be applied
- ⚠️ **New optional parameters** may be added
- ⚠️ **Additional response metadata** may be included
- ⚠️ **Deprecation warnings** will be communicated in advance

## 🏗️ Future Versioning Strategy

### Version 3.0 (Planned)
**Potential Features:**
- GraphQL support alongside REST
- Real-time subscriptions
- Advanced filtering and sorting
- Bulk operations support
- Enhanced security features

### Deprecation Process
1. **Announce deprecation** 6+ months in advance
2. **Provide migration guides** and tools
3. **Support parallel versions** during transition
4. **Gradual feature restrictions** on deprecated versions
5. **Final sunset** after sufficient transition period

## 📚 Developer Resources

### Testing Your Integration
```bash
# Test all versions work correctly
curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/products
curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v1/products  
curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v2/products
```

### Documentation Access
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **V2 Health:** http://localhost:8080/api/v2/products/health
- **General Health:** http://localhost:8080/actuator/health

### Support and Questions
For version-specific questions or migration support:
1. Check this documentation first
2. Review the API examples in the main README
3. Test endpoints using the Swagger UI
4. Contact the development team for complex migration scenarios