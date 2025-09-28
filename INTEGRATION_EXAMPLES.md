# Practical Examples - Inventory Microservice Integration

## 🎯 Real Use Cases

### 1. Initial Catalog Synchronization

When the inventory microservice starts up, it needs to synchronize all products:

```java
@Component
public class ProductCatalogSynchronizer {

    private final WebClient productServiceClient;
    
    @EventListener(ApplicationReadyEvent.class)
    public void synchronizeProductCatalog() {
        log.info("Starting product catalog synchronization...");
        
        // Get all products from the product microservice
        ProductListResponse products = productServiceClient
            .get()
            .uri("/api/internal/products")
            .retrieve()
            .bodyToMono(ProductListResponse.class)
            .block();
            
        // Create inventory records for new products
        products.getProducts().forEach(product -> {
            if (!inventoryExists(product.getId())) {
                createInventoryRecord(product);
                log.info("Created inventory for product: {}", product.getName());
            }
        });
        
        log.info("Synchronization completed. {} products processed", 
                 products.getTotalCount());
    }
}
```

### 2. Product Validation Before Creating Inventory

Before creating a new inventory record, validate that the product exists:

```java
@Service
public class InventoryService {

    public InventoryRecord createInventory(Long productId, int quantity) {
        // 1. Verify that the product exists
        ProductDto product = validateProductExists(productId);
        
        // 2. Create inventory record
        InventoryRecord inventory = new InventoryRecord();
        inventory.setProductId(productId);
        inventory.setProductName(product.getName());
        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        
        return inventoryRepository.save(inventory);
    }
    
    private ProductDto validateProductExists(Long productId) {
        try {
            return productServiceClient
                .get()
                .uri("/api/internal/products/" + productId)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
    }
}
```

### 3. Periodic Availability Updates

Periodically synchronize product availability:

```java
@Component
public class ProductAvailabilityUpdater {

    @Scheduled(fixedDelay = 300000) // Every 5 minutes
    public void updateProductAvailability() {
        log.debug("Updating product availability...");
        
        List<InventoryRecord> inventories = inventoryRepository.findAll();
        
        inventories.parallelStream().forEach(inventory -> {
            try {
                AvailabilityResponse availability = productServiceClient
                    .get()
                    .uri("/api/internal/products/" + inventory.getProductId() + "/availability")
                    .retrieve()
                    .bodyToMono(AvailabilityResponse.class)
                    .block();
                
                if (inventory.isAvailable() != availability.isAvailable()) {
                    inventory.setAvailable(availability.isAvailable());
                    inventoryRepository.save(inventory);
                    log.info("Updated availability for product {}: {}", 
                             inventory.getProductId(), availability.isAvailable());
                }
            } catch (Exception e) {
                log.error("Error updating availability for product {}: {}", 
                          inventory.getProductId(), e.getMessage());
            }
        });
    }
}
```

### 4. Batch Operation for Multiple Products

When you need information for several products at once:

```java
@Service
public class BulkInventoryService {

    public List<InventoryDto> getInventoryForProducts(List<Long> productIds) {
        // 1. Get product information in batch
        BatchProductRequest request = new BatchProductRequest();
        request.setProductIds(productIds);
        
        ProductListResponse products = productServiceClient
            .post()
            .uri("/api/internal/products/batch")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ProductListResponse.class)
            .block();
        
        // 2. Combine with local inventory information
        return products.getProducts().stream()
            .map(product -> {
                InventoryRecord inventory = inventoryRepository
                    .findByProductId(product.getId())
                    .orElse(new InventoryRecord());
                
                return InventoryDto.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(inventory.getQuantity())
                    .available(product.isAvailability() && inventory.getQuantity() > 0)
                    .build();
            })
            .collect(Collectors.toList());
    }
}
```

## 🛠️ WebClient Configuration

### Basic Configuration

```java
@Configuration
public class ProductServiceConfig {

    @Value("${products.service.url:http://localhost:8080}")
    private String productsServiceUrl;
    
    @Value("${products.service.api-key:your-secret-api-key-here}")
    private String apiKey;

    @Bean
    public WebClient productServiceClient(WebClient.Builder builder) {
        return builder
            .baseUrl(productsServiceUrl)
            .defaultHeader("X-API-Key", apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
```

### Advanced Configuration with Resilience4j

```java
@Configuration
public class ResilientProductServiceConfig {

    @Bean
    public WebClient resilientProductServiceClient(WebClient.Builder builder) {
        return builder
            .baseUrl(productsServiceUrl)
            .defaultHeader("X-API-Key", apiKey)
            .defaultHeader("Content-Type", "application/json")
            .filter(ExchangeFilterFunction.ofRequestProcessor(
                CircuitBreakerOperator.of(circuitBreaker)))
            .filter(ExchangeFilterFunction.ofRequestProcessor(
                RetryOperator.of(retry)))
            .build();
    }
    
    @Bean
    public CircuitBreaker circuitBreaker() {
        return CircuitBreaker.ofDefaults("productService");
    }
    
    @Bean  
    public Retry retry() {
        return Retry.ofDefaults("productService");
    }
}
```

## 📝 Inventory Microservice DTOs

```java
// Product service response
public class ProductListResponse {
    private List<ProductDto> products;
    private int totalCount;
    // getters and setters
}

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean availability;
    // getters and setters
}

public class AvailabilityResponse {
    private Long productId;
    private boolean available;
    // getters and setters
}

// Request for batch operations
public class BatchProductRequest {
    private List<Long> productIds;
    // getters and setters
}
```

## 🧪 Testing Examples

### Integration Test

```java
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;
    
    @Test
    @Order(1)
    void shouldRetrieveAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getId()).isNotNull();
        assertThat(products.get(0).getName()).isNotNull();
    }
    
    @Test
    @Order(2)
    void shouldRetrieveSpecificProduct() {
        ProductDto product = productService.getProductById(1L);
        
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isNotNull();
        assertThat(product.getPrice()).isPositive();
    }
    
    @Test
    @Order(3)
    void shouldCheckProductAvailability() {
        boolean available = productService.isProductAvailable(1L);
        
        assertThat(available).isNotNull();
    }
}
```

### Test with WireMock

```java
@SpringBootTest
class ProductServiceMockTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().port(8089))
        .build();

    @Test
    void shouldHandleProductNotFound() {
        // Configure mock
        wireMock.stubFor(get(urlEqualTo("/api/internal/products/999"))
            .willReturn(aResponse().withStatus(404)));
        
        // Verify error handling
        assertThatThrownBy(() -> productService.getProductById(999L))
            .isInstanceOf(ProductNotFoundException.class);
    }
}
```

## 🚀 Quick Test Commands

### Verify connectivity
```bash
# Basic connectivity test
curl -f -H "X-API-Key: your-secret-api-key-here" \
     http://localhost:8080/actuator/health

# Get products
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products | jq '.'
```

### Development initialization script
```bash
#!/bin/bash
echo "Starting product microservice..."
docker compose -f docker-compose.dev.yml up -d

echo "Waiting for service to be ready..."
until curl -f -s -H "X-API-Key: your-secret-api-key-here" \
           http://localhost:8080/actuator/health > /dev/null; do
    sleep 2
done

echo "✅ Product microservice ready for integrations"
```

## 📋 Integration Checklist

- [ ] Configure WebClient with correct URL and API key
- [ ] Implement error handling (404, 500, timeouts)
- [ ] Configure circuit breaker and retry policies  
- [ ] Create integration tests
- [ ] Configure logging for debugging
- [ ] Document used endpoints
- [ ] Validate performance with expected load