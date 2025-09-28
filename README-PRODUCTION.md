# 🚀 GUÍA DE DEPLOYMENT EN PRODUCCIÓN

## 📋 PRE-REQUISITOS

- Docker Engine 20.10+
- Docker Compose 2.0+
- Servidor con al menos 8GB RAM y 4 CPU cores
- Acceso SSH al servidor de producción

## 🔐 CONFIGURACIÓN DE VARIABLES DE ENTORNO

### 1. Crear archivo de configuración

```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar con tus valores de producción
nano .env
```

### 2. Variables críticas a configurar

```bash
# ⚠️  CAMBIAR OBLIGATORIAMENTE ESTOS VALORES:

# Base de datos
POSTGRES_PASSWORD=TU_PASSWORD_SUPER_SEGURO_123!
DATABASE_PASSWORD=TU_PASSWORD_SUPER_SEGURO_123!

# API Key de la aplicación
APP_API_KEY=TU_API_KEY_SEGURA_DE_32_CARACTERES_MINIMO_123456789

# Grafana
GRAFANA_ADMIN_PASSWORD=TU_PASSWORD_GRAFANA_SUPER_SEGURO!
```

## 🛠️ DEPLOYMENT

### 1. Clonar repositorio en servidor

```bash
git clone https://github.com/cristianino/java-product-api.git
cd java-product-api
git checkout main  # o la rama de producción
```

### 2. Configurar variables de entorno

```bash
cp .env.example .env
# Editar .env con valores reales de producción
```

### 3. Levantar servicios

```bash
# Construir y levantar todos los servicios
docker-compose up -d

# Verificar que todos los servicios estén corriendo
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f
```

### 4. Verificar deployment

```bash
# Health check de la aplicación
curl http://localhost:8080/actuator/health

# Health check de la base de datos
docker-compose exec postgres pg_isready -U productuser -d productdb

# Verificar Grafana
curl http://localhost:3000/api/health
```

## 📊 ACCESO A SERVICIOS

| Servicio | URL | Usuario | Password |
|----------|-----|---------|----------|
| API REST | http://localhost:8080 | - | API Key en header |
| Grafana | http://localhost:3000 | admin | ${GRAFANA_ADMIN_PASSWORD} |
| API Docs | http://localhost:8080/swagger-ui.html | - | - |

## 🔍 MONITOREO Y LOGS

### Ver logs de un servicio específico
```bash
docker-compose logs -f java-product-api
docker-compose logs -f postgres
docker-compose logs -f grafana
```

### Acceder a Grafana
1. Ir a http://localhost:3000
2. Usuario: `admin`
3. Password: el configurado en `GRAFANA_ADMIN_PASSWORD`

### Health Checks
```bash
# API principal
curl http://localhost:8080/actuator/health

# Métricas de la aplicación
curl http://localhost:8080/actuator/metrics

# Info de la aplicación
curl http://localhost:8080/actuator/info
```

## 🛡️ SEGURIDAD

### Configuración aplicada:
- ✅ **Credenciales en variables de entorno** (no hardcodeadas)
- ✅ **Puertos internos** PostgreSQL y Loki no expuestos al host
- ✅ **Límites de recursos** definidos para todos los servicios
- ✅ **Usuarios no-root** en contenedores
- ✅ **Health checks** configurados
- ✅ **Logging con rotación** configurado
- ✅ **Restart policies** para alta disponibilidad

### Recomendaciones adicionales:
- 🔥 **Firewall**: Configurar iptables/ufw para limitar acceso
- 🔒 **HTTPS**: Usar reverse proxy (nginx/traefik) con SSL
- 🔑 **Backup**: Configurar backup automático de la base de datos
- 📈 **Monitoring**: Configurar alertas en Grafana

## 🚨 COMANDOS DE EMERGENCIA

### Reiniciar servicios
```bash
docker-compose restart java-product-api
docker-compose restart postgres
```

### Ver logs de errores
```bash
docker-compose logs --tail=100 java-product-api | grep ERROR
```

### Backup de base de datos
```bash
docker-compose exec postgres pg_dump -U productuser productdb > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar base de datos
```bash
docker-compose exec -T postgres psql -U productuser -d productdb < backup_file.sql
```

### Parar todos los servicios
```bash
docker-compose down
```

### Parar y eliminar volúmenes (⚠️ CUIDADO: borra datos)
```bash
docker-compose down -v
```

## 📋 LÍMITES DE RECURSOS CONFIGURADOS

| Servicio | CPU Límite | RAM Límite | CPU Reserva | RAM Reserva |
|----------|------------|------------|-------------|-------------|
| java-product-api | 2.0 cores | 2GB | 1.0 core | 1GB |
| postgres | 1.0 core | 1GB | 0.5 cores | 512MB |
| grafana | 1.0 core | 512MB | 0.5 cores | 256MB |
| loki | 0.5 cores | 512MB | 0.25 cores | 256MB |
| promtail | 0.25 cores | 256MB | 0.1 cores | 128MB |

**Total requerido:** ~5 cores CPU, ~4.25GB RAM

## 🔄 ACTUALIZACIONES

### 1. Actualizar código
```bash
git pull origin main
docker-compose build java-product-api
docker-compose up -d java-product-api
```

### 2. Actualizar toda la infraestructura
```bash
git pull origin main
docker-compose build
docker-compose up -d
```

---
📚 **Documentación adicional:** Ver archivos `DEVELOPER_GUIDE.md`, `API_VERSIONING_GUIDE.md` y `OBSERVABILITY_GUIDE.md`