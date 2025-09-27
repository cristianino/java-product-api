# Java Pro- **📖 [API - **🔐 API Key security** via X-API-Key header
- **📋 API Versioning** with multiple concurrent versions (v1, v2) and backward compatibility
- **🐘 PostgreSQL** persistence with JPA
- **🔄 Flyway** database migrations  
- **📚 Swagger/OpenAPI** documentation with microservice examples
- **💊 Health checks** with microservice connectivity monitoring
- **🔗 Connectivity endpoints** for verifying communication with other microservices
- **📝 Structured JSON logging** for Loki/Grafanatation](http://localhost:8080/swagger-ui/index.html)** - Interactive Swagger UI (when running)
- **🏥 [Health Check](http://localhost:8080/actuator/health)** - Application health status
- **🔗 [Connectivity Status](http://localhost:8080/api/connectivity/status)** - Microservice connectivity monitoringct API

A Spring Boot 3 microservice for managing products with **simple and clear APIs** designed for microservice communication. Features JSON:API specification, Hexagonal Architecture, PostgreSQL, and Docker support.

## 📚 Documentation

- **🚀 [Developer Guide](DEVELOPER_GUIDE.md)** - Complete setup, testing, and development guide
- **🔗 [Microservice Integration](MICROSERVICE_INTEGRATION.md)** - **Simple guide for consuming APIs**
- **💡 [Integration Examples](INTEGRATION_EXAMPLES.md)** - **Practical examples for inventory microservice**
- **� [Observability Guide](OBSERVABILITY_GUIDE.md)** - **Loki + Grafana dashboard usage and monitoring**
- **�📖 [API Documentation](http://localhost:8080/swagger-ui/index.html)** - Interactive Swagger UI (when running)
- **🏥 [Health Check](http://localhost:8080/actuator/health)** - Application health status

## Features

- **Spring Boot 3** with **Java 17**
- **Hexagonal Architecture** (Domain, Application, Infrastructure layers)
- **🎯 Dual API Design:**
  - **JSON:API** endpoints for external integrations (`/api/products`)
  - **Simple JSON** endpoints for microservice communication (`/api/internal/products`)
- **🔐 API Key security** via X-API-Key header
- **🐘 PostgreSQL** persistence with JPA
- **🔄 Flyway** database migrations  
- **📚 Swagger/OpenAPI** documentation with microservice examples
- **💊 Health checks** with microservice connectivity monitoring
- **� Connectivity endpoints** for verifying communication with other microservices
- **�📝 Structured JSON logging** for Loki/Grafana
- **🧪 Unit & Integration tests** with JUnit and Testcontainers
- **🐳 Docker** support with multi-environment configurations

## Architecture

The application follows Hexagonal Architecture with clear separation of concerns:

```
src/main/java/com/cristianino/productapi/
├── domain/
│   ├── model/           # Product entity
│   ├── port/            # Repository interfaces
│   └── service/         # Domain business logic
├── application/
│   ├── dto/             # Data Transfer Objects & JSON:API structures
│   └── usecase/         # Application services
└── infrastructure/
    ├── config/          # Security, OpenAPI, etc.
    ├── persistence/     # JPA entities & repository implementations
    └── web/             # REST controllers
```

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (for full setup)

## 🚀 Simple API for Microservices

This service provides **two clear API approaches**:

### For Microservice Communication (Recommended)
```bash
# Get all products (simplified format)
curl -H "X-API-Key: your-secret-api-key-here" \
     http://localhost:8080/api/internal/products

# Response: {"products": [{"id":1,"name":"Product","price":99.99,"availability":true}], "totalCount":1}
```

### For External Integrations (JSON:API Standard)
```bash
# Get products (JSON:API format)
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Content-Type: application/vnd.api+json" \
     http://localhost:8080/api/products
```

**See [MICROSERVICE_INTEGRATION.md](MICROSERVICE_INTEGRATION.md) for complete integration guide.**

## 🔗 Microservice Connectivity

The API includes **connectivity endpoints** to verify communication with other microservices, perfect for health checks and integration testing.

### Connectivity Endpoints

#### Check Inventory Service Connection
```bash
# Check connectivity to inventory microservice
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Accept: application/json" \
     "http://localhost:8080/api/connectivity/inventory"
```

**Response when inventory is available:**
```json
{
  "service": "inventory-api",
  "status": "UP",
  "url": "http://localhost:8082/actuator/health",
  "response": {"status": "UP", "components": {...}},
  "message": "Successfully connected to Inventory service"
}
```

**Response when inventory is unavailable:**
```json
{
  "service": "inventory-api", 
  "status": "DOWN",
  "url": "http://localhost:8082/actuator/health",
  "error": "Connection refused",
  "message": "Failed to connect to Inventory service"
}
```

#### Check Overall Connectivity Status
```bash
# Check status of all connected microservices
curl -H "X-API-Key: your-secret-api-key-here" \
     -H "Accept: application/json" \
     "http://localhost:8080/api/connectivity/status"
```

**Response:**
```json
{
  "overall-status": "UP",  // UP | DEGRADED
  "product-api": {
    "service": "product-api",
    "status": "UP",
    "message": "Product API is running"
  },
  "inventory-api": {
    "service": "inventory-api",
    "status": "UP",
    "url": "http://localhost:8082/actuator/health",
    "response": {...},
    "message": "Successfully connected to Inventory service"
  },
  "timestamp": 1758994357041
}
```

### Configuration for Microservice Communication

The connectivity is configured in `application.yml`:

```yaml
microservices:
  inventory:
    base-url: "http://localhost:8082"
    health-endpoint: "/actuator/health"
```

### Testing Connectivity in Practice

1. **Start Product API:**
   ```bash
   docker compose -f docker-compose.dev.yml up -d
   ```

2. **Start Inventory API on port 8082**

3. **Verify connectivity:**
   ```bash
   # Should return status: "UP" when both services are running
   curl -H "X-API-Key: your-secret-api-key-here" \
        "http://localhost:8080/api/connectivity/status" | jq .
   ```

This connectivity feature is essential for:
- **🏥 Health monitoring** in microservice architectures
- **🔍 Integration testing** and CI/CD pipelines  
- **📊 Service mesh observability**
- **⚡ Quick troubleshooting** of service dependencies

## 📋 API Versioning Strategy

The API implements **URI Path Versioning** for clear version management and backward compatibility.

### Available API Versions

#### 🆕 Version 2.0 (Current - Recommended)
**Base Path:** `/api/v2/products`

**Enhanced Features:**
- **Pagination support** with `page` and `size` parameters
- **Enhanced metadata** in responses (timestamps, version info)
- **Improved error handling** with detailed error codes
- **Additional HATEOAS links** (edit, delete, collection)
- **Version-specific health endpoint** (`/api/v2/products/health`)

```bash
# Get products with pagination
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v2/products?page=0&size=10"

# Enhanced response includes:
{
  "data": [...],
  "links": {
    "self": "/api/v2/products?page=0&size=10",
    "first": "/api/v2/products?page=0&size=10",
    "last": "/api/v2/products?page=2&size=10"
  },
  "meta": {
    "version": "2.0",
    "page": 0,
    "size": 10,
    "total_pages": 3,
    "features": ["pagination", "filtering", "enhanced_metadata"],
    "api_version": "v2"
  }
}
```

#### 🔄 Version 1.0 (Stable)
**Base Path:** `/api/v1/products`

**Standard Features:**
- **Basic CRUD operations** with JSON:API format
- **Simple metadata** and links
- **Standard error handling**

```bash
# Basic usage
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v1/products"

# Response format:
{
  "data": [...],
  "links": {"self": "/api/v1/products"},
  "meta": {"version": "1.0", "count": 5}
}
```

#### ⚠️ Legacy Version (Deprecated)
**Base Path:** `/api/products`

- **Backward compatibility** maintained
- **Deprecated warnings** in documentation
- **Redirects to latest version** behavior

### Version Selection Guidelines

| Use Case | Recommended Version | Reason |
|----------|-------------------|---------|
| **New integrations** | `v2` | Latest features, best performance |
| **Existing stable integrations** | `v1` | Proven stability, no breaking changes |
| **Legacy systems** | `/api/products` | Backward compatibility during migration |

### API Documentation by Version

The Swagger UI provides separate documentation for each version:

- **📖 All Versions:** http://localhost:8080/swagger-ui.html
- **🆕 V2.0 Products:** Select "Products API V2.0 (Current)" group
- **🔄 V1.0 Products:** Select "Products API V1.0" group  
- **⚠️ Legacy:** Select "Legacy API (Deprecated)" group
- **🔗 Connectivity:** Select "Connectivity & Health" group

### Version Migration Strategy

**Backward Compatibility Promise:**
- ✅ **V1 endpoints** will remain functional
- ✅ **Response format** stays consistent within versions
- ✅ **Breaking changes** only in new major versions
- ✅ **Deprecation notices** provided 6+ months in advance

**Migration Path:**
1. **Assess current usage** of legacy endpoints
2. **Test V2 endpoints** in development environment
3. **Gradual migration** of integrations to V2
4. **Monitor both versions** during transition period

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/cristianino/java-product-api.git
   cd java-product-api
   ```

2. **Start with Docker (Recommended)**
   ```bash
   # Development environment with PostgreSQL
   docker compose -f docker-compose.dev.yml up -d
   ```

   This starts the API on **port 8080** with PostgreSQL

3. **Test the APIs**
   ```bash
   # Test internal API (for microservices)
   curl -H "X-API-Key: your-secret-api-key-here" \
        http://localhost:8080/api/internal/products
   ```

4. **Access documentation**
   - **Swagger UI:** http://localhost:8080/swagger-ui.html
   - **Health Check:** http://localhost:8080/actuator/health
   - Health Check: http://localhost:8080/actuator/health

### API Usage

All API endpoints require the `X-API-Key` header. Default key: `your-secret-api-key-here`

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "X-API-Key: your-secret-api-key-here" \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "type": "products",
      "attributes": {
        "name": "Laptop",
        "price": 999.99
      }
    }
  }'
```

#### Get All Products
```bash
curl -X GET http://localhost:8080/api/products \
  -H "X-API-Key: your-secret-api-key-here"
```

#### Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "X-API-Key: your-secret-api-key-here"
```

#### Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "X-API-Key: your-secret-api-key-here" \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "type": "products",
      "attributes": {
        "name": "Updated Laptop",
        "price": 1199.99
      }
    }
  }'
```

#### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "X-API-Key: your-secret-api-key-here"
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/productdb` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `productuser` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `productpass` |
| `APP_API_KEY` | API key for authentication | `your-secret-api-key-here` |
| `SPRING_PROFILES_ACTIVE` | Active profiles | - |

### Application Profiles

- **default**: Uses PostgreSQL, console logging
- **prod**: Uses PostgreSQL, structured JSON logging
- **test**: Uses H2 in-memory database for testing

## Development

### Running Tests

```bash
# Run all tests with coverage report
mvn clean test jacoco:report

# Run only unit tests
mvn test -Dtest="*Test"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"

# Run tests in Docker
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-17 mvn clean test jacoco:report
```

**📊 Test Coverage Achieved:**
- **✅ 112 tests** implemented across all layers
- **📝 87.69% line coverage** (292/333 lines)
- **🔧 88.08% instruction coverage** (1,204/1,367 instructions)
- **⚙️ 85.94% method coverage** (110/128 methods)

*For detailed testing information, see [Developer Guide](DEVELOPER_GUIDE.md#-ejecutar-tests)*

### Database Migrations

Database schema is managed with Flyway. Migration scripts are in `src/main/resources/db/migration/`.

### Code Structure

The application follows these principles:
- **Domain-driven design** with clear boundaries
- **Dependency inversion** - domain doesn't depend on infrastructure
- **JSON:API specification** for consistent API responses
- **Comprehensive testing** with unit and integration tests

## 📊 Monitoring & Observability

### Health Checks
- Endpoint: `/actuator/health`

### 📈 Loki + Grafana Stack

El proyecto incluye un stack completo de observabilidad con Loki y Grafana para visualización de logs centralizada.

#### Configuración incluida:
- **Loki**: Agregación de logs centralizada
- **Promtail**: Recolección de logs de contenedores Docker 
- **Grafana**: Dashboards interactivos para visualización de logs

#### Acceso rápido:
```bash
# Iniciar el stack completo incluyendo observabilidad
docker compose up -d

# Acceder a Grafana
http://localhost:3000
# Usuario: admin, Contraseña: admin123
```

#### URLs de los servicios:
- **🎯 Grafana Dashboard**: http://localhost:3000
- **📊 Loki API**: http://localhost:3100
- **🔍 Loki Health**: http://localhost:3100/ready

#### Dashboards incluidos:
- **Java Product API - Logs Dashboard**: Visualización completa de logs de la aplicación
  - Distribución de niveles de log (INFO, ERROR, WARN, DEBUG)
  - Rate de logs por nivel en tiempo real
  - Logs de aplicación con búsqueda y filtrado
  - Panel específico para logs de ERROR

#### Características de logging:
- **Logs estructurados en JSON** enviados directamente a Loki
- **Etiquetas automáticas**: `application=java-product-api`, `host`, `level`
- **Metadatos incluidos**: timestamp, thread, logger, MDC context, excepciones
- **Configuración dual**: logs en consola para desarrollo + Loki para producción

#### 📖 Detailed Documentation:
Para una guía completa sobre cómo usar los dashboards, interpretar las métricas y personalizar las queries, consulta la **[Observability Guide](OBSERVABILITY_GUIDE.md)**.
- Shows application and database health

### Metrics
- Endpoint: `/actuator/metrics`
- Standard Spring Boot metrics

### Logging
- **Development**: Console logging with colors
- **Production**: Structured JSON logging for Loki/Grafana integration

## Docker

### Build Image
```bash
docker build -t java-product-api .
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/productdb \
  -e SPRING_DATASOURCE_USERNAME=productuser \
  -e SPRING_DATASOURCE_PASSWORD=productpass \
  java-product-api
```

## 🚀 Quick Reference

| Command | Description |
|---------|-------------|
| `docker compose -f docker-compose.dev.yml up -d` | Start full application stack |
| `mvn clean test jacoco:report` | Run all tests with coverage |
| `mvn spring-boot:run` | Start application locally |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v2/products` | **Test V2 API (Recommended)** |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v1/products` | Test V1 API (Stable) |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v2/products/health` | V2 API Health Check |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/connectivity/status` | Check microservice connectivity |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/connectivity/inventory` | Check inventory service connection |

**📖 Need more details?** Check out the comprehensive [Developer Guide](DEVELOPER_GUIDE.md) for:
- 🏗️ Architecture deep-dive
- 🛠️ Environment setup
- 🧪 Testing strategies  
- 🔧 API examples
- 🐳 Docker workflows
- 🎯 Troubleshooting

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass (`mvn test`)
6. Check coverage (`mvn jacoco:report`)
7. Submit a pull request

**📋 PR Checklist:** All 112 tests passing ✅ | Coverage >85% ✅ | Documentation updated ✅

## License

This project is licensed under the MIT License.