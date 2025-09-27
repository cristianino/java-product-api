# Guía de Integración para Microservicios

## 📋 Resumen

Este microservicio de productos proporciona APIs simples y claras para ser consumido por otros microservicios, específicamente el **microservicio de inventario**.

## 🔑 Autenticación

Todas las APIs requieren el header de autenticación:
```
X-API-Key: your-secret-api-key-here
```

## 🎯 APIs Disponibles para Microservicios

### 1. API Pública JSON:API (Recomendada para integraciones externas)

**Base URL:** `http://localhost:8080/api/products`

#### Obtener todos los productos
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/vnd.api+json" \
     http://localhost:8080/api/products
```

#### Obtener un producto específico
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/vnd.api+json" \
     http://localhost:8080/api/products/1
```

### 2. API Interna Simplificada (Recomendada para microservicios)

**Base URL:** `http://localhost:8080/api/internal/products`

#### Obtener todos los productos (formato simplificado)
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products
```

**Respuesta:**
```json
{
  "products": [
    {
      "id": 1,
      "name": "Laptop Dell XPS 13",
      "price": 999.99,
      "availability": true
    }
  ],
  "totalCount": 1
}
```

#### Obtener un producto específico
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Laptop Dell XPS 13", 
  "price": 999.99,
  "availability": true
}
```

#### Obtener múltiples productos por IDs
```bash
curl -X POST \
     -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     -d '{"productIds": [1, 2, 3]}' \
     http://localhost:8080/api/internal/products/batch
```

#### Verificar disponibilidad de un producto
```bash
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/internal/products/1/availability
```

**Respuesta:**
```json
{
  "productId": 1,
  "available": true
}
```

## 🏗️ Integración con Microservicio de Inventario

### Escenario Típico de Uso

1. **Sincronización de Catálogo:** El microservicio de inventario obtiene todos los productos
2. **Validación de Producto:** Antes de crear inventario, verifica que el producto existe
3. **Actualización de Disponibilidad:** Consulta la disponibilidad actual del producto

### Ejemplo de Implementación en Java (Microservicio de Inventario)

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

Verificar el estado del microservicio:
```bash
curl http://localhost:8080/actuator/health
```

## 🐳 Configuración de Entornos

### Desarrollo (puerto 8080)
```bash
docker compose -f docker-compose.dev.yml up -d
```

### Testing (puerto 8082) 
```bash
# Configurar perfil de test
java -Dspring.profiles.active=test -jar target/product-api-*.jar
```

### Producción
```bash
# Variables de entorno para configuración
export DATABASE_URL=jdbc:postgresql://prod-db:5432/products
export API_KEY=production-secret-key
export INVENTORY_SERVICE_URL=http://inventory-service:8080
```

## 📊 Monitoreo y Logs

- **Health Check:** `/actuator/health`
- **Metrics:** `/actuator/metrics`  
- **Logs:** Configurados con nivel DEBUG para comunicación entre microservicios

## ⚠️ Consideraciones Importantes

1. **API Key:** Usa siempre el header `X-API-Key` para autenticación
2. **Content-Type:** Para API interna usa `application/json`, para API pública usa `application/vnd.api+json`
3. **Timeouts:** Configura timeouts apropiados en tu WebClient (recomendado: 5 segundos)
4. **Retry Policy:** Implementa retry con backoff exponencial para llamadas fallidas
5. **Circuit Breaker:** Considera usar Resilience4j para manejo de fallos

## 🚀 Próximos Pasos

1. El microservicio de inventario debe implementar estas llamadas
2. Configurar service discovery (Eureka) para producción  
3. Implementar circuit breakers para mayor resiliencia
4. Añadir métricas de comunicación entre servicios