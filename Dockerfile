# ---------- Build stage ----------
FROM maven:3-openjdk-17 AS build
WORKDIR /app

# Cache deps
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17.0.12_7-jre-alpine

# Alpine usa apk (no apt)
RUN apk add --no-cache curl tzdata

# Zona horaria (opcional)
ENV TZ=America/Bogota

# Perfil y workaround Micrometer/cgroups
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_TOOL_OPTIONS="-Dmanagement.metrics.binders.processor.enabled=false"

WORKDIR /app
COPY --from=build /app/target/java-product-api-*.jar /app/app.jar

# Usuario no root en Alpine
RUN addgroup -S app && adduser -S appuser -G app \
 && chown -R appuser:app /app
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=5 \
  CMD curl -fsS http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

# Flags JVM: elimina UseContainerSupport (deprecado en 15+)
ENTRYPOINT ["java","-jar","/app/app.jar"]
