# Java Pro- **📖 [API - **🔐 API Key security** via X-API-Key header
- **📋 API Versioning Strategy** with V1.0 implementation and V2.0 roadmap ready
- **🐘 PostgreSQL** persistence with JPA
- **🔄 Flyway** database migrations  
- **📚 Swagger/OpenAPI** documentation with microservice examples
- **💊 Health checks** with microservice connectivity monitoring
- **🔗 Connectivity endpoints** for verifying com## 🧪 Testing and Code Quality

### Test Coverage
- **✅ 47 tests implemented** distributed across all layers:
  - **🔬 30 Unit Tests**: Domain, Application, and Infrastructure
  - **🔗 17 Integration Tests**: End-to-end with TestContainers
- **🎯 88% instruction coverage** (1,204/1,367 instructions)

### Coverage Distribution by Package
| Layer | Coverage | Status |
|-------|----------|--------|
| **Infrastructure layer** | **100%** | ✅ Complete |
| **Application UseCase** | **96%** | ✅ Excellent |
| **Domain Model** | **95%** | ✅ Excellent |
| **Application DTO** | **82%** | ✅ Good |
| **Domain Service** | **42%** | ⚠️ Improvable |
| **Main Application Class** | **37%** | ⚠️ Improvable |

### Running Tests

```bash
# Run all tests
mvn clean test

# Run tests with coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Types of Tests Implemented

#### Unit Tests (30)
- **Domain Model Tests**: Entity and domain logic validation
- **UseCase Tests**: Use case testing with mocks
- **DTO Tests**: Data transformation validation
- **Infrastructure Tests**: Adapter testing

#### Integration Tests (17)
- **API Tests**: End-to-end REST endpoint testing  
- **Database Tests**: PostgreSQL integration using TestContainers
- **Security Tests**: Authentication and authorization validation
- **Health Check Tests**: Health endpoint verification

## 🔍 API Verification and Testing

### Quick System Verification

```bash
# 1. Verify all services are running
docker-compose ps

# 2. Test application health check
curl http://localhost:8080/actuator/health

# 3. Test main endpoint with API Key
curl -H "X-API-Key: dev_api_key_for_local_development_12345" \
     http://localhost:8080/api/v1/products

# 4. Verify microservice connectivity
curl -H "X-API-Key: dev_api_key_for_local_development_12345" \
     http://localhost:8080/api/connectivity/status
```

### API Usage Examples

#### Get all products
```bash
curl -H "X-API-Key: dev_api_key_for_local_development_12345" \
     -H "Accept: application/vnd.api+json" \
     http://localhost:8080/api/v1/products
```

#### Get a specific product
```bash
curl -H "X-API-Key: dev_api_key_for_local_development_12345" \
     -H "Accept: application/vnd.api+json" \
     http://localhost:8080/api/v1/products/1
```

#### Create a new product
```bash
curl -X POST \
     -H "X-API-Key: dev_api_key_for_local_development_12345" \
     -H "Content-Type: application/vnd.api+json" \
     -d '{
       "data": {
         "type": "products",
         "attributes": {
           "name": "New Product",
           "price": 199.99
         }
       }
     }' \
     http://localhost:8080/api/v1/products
```

#### Update a product
```bash
curl -X PUT \
     -H "X-API-Key: dev_api_key_for_local_development_12345" \
     -H "Content-Type: application/vnd.api+json" \
     -d '{
       "data": {
         "type": "products",
         "id": "1",
         "attributes": {
           "name": "Updated Product",
           "price": 299.99
         }
       }
     }' \
     http://localhost:8080/api/v1/products/1
```

### Monitoring and Observability

#### Access Grafana
1. Open http://localhost:3000
2. Login: `admin` / `admin123`
3. Go to Dashboards → Java Product API Logs
4. View application metrics

#### View structured logs
```bash
# View application logs
docker-compose logs -f java-product-api

# View specific query logs
docker-compose logs java-product-api | grep "GET /api/v1/products"
```

*For detailed testing information, see [Developer Guide](DEVELOPER_GUIDE.md#-running-tests)*her microservices
- **📝 Structured JSON logging** for Loki/Grafanatation](http://localhost:8080/swagger-ui/index.html)** - Interactive Swagger UI (when running)
- **🏥 [Health Check](http://localhost:8080/actuator/health)** - Application health status
- **🔗 [Connectivity Status](http://localhost:8080/api/connectivity/status)** - Microservice connectivity monitoringct API

# Java Product API

A REST API developed with Spring Boot 3 for product management, designed as part of a microservices architecture. Implements modern design patterns, complete observability, and is production-ready.

## 📋 Project Description

This API is part of a **backend technical assessment** that demonstrates:

- **Hexagonal Architecture** with clear separation of concerns
- **RESTful APIs** following JSON:API and OpenAPI standards
- **Security** through API Key authentication
- **Complete observability** with Loki, Grafana, and Promtail
- **Containerization** with Docker and Docker Compose
- **Robust testing** with 88% coverage (47 tests implemented)
- **Comprehensive technical documentation** and integration guides

## 🏗️ Technical Features

### Core Framework
- **Spring Boot 3.2.0** with Java 17
- **Spring Data JPA** for persistence
- **Spring Security** for authentication
- **Flyway** for database migrations

### Architecture and Patterns
- **Hexagonal Architecture** (Domain, Application, Infrastructure)
- **Domain-Driven Design** with domain entities and services
- **Dependency Injection** and SOLID principles
- **API Versioning** with URI Path strategy

### Observability and Monitoring
- **Structured Logging** with JSON format for Loki
- **Custom Health Checks** for microservices
- **Grafana Dashboards** for real-time monitoring
- **Connectivity Endpoints** to verify service status

### Testing and Quality
- **88% code coverage** (target: 90%)
- **47 tests implemented**: 30 unit + 17 integration tests
- **TestContainers** for PostgreSQL integration testing
- **JaCoCo** for coverage reports

## 📚 Technical Documentation

| Document | Description |
|----------|-------------|
| **🚀 [Developer Guide](DEVELOPER_GUIDE.md)** | Complete development and setup guide |
| **🔗 [Microservice Integration](MICROSERVICE_INTEGRATION.md)** | Integration guide for microservices |
| **💡 [Integration Examples](INTEGRATION_EXAMPLES.md)** | Practical API usage examples |
| **📊 [Observability Guide](OBSERVABILITY_GUIDE.md)** | Loki + Grafana setup and usage |
| **📖 [API Versioning Guide](API_VERSIONING_GUIDE.md)** | API versioning strategy |
| **📝 [Microservice Status](MICROSERVICE_STATUS.md)** | Current architecture status |

## 🌐 Important Links

- **📖 [API Documentation](http://localhost:8080/swagger-ui/index.html)** - Interactive Swagger UI
- **🏥 [Health Check](http://localhost:8080/actuator/health)** - Application status
- **🔗 [Connectivity Status](http://localhost:8080/api/connectivity/status)** - Microservices monitoring
- **📊 [Grafana Dashboard](http://localhost:3000)** - Observability dashboards

## 🎯 Main Features

### REST APIs
- **JSON:API compliant** endpoints for external integrations  
- **Simple JSON** endpoints optimized for microservice communication
- **API Versioning** with stable V1.0 and V2.0 roadmap
- **Robust validation** of input data

### Security
- **API Key Authentication** via `X-API-Key` header
- **CORS** configured for development and production
- **Input validation** with Bean Validation
- **Security headers** configured

### Persistence
- **PostgreSQL** as primary database
- **JPA/Hibernate** for ORM
- **Flyway** for database version control
- **Optimized connection pooling**

### Observability
- **Structured JSON logging** for Loki aggregation
- **Custom metrics** for Grafana dashboards
- **Custom health indicators**
- **Distributed tracing** ready

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

## 🏛️ System Architecture

### Hexagonal Architecture (Ports & Adapters)

The application implements **Hexagonal Architecture** to keep the domain independent of external frameworks and technologies:

```
src/main/java/com/cristianino/productapi/
├── 🎯 domain/                    # BUSINESS CORE
│   ├── model/                   # Domain entities (Product)
│   ├── port/                    # Interfaces (ProductRepository)
│   └── service/                 # Pure business logic
├── 🔄 application/              # USE CASES
│   ├── dto/                     # DTOs and JSON:API structures
│   └── usecase/                 # Application services (ProductUseCase)
└── 🔧 infrastructure/           # EXTERNAL ADAPTERS
    ├── config/                  # Configurations (Security, OpenAPI)
    ├── persistence/             # JPA adapters (ProductEntity, Repository)
    ├── service/                 # Infrastructure services
    ├── util/                    # Utilities and helpers
    └── web/                     # REST controllers
```

### Applied Design Principles

- **🎯 Domain-Driven Design**: Domain is completely isolated
- **🔄 Dependency Inversion**: Dependencies point towards the domain
- **🔧 Single Responsibility**: Each layer has a specific responsibility
- **📦 Clean Code**: Readable, maintainable, and testable code

### Implemented Patterns

| Pattern | Location | Purpose |
|---------|----------|---------|
| **Repository** | `domain/port/` | Persistence abstraction |
| **Use Case** | `application/usecase/` | Business logic orchestration |
| **DTO** | `application/dto/` | Data transfer |
| **Entity** | `domain/model/` | Domain modeling |
| **Adapter** | `infrastructure/` | Port implementations |

### Technology Stack

#### Backend Core
- **Spring Boot 3.2.0** - Main framework
- **Spring Data JPA** - Persistence and ORM
- **Spring Security** - Authentication and authorization
- **Spring Web** - REST APIs
- **Flyway** - Database migrations

#### Database
- **PostgreSQL 15** - Primary database
- **HikariCP** - Connection pooling
- **JPA/Hibernate** - ORM

#### Observability
- **Grafana 10.1.0** - Dashboards and visualization
- **Loki 2.9.0** - Log aggregation
- **Promtail 2.9.0** - Log collection
- **Logback** - Structured logging

#### Testing
- **JUnit 5** - Testing framework
- **TestContainers** - Integration tests
- **Mockito** - Mocking
- **JaCoCo** - Code coverage

#### DevOps
- **Docker & Docker Compose** - Containerization
- **Maven** - Build and dependency management
- **OpenAPI 3** - API documentation

## 🚀 Installation and Setup

### Prerequisites

- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.6+** for build and dependency management
- **Docker & Docker Compose** for complete environment
- **Git** for repository cloning

### 🐳 Quick Start with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/cristianino/java-product-api.git
cd java-product-api

# 2. Setup environment variables
cp .env.example .env
# Edit .env with your configurations if needed

# 3. Start all services
docker-compose up --build -d

# 4. Verify all services are running
docker-compose ps

# 5. Test the API
curl -H "X-API-Key: dev_api_key_for_local_development_12345" \
     http://localhost:8080/api/v1/products
```

### 🔧 Local Development

```bash
# 1. Setup local PostgreSQL database
# Create 'productdb' database with 'productuser' user

# 2. Configure application-dev.yml with your credentials

# 3. Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 4. Run tests
mvn clean test jacoco:report
```

### 🌐 Access URLs

Once services are running, you'll have access to:

| Service | URL | Credentials |
|---------|-----|-------------|
| **Product API** | http://localhost:8080 | API Key: `dev_api_key_for_local_development_12345` |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **Health Check** | http://localhost:8080/actuator/health | - |
| **Grafana** | http://localhost:3000 | admin / admin123 |
| **PostgreSQL** | localhost:5432 | productuser / dev_password_123 |

### ⚙️ Environment Configuration

#### Main Environment Variables

```bash
# Database
POSTGRES_DB=productdb
POSTGRES_USER=productuser
POSTGRES_PASSWORD=dev_password_123

# Application
SPRING_PROFILES_ACTIVE=dev
APP_API_KEY=dev_api_key_for_local_development_12345

# Grafana
GRAFANA_ADMIN_PASSWORD=admin123

# JVM (Production)
JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC
```

#### Spring Profiles

- **`dev`**: Local development with detailed logs
- **`prod`**: Production with structured JSON logs
- **`test`**: Testing with in-memory database

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

The API implements **URI Path Versioning** for clear version management and future scalability.

### Current Implementation

#### ✅ Version 1.0 (Current & Stable)
**Base Path:** `/api/v1/products`

**Features:**
- **CRUD operations** with JSON:API format
- **Basic metadata** and links
- **Proven stability** and reliability
- **Foundation** for V2.0 development

```bash
# Current V1.0 usage
curl -H "X-API-Key: your-secret-api-key-here" \
     "http://localhost:8080/api/v1/products"

# Response format:
{
  "data": [...],
  "links": {"self": "/api/v1/products"},
  "meta": {"version": "1.0", "count": 5}
}
```

#### ✅ Default Version
**Base Path:** `/api/products`

**Purpose:**
- **Simple integration** for basic use cases
- **Backward compatibility** maintained
- **Same functionality** as V1.0 without version prefix

### 🚀 V2.0 Development Roadmap

#### � Version 2.0 (Planned - Ready for Implementation)
**Future Base Path:** `/api/v2/products`

**Planned Enhanced Features:**
- ✨ **Advanced pagination** (`page`, `size`, `sort` parameters)
- ✨ **Rich filtering** (by name, price range, category)
- ✨ **Enhanced metadata** (timestamps, performance metrics)
- ✨ **Improved HATEOAS links** (edit, delete, related resources)
- ✨ **Version-specific health endpoint**
- ✨ **Bulk operations** support
- ✨ **Enhanced error handling** with detailed error codes

**Proposed V2.0 Response Format:**
```json
{
  "data": [...],
  "links": {
    "self": "/api/v2/products?page=0&size=20",
    "first": "/api/v2/products?page=0&size=20",
    "last": "/api/v2/products?page=5&size=20",
    "next": "/api/v2/products?page=1&size=20"
  },
  "meta": {
    "version": "2.0",
    "page": 0,
    "size": 20,
    "total_pages": 6,
    "features": ["pagination", "filtering", "bulk_operations"],
    "retrieved_at": "2025-09-27T17:30:22.123Z"
  }
}
```

### Implementation Guidelines

| Use Case | Current Recommendation | Future with V2.0 |
|----------|----------------------|-------------------|
| **New integrations** | `v1` - Stable & proven | `v2` - Enhanced features |
| **Existing integrations** | `v1` - No changes needed | `v1` - Maintain compatibility |
| **Simple use cases** | `/api/products` - Easy setup | `/api/products` - Continues working |

### API Documentation

The Swagger UI provides separate documentation groups:

- **📖 All Versions:** http://localhost:8080/swagger-ui.html
- **✅ V1.0 Products:** Select "Products API V1.0 (Current)" group
- **✅ Default Products:** Select "Products API (Default)" group  
- **🔗 Connectivity:** Select "Connectivity & Health" group

### 🛠️ V2.0 Development Guide

For developers ready to implement V2.0, see the comprehensive **[API Versioning Guide](API_VERSIONING_GUIDE.md)** which includes:

- 📋 **Complete V2.0 feature specifications**
- 🏗️ **Step-by-step implementation guide**
- 🧩 **Controller templates and code examples**
- 🧪 **Testing strategies and test templates**
- 📚 **OpenAPI configuration updates**

**Quick Start for V2.0 Development:**
1. Review the [API_VERSIONING_GUIDE.md](API_VERSIONING_GUIDE.md)
2. Create `v2/ProductControllerV2.java` using provided templates
3. Add enhanced features incrementally
4. Update OpenAPI configuration
5. Implement comprehensive tests

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
- **✅ 47 tests** implemented across all layers
- **🎯 88% instruction coverage** (1,204/1,367 instructions)
- **� Coverage by Package:**
  - Infrastructure layer: **100%** coverage
  - Application UseCase: **96%** coverage  
  - Domain Model: **95%** coverage
  - Application DTO: **82%** coverage
  - Domain Service: **42%** coverage
  - Main Application Class: **37%** coverage

**📝 Coverage Analysis:**
- **Target**: 90% total coverage
- **Current**: 88% total coverage 
- **Gap**: 2 percentage points (≈27 instructions)
- **Status**: Comprehensive test suite with room for domain service improvements

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
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/v1/products` | **Test V1 API (Current & Recommended)** |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/products` | Test Default API |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/connectivity/status` | Check microservice connectivity |
| `curl -H "X-API-Key: your-secret-api-key-here" http://localhost:8080/api/connectivity/inventory` | Check inventory service connection |

**📖 Need more details?** Check out the comprehensive [Developer Guide](DEVELOPER_GUIDE.md) for:
- 🏗️ Architecture deep-dive
- 🛠️ Environment setup
- 🧪 Testing strategies  
- 🔧 API examples
- 🐳 Docker workflows
- 🎯 Troubleshooting

## 🚀 Recommended Future Improvements

### 📊 Observability and Monitoring

#### Isolate Grafana in a dedicated container
- **Current issue**: Grafana is included in the same docker-compose as the application
- **Recommended solution**: 
  - Move Grafana to a dedicated cluster or separate docker-compose
  - Implement Grafana as a managed service (Grafana Cloud or similar)
  - Configure dashboards as code with automatic provisioning
- **Benefits**: 
  - Separation of concerns between application and observability
  - Independent scalability of monitoring tools
  - Better management of centralized configurations and dashboards

### 🗄️ Database and Persistence

#### Replace containerized PostgreSQL with managed service
- **Current issue**: PostgreSQL running in Docker containers
- **Recommended solution**:
  - **AWS RDS PostgreSQL** for AWS environments
  - **Google Cloud SQL** for GCP environments
  - **Azure Database for PostgreSQL** for Azure environments
  - **Implement external connection pooling** (PgBouncer)
- **Benefits**:
  - Automatic backups and point-in-time recovery
  - High availability and automatic failover
  - Managed vertical and horizontal scalability
  - Automatic security updates
  - Integrated monitoring and alerts

### 🏗️ Architecture and Scalability

#### Service Mesh Implementation
- **Istio** or **Linkerd** for microservice communication
- Automatic **circuit breakers** and **retry policies**
- Network-level **observability** (distributed tracing)

#### API Gateway
- **Kong**, **Ambassador**, or **AWS API Gateway**
- Centralized authentication and rate limiting
- Intelligent routing and load balancing

#### Event-Driven Architecture
- **Apache Kafka** or **AWS EventBridge** for asynchronous events
- **CQRS** pattern to separate commands from queries
- **Event Sourcing** for complete audit trail

### 🔐 Security

#### Advanced Authentication and Authorization
- **OAuth 2.0** with **JWT tokens**
- **Role-Based Access Control (RBAC)**
- **Integration with Identity Providers** (Auth0, Keycloak)

#### Secrets Management
- **HashiCorp Vault** or **AWS Secrets Manager**
- **Automatic credential rotation**
- **Encryption at rest** and **in transit**

### 🧪 Testing and Quality

#### Achieve 90%+ coverage
- Implement missing tests in **Domain Services** (currently 42%)
- **Mutation testing** with PIT
- **Contract testing** with Pact

#### Advanced CI/CD
- **Multi-environment deployment pipeline**
- **Blue-Green deployments** or **Canary releases**
- **Automated rollback** on errors

### 📈 Performance and Scalability

#### Caching Strategy
- **Redis** for distributed caching
- **CDN** for static content
- **Database query optimization** and indexing

#### Horizontal Scaling
- **Kubernetes** for container orchestration
- **Horizontal Pod Autoscaler** based on metrics
- **Distributed session management**

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass (`mvn test`)
6. Check coverage (`mvn jacoco:report`)
7. Submit a pull request

**📋 PR Checklist:** All 47 tests passing ✅ | Coverage 88% (Target: 90%) ✅ | Documentation updated ✅

## License

This project is licensed under the MIT License.