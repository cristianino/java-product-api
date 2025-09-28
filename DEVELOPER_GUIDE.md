# 🚀 Developer Guide - Product API

## 📋 Table of Contents
- [🏗️ Project Architecture](#️-project-architecture)
- [🛠️ Environment Setup](#️-environment-setup)
- [🚀 Starting the Project](#-starting-the-project)
- [🧪 Running Tests](#-running-tests)
- [📊 Coverage Reports](#-coverage-reports)
- [🔧 API Endpoints](#-api-endpoints)
- [🔐 Authentication](#-authentication)
- [🐳 Docker](#-docker)
- [📚 JSON:API Specification](#-jsonapi-specification)
- [🎯 Troubleshooting](#-troubleshooting)

---

## 🏗️ Project Architecture

This project follows **Hexagonal Architecture** (Clean Architecture) principles with the following layers:

```
src/main/java/com/cristianino/productapi/
├── 📁 domain/              # Domain Layer (Business logic)
│   ├── model/              # Domain entities
│   ├── port/               # Interfaces (ports)
│   └── service/            # Domain services
├── 📁 application/         # Application Layer (Use cases)
│   ├── dto/                # DTOs and transfer objects
│   └── usecase/            # Use cases
├── 📁 infrastructure/      # Infrastructure Layer (Adapters)
│   ├── config/             # Configuration (Security, OpenAPI)
│   ├── persistence/        # JPA Entities and Repositories
│   └── web/                # REST Controllers
└── ProductApiApplication.java # Main Spring Boot class
```

### 🎯 Applied Principles:
- **DDD (Domain-Driven Design)**: Clear separation of responsibilities
- **SOLID**: Object-oriented design principles
- **JSON:API**: Standard specification for REST APIs
- **TDD**: Test-driven development (112 tests implemented)

---

## 🛠️ Environment Setup

### 📋 Prerequisites:
- **Java 17** or higher
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **PostgreSQL 15** (if not using Docker)

### 🔧 Environment Variables:
```bash
# Database
POSTGRES_DB=productdb
POSTGRES_USER=productuser
POSTGRES_PASSWORD=productpass
POSTGRES_HOST=localhost
POSTGRES_PORT=5432

# API Key for authentication
API_KEY=your-secret-api-key

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
```

### 📁 Configuration Structure:
```
src/main/resources/
├── application.yml          # Main configuration
├── application-dev.yml      # Development configuration
├── application-prod.yml     # Production configuration
├── logback-spring.xml       # Logging configuration
└── db/migration/
    └── V1__Create_products_table.sql  # Flyway migration
```

---

## 🚀 Starting the Project

### 🐳 Option 1: With Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/cristianino/java-product-api.git
cd java-product-api

# 2. Start the database
docker-compose up -d postgres

# 3. Build and run the application
docker-compose up api
```

### 💻 Option 2: Local Development

```bash
# 1. Start PostgreSQL
docker-compose up -d postgres

# 2. Build the project
mvn clean compile

# 3. Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or alternatively:
java -jar target/java-product-api-1.0.0.jar --spring.profiles.active=dev
```

### 🌐 Application Access:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Actuator Health**: http://localhost:8080/actuator/health

---

## 🧪 Running Tests

### 📊 Complete Test Suite (112 tests):

```bash
# Run all tests with coverage report
mvn clean test jacoco:report

# Run only unit tests
mvn test -Dtest="*Test"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"

# Run tests with specific profile
mvn test -Dspring.profiles.active=test
```

### 🐳 Running Tests in Docker:

```bash
# Complete option with Maven in Docker
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-17 mvn clean test jacoco:report

# With docker-compose
docker-compose run --rm test
```

### 📁 Implemented Test Types:

#### 🔬 Unit Tests:
- **ProductTest** (21 tests): Domain model tests
- **ProductDtoTest** (10 tests): DTOs and serialization tests
- **ProductUseCaseTest** (14 tests): Use case tests
- **JsonApiErrorTest** (9 tests): Error handling tests
- **JsonApiResponseTest** (7 tests): API response tests

#### 🔧 Infrastructure Tests:
- **ProductRepositoryImplTest** (9 tests): JPA repository tests
- **ProductEntityTest** (7 tests): JPA entity tests
- **SecurityConfigTest** (2 tests): Security configuration tests
- **ApiKeyAuthenticationFilterTest** (6 tests): Authentication filter tests

#### 🌐 Web Tests:
- **ProductControllerUnitTest** (10 tests): Controller tests (MockMvc)
- **ProductControllerTest** (3 tests): HTTP integration tests
- **JsonApiRequestTest** (5 tests): JSON:API request tests

#### 📊 Integration Tests:
- **ProductIntegrationTest** (9 tests): End-to-end tests with TestContainers

---

## 📊 Coverage Reports

### 🎯 Current Coverage Achieved:
- 🔧 **Instructions: 88.08%** (1,204/1,367)
- 🔀 **Branches: 67.50%** (81/120)
- 📝 **Lines: 87.69%** (292/333) ⭐
- ⚙️ **Methods: 85.94%** (110/128)

### 📈 View Reports:

```bash
# Generate JaCoCo HTML report
mvn jacoco:report

# Report location
open target/site/jacoco/index.html

# View CSV report
cat target/site/jacoco/jacoco.csv
```

### 🏆 Classes with Complete Coverage (100%):
- ProductController
- ProductEntity  
- ProductRepositoryImpl
- ProductDto
- JsonApiRequest
- SecurityConfig
- ApiKeyAuthenticationFilter

---

## 🔧 API Endpoints

### 📝 Products API:

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/products` | List all products | ❌ |
| `GET` | `/api/products/{id}` | Get product by ID | ❌ |
| `POST` | `/api/products` | Create new product | ✅ |
| `PUT` | `/api/products/{id}` | Update product | ✅ |
| `DELETE` | `/api/products/{id}` | Delete product | ✅ |

### 📋 Actuator Endpoints:
| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health status |
| `/actuator/info` | Application information |
| `/actuator/metrics` | Application metrics |

### 📖 JSON:API Request Example:

```json
POST /api/products
Content-Type: application/vnd.api+json
X-API-Key: your-secret-api-key

{
  "data": {
    "type": "products",
    "attributes": {
      "name": "Gaming Laptop",
      "price": 1299.99
    }
  }
}
```

### 📖 JSON:API Response Example:

```json
HTTP/1.1 201 Created
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "products",
    "id": "1",
    "attributes": {
      "name": "Laptop Gaming",
      "price": 1299.99
    }
  }
}
```

---

## 🔐 Authentication

### 🔑 API Key Authentication:

The project uses API Key authentication in the header:

```bash
# Required header for protected endpoints
X-API-Key: your-secret-api-key
```

### ⚙️ Security Configuration:

```yaml
# application.yml
api:
  security:
    key: ${API_KEY:default-dev-key}
    enabled: true
```

### 🔒 Protected Endpoints:
- `POST /api/products` - Create product
- `PUT /api/products/{id}` - Update product  
- `DELETE /api/products/{id}` - Delete product

### 🌐 Public Endpoints:
- `GET /api/products` - List products
- `GET /api/products/{id}` - Get product
- `/actuator/health` - Health check

---

## 🐳 Docker

### 📁 Docker Files:

#### 🐳 Dockerfile:
```dockerfile
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/java-product-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 🔧 docker-compose.yml:
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: productdb
      POSTGRES_USER: productuser
      POSTGRES_PASSWORD: productpass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  api:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker
      POSTGRES_HOST: postgres
```

### 🚀 Docker Commands:

```bash
# Build image
docker build -t java-product-api .

# Run only the DB
docker-compose up -d postgres

# Run the entire stack
docker-compose up

# View logs
docker-compose logs -f api

# Stop services
docker-compose down

# Clean volumes
docker-compose down -v
```

---

## 📚 JSON:API Specification

This project implements the [JSON:API v1.1](https://jsonapi.org/) specification:

### 🎯 Implemented Features:
- ✅ Standard document structure (`data`, `type`, `id`, `attributes`)
- ✅ Error handling with JSON:API format
- ✅ Headers `Content-Type: application/vnd.api+json`
- ✅ Correct HTTP status codes
- ✅ Request validation

### 📋 JSON:API Error Format:
```json
{
  "errors": [
    {
      "status": "400",
      "code": "VALIDATION_ERROR", 
      "title": "Validation Failed",
      "detail": "Name is required",
      "source": {
        "pointer": "/data/attributes/name"
      }
    }
  ]
}
```

---

## 🎯 Troubleshooting

### ❌ Common Issues:

#### 🐘 PostgreSQL Connection Error:
```bash
# Verify PostgreSQL is running
docker-compose ps

# View PostgreSQL logs  
docker-compose logs postgres

# Restart services
docker-compose restart postgres
```

#### 🔑 API Key Error:
```bash
# Verify the header is included
curl -H "X-API-Key: your-key" http://localhost:8080/api/products

# Typical error: 403 Forbidden without API Key
```

#### 🧪 Test Failures:
```bash
# Clean and recompile
mvn clean compile test

# Run specific test
mvn test -Dtest=ProductTest

# View detailed logs
mvn test -X
```

#### 🐳 Docker Issues:
```bash
# Clean containers
docker-compose down
docker system prune -f

# Rebuild images
docker-compose build --no-cache

# Check resources
docker stats
```

### 📊 Health Verification:

```bash
# Application health check
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}

# Verify DB connectivity
curl http://localhost:8080/actuator/health/db
```

### 🔍 Logs and Debugging:

```bash
# View application logs
docker-compose logs -f api

# Logs with timestamp
docker-compose logs -t api

# Last 100 lines of logs
docker-compose logs --tail=100 api
```

---

## 📈 Project Metrics

### 🧪 **Test Coverage**:
- **Total Tests**: 112 ✅
- **Test Files**: 13
- **Line Coverage**: 87.69%
- **Branch Coverage**: 67.50%
- **Method Coverage**: 85.94%

### 📁 **Code Structure**:
- **Main Classes**: 16
- **Test Classes**: 13
- **Lines of Code**: ~2,500
- **Packages**: 8

### 🏗️ **Technologies**:
- **Spring Boot**: 3.2.0
- **Java**: 17
- **PostgreSQL**: 15
- **Maven**: 3.9
- **JUnit**: 5.10.1
- **Mockito**: 5.7.0
- **TestContainers**: 1.19.3
- **JaCoCo**: 0.8.11

---

## 👥 Contributing to the Project

### 🔄 Development Workflow:

1. **Fork** the repository
2. **Create branch** for feature: `git checkout -b feature/new-functionality`
3. **Write tests** first (TDD)
4. **Implement** functionality
5. **Run tests**: `mvn test`
6. **Verify coverage**: `mvn jacoco:report`
7. **Commit** with descriptive message
8. **Push** and create **Pull Request**

### ✅ PR Checklist:
- [ ] Tests pass (112/112 ✅)
- [ ] Coverage >85%
- [ ] Documentation updated
- [ ] Code formatted
- [ ] No breaking changes

---

## 📞 Support

### 🐛 Report Issues:
- **GitHub Issues**: [Create new issue](https://github.com/cristianino/java-product-api/issues)
- **Documentation**: This file (DEVELOPER_GUIDE.md)
- **Swagger UI**: http://localhost:8080/swagger-ui/

### 📚 Additional Resources:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JSON:API Specification](https://jsonapi.org/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

---

**🎉 Happy Coding!** 

*Last updated: September 2025*