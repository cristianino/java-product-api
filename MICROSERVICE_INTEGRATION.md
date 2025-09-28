# Microservice Integration Guide

## 📋 Overview

This product microservice provides simple and clear APIs to be consumed by other microservices, specifically the **inventory microservice**.

## 🔑 Authentication

All APIs require the authentication header:
```
X-API-Key: your-secret-api-key-here
```

## 🎯 Available APIs for Microservices

### 1. Public JSON:API (Recommended for external integrations)

**Base URL:** `http://localhost:8080/api/products`

#### Get all products
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/vnd.api+json" \
     http://localhost:8080/api/products
```

#### Get a specific product
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/vnd.api+json" \
     http://localhost:8080/api/products/1
```

### 2. Simplified Internal API (Recommended for microservices)

**Base URL:** `http://localhost:8080/api/internal/products`

#### Get all products (simplified format)
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products
```

**Response:**
```json
{
  "products": [
    {
      "id": 1,
      "name": "Dell XPS 13 Laptop",
      "price": 999.99,
      "availability": true
    }
  ],
  "totalCount": 1
}
```

#### Get a specific product
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Dell XPS 13 Laptop", 
  "price": 999.99,
  "availability": true
}
```

#### Get multiple products by IDs
```bash
curl -X POST \
     -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     -d '{"productIds": [1, 2, 3]}' \
     http://localhost:8080/api/internal/products/batch
```

#### Check product availability
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products/1/availability
```

**Response:**
```json
{
  "productId": 1,
  "available": true
}
```

## 🏗️ Integration with Inventory Microservice

### Typical Usage Scenario

1. **Catalog Synchronization:** The inventory microservice gets all products
2. **Product Validation:** Before creating inventory, verifies that the product exists
3. **Availability Update:** Queries the current product availability

### Java Implementation Example (Inventory Microservice)

```java
@Service
public class ProductService {
    
    private final WebClient webClient;
    
    @Value("${products.service.url:http://localhost:8080}")
    private String productsServiceUrl;
    
    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .defaultHeader("X-API-Key", "your-secret-api-key-here")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
    
    public List<ProductDto> getAllProducts() {
        String url = productsServiceUrl + "/api/internal/products";
        
        ProductListResponse response = webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(ProductListResponse.class)
            .block();
            
        return response.getProducts();
    }
    
    public ProductDto getProductById(Long productId) {
        String url = productsServiceUrl + "/api/internal/products/" + productId;
        
        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(ProductDto.class)
            .block();
    }
    
    public boolean isProductAvailable(Long productId) {
        String url = productsServiceUrl + "/api/internal/products/" + productId + "/availability";
        
        AvailabilityResponse response = webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(AvailabilityResponse.class)
            .block();
            
        return response.isAvailable();
    }
}
```

## 🔍 Health Checks

Check the microservice status:
```bash
curl http://localhost:8080/actuator/health
```

## 🐳 Environment Configuration

### Development (port 8080)
```bash
docker compose -f docker-compose.dev.yml up -d
```

### Testing (port 8082) 
```bash
# Configure test profile
java -Dspring.profiles.active=test -jar target/product-api-*.jar
```

### Production
```bash
# Environment variables for configuration
export DATABASE_URL=jdbc:postgresql://prod-db:5432/products
export API_KEY=production-secret-key
export INVENTORY_SERVICE_URL=http://inventory-service:8080
```

## 📊 Monitoring and Logs

- **Health Check:** `/actuator/health`
- **Metrics:** `/actuator/metrics`  
- **Logs:** Configured with DEBUG level for microservice communication

## ⚠️ Important Considerations

1. **API Key:** Always use the `X-API-Key` header for authentication
2. **Content-Type:** For internal API use `application/json`, for public API use `application/vnd.api+json`
3. **Timeouts:** Configure appropriate timeouts in your WebClient (recommended: 5 seconds)
4. **Retry Policy:** Implement retry with exponential backoff for failed calls
5. **Circuit Breaker:** Consider using Resilience4j for failure handling

## 🚀 Next Steps

1. The inventory microservice should implement these calls
2. Configure service discovery (Eureka) for production  
3. Implement circuit breakers for greater resilience
4. Add communication metrics between services