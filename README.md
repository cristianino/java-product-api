# Java Product API

A Spring Boot 3 microservice for managing products with JSON:API specification, Hexagonal Architecture, PostgreSQL, and Docker support.

## Features

- **Spring Boot 3** with **Java 17**
- **Hexagonal Architecture** (Domain, Application, Infrastructure layers)
- **REST CRUD operations** for products (id, name, price)
- **JSON:API** response format (data, links, meta, errors)
- **API Key security** via X-API-Key header
- **PostgreSQL** persistence with JPA
- **Flyway** database migrations
- **Swagger/OpenAPI** documentation
- **Actuator** health checks
- **Structured JSON logging** for Loki/Grafana
- **Unit & Integration tests** with JUnit and Testcontainers
- **Docker** support with multi-stage builds

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

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/cristianino/java-product-api.git
   cd java-product-api
   ```

2. **Build the application**
   ```bash
   mvn clean package
   ```

3. **Run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

   This will start:
   - PostgreSQL database on port 5432
   - Java Product API on port 8080

4. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
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
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest="*Test"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"
```

### Database Migrations

Database schema is managed with Flyway. Migration scripts are in `src/main/resources/db/migration/`.

### Code Structure

The application follows these principles:
- **Domain-driven design** with clear boundaries
- **Dependency inversion** - domain doesn't depend on infrastructure
- **JSON:API specification** for consistent API responses
- **Comprehensive testing** with unit and integration tests

## Monitoring & Observability

### Health Checks
- Endpoint: `/actuator/health`
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

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License.