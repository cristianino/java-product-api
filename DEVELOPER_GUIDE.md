# 🚀 Guía del Desarrollador - Product API

## 📋 Tabla de Contenidos
- [🏗️ Arquitectura del Proyecto](#️-arquitectura-del-proyecto)
- [🛠️ Configuración del Entorno](#️-configuración-del-entorno)
- [🚀 Arrancar el Proyecto](#-arrancar-el-proyecto)
- [🧪 Ejecutar Tests](#-ejecutar-tests)
- [📊 Reportes de Cobertura](#-reportes-de-cobertura)
- [🔧 API Endpoints](#-api-endpoints)
- [🔐 Autenticación](#-autenticación)
- [🐳 Docker](#-docker)
- [📚 Especificación JSON:API](#-especificación-jsonapi)
- [🎯 Troubleshooting](#-troubleshooting)

---

## 🏗️ Arquitectura del Proyecto

Este proyecto sigue los principios de **Arquitectura Hexagonal** (Clean Architecture) con las siguientes capas:

```
src/main/java/com/cristianino/productapi/
├── 📁 domain/              # Capa de Dominio (Lógica de negocio)
│   ├── model/              # Entidades de dominio
│   ├── port/               # Interfaces (puertos)
│   └── service/            # Servicios de dominio
├── 📁 application/         # Capa de Aplicación (Casos de uso)
│   ├── dto/                # DTOs y objetos de transferencia
│   └── usecase/            # Casos de uso
├── 📁 infrastructure/      # Capa de Infraestructura (Adaptadores)
│   ├── config/             # Configuración (Seguridad, OpenAPI)
│   ├── persistence/        # JPA Entities y Repositories
│   └── web/                # Controllers REST
└── ProductApiApplication.java # Clase principal Spring Boot
```

### 🎯 Principios Aplicados:
- **DDD (Domain-Driven Design)**: Separación clara de responsabilidades
- **SOLID**: Principios de diseño orientado a objetos
- **JSON:API**: Especificación estándar para APIs REST
- **TDD**: Desarrollo guiado por tests (112 tests implementados)

---

## 🛠️ Configuración del Entorno

### 📋 Requisitos Previos:
- **Java 17** o superior
- **Maven 3.9+**
- **Docker** y **Docker Compose**
- **PostgreSQL 15** (si no usas Docker)

### 🔧 Variables de Entorno:
```bash
# Base de datos
POSTGRES_DB=productdb
POSTGRES_USER=productuser
POSTGRES_PASSWORD=productpass
POSTGRES_HOST=localhost
POSTGRES_PORT=5432

# API Key para autenticación
API_KEY=your-secret-api-key

# Profile de Spring
SPRING_PROFILES_ACTIVE=dev
```

### 📁 Estructura de Configuración:
```
src/main/resources/
├── application.yml          # Configuración principal
├── application-dev.yml      # Configuración desarrollo
├── application-prod.yml     # Configuración producción
├── logback-spring.xml       # Configuración de logs
└── db/migration/
    └── V1__Create_products_table.sql  # Migración Flyway
```

---

## 🚀 Arrancar el Proyecto

### 🐳 Opción 1: Con Docker (Recomendado)

```bash
# 1. Clonar el repositorio
git clone https://github.com/cristianino/java-product-api.git
cd java-product-api

# 2. Levantar la base de datos
docker-compose up -d postgres

# 3. Compilar y ejecutar la aplicación
docker-compose up api
```

### 💻 Opción 2: Desarrollo Local

```bash
# 1. Iniciar PostgreSQL
docker-compose up -d postgres

# 2. Compilar el proyecto
mvn clean compile

# 3. Ejecutar la aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O alternativamente:
java -jar target/java-product-api-1.0.0.jar --spring.profiles.active=dev
```

### 🌐 Acceso a la Aplicación:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Actuator Health**: http://localhost:8080/actuator/health

---

## 🧪 Ejecutar Tests

### 📊 Suite Completa de Tests (112 tests):

```bash
# Ejecutar todos los tests con reporte de cobertura
mvn clean test jacoco:report

# Ejecutar solo tests unitarios
mvn test -Dtest="*Test"

# Ejecutar solo tests de integración
mvn test -Dtest="*IntegrationTest"

# Ejecutar tests con profile específico
mvn test -Dspring.profiles.active=test
```

### 🐳 Ejecutar Tests en Docker:

```bash
# Opción completa con Maven en Docker
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-17 mvn clean test jacoco:report

# Con docker-compose
docker-compose run --rm test
```

### 📁 Tipos de Tests Implementados:

#### 🔬 Tests Unitarios:
- **ProductTest** (21 tests): Tests del modelo de dominio
- **ProductDtoTest** (10 tests): Tests de DTOs y serialización
- **ProductUseCaseTest** (14 tests): Tests de casos de uso
- **JsonApiErrorTest** (9 tests): Tests de manejo de errores
- **JsonApiResponseTest** (7 tests): Tests de respuestas API

#### 🔧 Tests de Infraestructura:
- **ProductRepositoryImplTest** (9 tests): Tests del repositorio JPA
- **ProductEntityTest** (7 tests): Tests de entidades JPA
- **SecurityConfigTest** (2 tests): Tests de configuración de seguridad
- **ApiKeyAuthenticationFilterTest** (6 tests): Tests del filtro de autenticación

#### 🌐 Tests Web:
- **ProductControllerUnitTest** (10 tests): Tests del controlador (MockMvc)
- **ProductControllerTest** (3 tests): Tests de integración HTTP
- **JsonApiRequestTest** (5 tests): Tests de requests JSON:API

#### 📊 Tests de Integración:
- **ProductIntegrationTest** (9 tests): Tests end-to-end con TestContainers

---

## 📊 Reportes de Cobertura

### 🎯 Cobertura Actual Lograda:
- 🔧 **Instrucciones: 88.08%** (1,204/1,367)
- 🔀 **Ramas: 67.50%** (81/120)
- 📝 **Líneas: 87.69%** (292/333) ⭐
- ⚙️ **Métodos: 85.94%** (110/128)

### 📈 Ver Reportes:

```bash
# Generar reporte HTML de JaCoCo
mvn jacoco:report

# Ubicación del reporte
open target/site/jacoco/index.html

# Ver reporte en CSV
cat target/site/jacoco/jacoco.csv
```

### 🏆 Clases con Cobertura Completa (100%):
- ProductController
- ProductEntity  
- ProductRepositoryImpl
- ProductDto
- JsonApiRequest
- SecurityConfig
- ApiKeyAuthenticationFilter

---

## 🔧 API Endpoints

### 📝 Productos API:

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/products` | Listar todos los productos | ❌ |
| `GET` | `/api/products/{id}` | Obtener producto por ID | ❌ |
| `POST` | `/api/products` | Crear nuevo producto | ✅ |
| `PUT` | `/api/products/{id}` | Actualizar producto | ✅ |
| `DELETE` | `/api/products/{id}` | Eliminar producto | ✅ |

### 📋 Actuator Endpoints:
| Endpoint | Descripción |
|----------|-------------|
| `/actuator/health` | Estado de salud |
| `/actuator/info` | Información de la aplicación |
| `/actuator/metrics` | Métricas de la aplicación |

### 📖 Ejemplo de Request JSON:API:

```json
POST /api/products
Content-Type: application/vnd.api+json
X-API-Key: your-secret-api-key

{
  "data": {
    "type": "products",
    "attributes": {
      "name": "Laptop Gaming",
      "price": 1299.99
    }
  }
}
```

### 📖 Ejemplo de Response JSON:API:

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

## 🔐 Autenticación

### 🔑 API Key Authentication:

El proyecto utiliza autenticación por API Key en el header:

```bash
# Header requerido para endpoints protegidos
X-API-Key: your-secret-api-key
```

### ⚙️ Configuración de Seguridad:

```yaml
# application.yml
api:
  security:
    key: ${API_KEY:default-dev-key}
    enabled: true
```

### 🔒 Endpoints Protegidos:
- `POST /api/products` - Crear producto
- `PUT /api/products/{id}` - Actualizar producto  
- `DELETE /api/products/{id}` - Eliminar producto

### 🌐 Endpoints Públicos:
- `GET /api/products` - Listar productos
- `GET /api/products/{id}` - Obtener producto
- `/actuator/health` - Health check

---

## 🐳 Docker

### 📁 Archivos Docker:

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

### 🚀 Comandos Docker:

```bash
# Construir imagen
docker build -t java-product-api .

# Ejecutar solo la BD
docker-compose up -d postgres

# Ejecutar todo el stack
docker-compose up

# Ver logs
docker-compose logs -f api

# Parar servicios
docker-compose down

# Limpiar volúmenes
docker-compose down -v
```

---

## 📚 Especificación JSON:API

Este proyecto implementa la especificación [JSON:API v1.1](https://jsonapi.org/):

### 🎯 Características Implementadas:
- ✅ Estructura de documentos estándar (`data`, `type`, `id`, `attributes`)
- ✅ Manejo de errores con formato JSON:API
- ✅ Headers `Content-Type: application/vnd.api+json`
- ✅ Códigos de estado HTTP correctos
- ✅ Validación de requests

### 📋 Formato de Error JSON:API:
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

### ❌ Problemas Comunes:

#### 🐘 Error de Conexión a PostgreSQL:
```bash
# Verificar que PostgreSQL esté corriendo
docker-compose ps

# Ver logs de PostgreSQL  
docker-compose logs postgres

# Reiniciar servicios
docker-compose restart postgres
```

#### 🔑 Error de API Key:
```bash
# Verificar que el header esté incluido
curl -H "X-API-Key: your-key" http://localhost:8080/api/products

# Error típico: 403 Forbidden sin API Key
```

#### 🧪 Fallos en Tests:
```bash
# Limpiar y recompilar
mvn clean compile test

# Ejecutar test específico
mvn test -Dtest=ProductTest

# Ver logs detallados
mvn test -X
```

#### 🐳 Problemas con Docker:
```bash
# Limpiar contenedores
docker-compose down
docker system prune -f

# Reconstruir imágenes
docker-compose build --no-cache

# Verificar recursos
docker stats
```

### 📊 Verificación de Salud:

```bash
# Health check de la aplicación
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}

# Verificar conectividad de BD
curl http://localhost:8080/actuator/health/db
```

### 🔍 Logs y Debugging:

```bash
# Ver logs de la aplicación
docker-compose logs -f api

# Logs con timestamp
docker-compose logs -t api

# Logs de los últimos 100 líneas
docker-compose logs --tail=100 api
```

---

## 📈 Métricas de Proyecto

### 🧪 **Tests Coverage**:
- **Total Tests**: 112 ✅
- **Test Files**: 13
- **Line Coverage**: 87.69%
- **Branch Coverage**: 67.50%
- **Method Coverage**: 85.94%

### 📁 **Estructura del Código**:
- **Main Classes**: 16
- **Test Classes**: 13
- **Lines of Code**: ~2,500
- **Packages**: 8

### 🏗️ **Tecnologías**:
- **Spring Boot**: 3.2.0
- **Java**: 17
- **PostgreSQL**: 15
- **Maven**: 3.9
- **JUnit**: 5.10.1
- **Mockito**: 5.7.0
- **TestContainers**: 1.19.3
- **JaCoCo**: 0.8.11

---

## 👥 Contribuir al Proyecto

### 🔄 Workflow de Desarrollo:

1. **Fork** del repositorio
2. **Crear branch** para feature: `git checkout -b feature/nueva-funcionalidad`
3. **Escribir tests** primero (TDD)
4. **Implementar** funcionalidad
5. **Ejecutar tests**: `mvn test`
6. **Verificar cobertura**: `mvn jacoco:report`
7. **Commit** con mensaje descriptivo
8. **Push** y crear **Pull Request**

### ✅ Checklist de PR:
- [ ] Tests pasan (112/112 ✅)
- [ ] Cobertura >85%
- [ ] Documentación actualizada
- [ ] Código formateado
- [ ] No breaking changes

---

## 📞 Soporte

### 🐛 Reportar Issues:
- **GitHub Issues**: [Crear nuevo issue](https://github.com/cristianino/java-product-api/issues)
- **Documentación**: Este archivo (DEVELOPER_GUIDE.md)
- **Swagger UI**: http://localhost:8080/swagger-ui/

### 📚 Recursos Adicionales:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JSON:API Specification](https://jsonapi.org/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

---

**🎉 ¡Happy Coding!** 

*Última actualización: Septiembre 2025*