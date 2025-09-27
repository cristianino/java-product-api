# API Versioning Strategy Guide

This document describes the comprehensive API versioning strategy implemented in the Java Product API microservice.

## 🎯 Versioning Approach

### URI Path Versioning
We use **URI Path Versioning** as our primary strategy:
- ✅ **Clear and explicit** version identification
- ✅ **Easy to understand** and implement
- ✅ **Compatible** with all HTTP tools and clients
- ✅ **Allows concurrent versions** without conflicts

### Version Format
- **Pattern:** `/api/v{major}/resource`
- **Examples:** `/api/v1/products`, `/api/v2/products`
- **Semantic:** Major version only in URI (e.g., v1, v2, v3)

## 📋 Current API Versions

### Version 2.0 (Current)
**Status:** ✅ **Active & Recommended**  
**Base Path:** `/api/v2/`

**Features:**
- Enhanced pagination (`page`, `size` parameters)
- Rich metadata in responses (timestamps, version info)
- Improved error handling with specific error codes
- Additional HATEOAS links (edit, delete, collection)
- Version-specific health endpoints
- Filtering capabilities

**Response Enhancements:**
```json
{
  "data": {...},
  "links": {
    "self": "/api/v2/products/1",
    "edit": "/api/v2/products/1",
    "delete": "/api/v2/products/1",
    "collection": "/api/v2/products"
  },
  "meta": {
    "version": "2.0",
    "api_version": "v2",
    "created_at": "2025-09-27T17:30:22.123Z",
    "features": ["pagination", "filtering", "enhanced_metadata"]
  }
}
```

### Version 1.0 (Stable)
**Status:** ✅ **Active & Supported**  
**Base Path:** `/api/v1/`

**Features:**
- Standard CRUD operations
- JSON:API specification compliance
- Basic metadata and links
- Stable and proven functionality

**Response Format:**
```json
{
  "data": {...},
  "links": {"self": "/api/v1/products/1"},
  "meta": {"version": "1.0", "count": 10}
}
```

### Legacy Version (Deprecated)
**Status:** ⚠️ **Deprecated**  
**Base Path:** `/api/products`

**Purpose:**
- Backward compatibility during migration
- Existing integrations support
- Gradual deprecation path

## 🛠️ Implementation Details

### Controller Architecture
```
src/main/java/.../infrastructure/web/
├── ProductController.java           # Legacy (deprecated)
├── v1/ProductControllerV1.java     # Version 1.0
├── v2/ProductControllerV2.java     # Version 2.0
└── ConnectivityController.java     # Unversioned utilities
```

### OpenAPI Documentation
Separate documentation groups for each version:
- **V2.0:** "Products API V2.0 (Current)"
- **V1.0:** "Products API V1.0"
- **Legacy:** "Legacy API (Deprecated)"
- **Utils:** "Connectivity & Health"

### Testing Strategy
Each version has dedicated test suites:
- `ProductControllerV1Test.java`
- `ProductControllerV2Test.java`
- `ApiVersioningIntegrationTest.java`

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