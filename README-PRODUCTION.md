# 🚀 PRODUCTION DEPLOYMENT GUIDE

## 📋 PREREQUISITES

- Docker Engine 20.10+
- Docker Compose 2.0+
- Server with at least 8GB RAM and 4 CPU cores
- SSH access to production server

## 🔐 ENVIRONMENT VARIABLES CONFIGURATION

### 1. Create configuration file

```bash
# Copy the example file
cp .env.example .env

# Edit with your production values
nano .env
```

### 2. Critical variables to configure

```bash
# ⚠️  MANDATORY TO CHANGE THESE VALUES:

# Database
POSTGRES_PASSWORD=YOUR_SUPER_SECURE_PASSWORD_123!
DATABASE_PASSWORD=YOUR_SUPER_SECURE_PASSWORD_123!

# Application API Key
APP_API_KEY=YOUR_SECURE_32_CHARACTER_MINIMUM_API_KEY_123456789

# Grafana
GRAFANA_ADMIN_PASSWORD=YOUR_SUPER_SECURE_GRAFANA_PASSWORD!
```

## 🛠️ DEPLOYMENT

### 1. Clone repository on server

```bash
git clone https://github.com/cristianino/java-product-api.git
cd java-product-api
git checkout main  # or production branch
```

### 2. Configure environment variables

```bash
cp .env.example .env
# Edit .env with real production values
```

### 3. Start services

```bash
# Build and start all services
docker-compose up -d

# Verify all services are running
docker-compose ps

# View logs in real time
docker-compose logs -f
```

### 4. Verify deployment

```bash
# Application health check
curl http://localhost:8080/actuator/health

# Database health check
docker-compose exec postgres pg_isready -U productuser -d productdb

# Verify Grafana
curl http://localhost:3000/api/health
```

## 📊 SERVICE ACCESS

| Service | URL | User | Password |
|----------|-----|---------|----------|
| REST API | http://localhost:8080 | - | API Key in header |
| Grafana | http://localhost:3000 | admin | ${GRAFANA_ADMIN_PASSWORD} |
| API Docs | http://localhost:8080/swagger-ui.html | - | - |

## 🔍 MONITORING AND LOGS

### View logs for specific service
```bash
docker-compose logs -f java-product-api
docker-compose logs -f postgres
docker-compose logs -f grafana
```

### Access Grafana
1. Go to http://localhost:3000
2. User: `admin`
3. Password: the one configured in `GRAFANA_ADMIN_PASSWORD`

### Health Checks
```bash
# Main API
curl http://localhost:8080/actuator/health

# Application metrics
curl http://localhost:8080/actuator/metrics

# Application info
curl http://localhost:8080/actuator/info
```

## 🛡️ SECURITY

### Applied configuration:
- ✅ **Credentials in environment variables** (not hardcoded)
- ✅ **Internal ports** PostgreSQL and Loki not exposed to host
- ✅ **Resource limits** defined for all services
- ✅ **Non-root users** in containers
- ✅ **Health checks** configured
- ✅ **Logging with rotation** configured
- ✅ **Restart policies** for high availability

### Additional recommendations:
- 🔥 **Firewall**: Configure iptables/ufw to limit access
- 🔒 **HTTPS**: Use reverse proxy (nginx/traefik) with SSL
- 🔑 **Backup**: Configure automatic database backup
- 📈 **Monitoring**: Configure alerts in Grafana

## 🚨 EMERGENCY COMMANDS

### Restart services
```bash
docker-compose restart java-product-api
docker-compose restart postgres
```

### View error logs
```bash
docker-compose logs --tail=100 java-product-api | grep ERROR
```

### Database backup
```bash
docker-compose exec postgres pg_dump -U productuser productdb > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restore database
```bash
docker-compose exec -T postgres psql -U productuser -d productdb < backup_file.sql
```

### Stop all services
```bash
docker-compose down
```

### Stop and remove volumes (⚠️ WARNING: deletes data)
```bash
docker-compose down -v
```

## 📋 CONFIGURED RESOURCE LIMITS

| Service | CPU Limit | RAM Limit | CPU Reserve | RAM Reserve |
|----------|------------|------------|-------------|-------------|
| java-product-api | 2.0 cores | 2GB | 1.0 core | 1GB |
| postgres | 1.0 core | 1GB | 0.5 cores | 512MB |
| grafana | 1.0 core | 512MB | 0.5 cores | 256MB |
| loki | 0.5 cores | 512MB | 0.25 cores | 256MB |
| promtail | 0.25 cores | 256MB | 0.1 cores | 128MB |

**Total required:** ~5 CPU cores, ~4.25GB RAM

## 🔄 UPDATES

### 1. Update code
```bash
git pull origin main
docker-compose build java-product-api
docker-compose up -d java-product-api
```

### 2. Update entire infrastructure
```bash
git pull origin main
docker-compose build
docker-compose up -d
```

---
📚 **Additional documentation:** See files `DEVELOPER_GUIDE.md`, `API_VERSIONING_GUIDE.md` and `OBSERVABILITY_GUIDE.md`