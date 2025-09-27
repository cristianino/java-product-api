# Ejemplos Prácticos - Integración Microservicio de Inventario

## 🎯 Casos de Uso Reales

### 1. Sincronización Inicial de Catálogo

Cuando el microservicio de inventario se inicia, necesita sincronizar todos los productos:

```java
@Component
public class ProductCatalogSynchronizer {

    private final WebClient productServiceClient;
    
    @EventListener(ApplicationReadyEvent.class)
    public void synchronizeProductCatalog() {
        log.info("Iniciando sincronización de catálogo de productos...");
        
        // Obtener todos los productos del microservicio de productos
        ProductListResponse products = productServiceClient
            .get()
            .uri("/api/internal/products")
            .retrieve()
            .bodyToMono(ProductListResponse.class)
            .block();
            
        // Crear registros de inventario para productos nuevos
        products.getProducts().forEach(product -> {
            if (!inventoryExists(product.getId())) {
                createInventoryRecord(product);
                log.info("Creado inventario para producto: {}", product.getName());
            }
        });
        
        log.info("Sincronización completada. {} productos procesados", 
                 products.getTotalCount());
    }
}
```

### 2. Validación de Producto Antes de Crear Inventario

Antes de crear un nuevo registro de inventario, validar que el producto existe:

```java
@Service
public class InventoryService {

    public InventoryRecord createInventory(Long productId, int quantity) {
        // 1. Verificar que el producto existe
        ProductDto product = validateProductExists(productId);
        
        // 2. Crear registro de inventario
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
            throw new ProductNotFoundException("Producto no encontrado: " + productId);
        }
    }
}
```

### 3. Actualización Periódica de Disponibilidad

Sincronizar periódicamente la disponibilidad de productos:

```java
@Component
public class ProductAvailabilityUpdater {

    @Scheduled(fixedDelay = 300000) // Cada 5 minutos
    public void updateProductAvailability() {
        log.debug("Actualizando disponibilidad de productos...");
        
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
                    log.info("Actualizada disponibilidad del producto {}: {}", 
                             inventory.getProductId(), availability.isAvailable());
                }
            } catch (Exception e) {
                log.error("Error actualizando disponibilidad del producto {}: {}", 
                          inventory.getProductId(), e.getMessage());
            }
        });
    }
}
```

### 4. Operación Batch para Múltiples Productos

Cuando necesitas información de varios productos a la vez:

```java
@Service
public class BulkInventoryService {

    public List<InventoryDto> getInventoryForProducts(List<Long> productIds) {
        // 1. Obtener información de productos en batch
        BatchProductRequest request = new BatchProductRequest();
        request.setProductIds(productIds);
        
        ProductListResponse products = productServiceClient
            .post()
            .uri("/api/internal/products/batch")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ProductListResponse.class)
            .block();
        
        // 2. Combinar con información de inventario local
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

## 🛠️ Configuración WebClient

### Configuración Básica

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

### Configuración Avanzada con Resilience4j

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

## 📝 DTOs del Microservicio de Inventario

```java
// Respuesta del servicio de productos
public class ProductListResponse {
    private List<ProductDto> products;
    private int totalCount;
    // getters y setters
}

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean availability;
    // getters y setters
}

public class AvailabilityResponse {
    private Long productId;
    private boolean available;
    // getters y setters
}

// Request para operaciones batch
public class BatchProductRequest {
    private List<Long> productIds;
    // getters y setters
}
```

## 🧪 Ejemplos de Testing

### Test de Integración

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

### Test con WireMock

```java
@SpringBootTest
class ProductServiceMockTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().port(8089))
        .build();

    @Test
    void shouldHandleProductNotFound() {
        // Configurar mock
        wireMock.stubFor(get(urlEqualTo("/api/internal/products/999"))
            .willReturn(aResponse().withStatus(404)));
        
        // Verificar manejo de error
        assertThatThrownBy(() -> productService.getProductById(999L))
            .isInstanceOf(ProductNotFoundException.class);
    }
}
```

## 🚀 Comandos de Prueba Rápida

### Verificar conectividad
```bash
# Test básico de conectividad
curl -f -H "X-API-Key: your-secret-api-key-here" \
     http://localhost:8080/actuator/health

# Obtener productos
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products | jq '.'
```

### Script de inicialización para desarrollo
```bash
#!/bin/bash
echo "Iniciando microservicio de productos..."
docker compose -f docker-compose.dev.yml up -d

echo "Esperando que el servicio esté listo..."
until curl -f -s -H "X-API-Key: your-secret-api-key-here" \
           http://localhost:8080/actuator/health > /dev/null; do
    sleep 2
done

echo "✅ Microservicio de productos listo para integraciones"
```

## 📋 Checklist de Integración

- [ ] Configurar WebClient con URL y API key correctas
- [ ] Implementar manejo de errores (404, 500, timeouts)
- [ ] Configurar circuit breaker y retry policies  
- [ ] Crear tests de integración
- [ ] Configurar logging para debugging
- [ ] Documentar endpoints usados
- [ ] Validar performance con carga esperada