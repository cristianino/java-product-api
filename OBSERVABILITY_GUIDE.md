# Observability Dashboard Guide

This guide explains how to use the Loki + Grafana observability stack integrated into the Java Product API project for centralized logging and monitoring.

## Overview

The observability stack consists of:
- **Loki**: Log aggregation system that stores and indexes logs
- **Promtail**: Log collection agent that scrapes logs from Docker containers
- **Grafana**: Visualization platform with pre-configured dashboards

## Access Information

| Service | URL | Credentials |
|---------|-----|-------------|
| **Grafana Dashboard** | http://localhost:3000 | admin / admin123 |
| **Loki API** | http://localhost:3100 | No authentication |
| **Loki Health Check** | http://localhost:3100/ready | - |

## Dashboard Components

### Java Product API - Logs Dashboard

The main dashboard provides comprehensive logging insights for the Java application:

#### 1. Log Levels Distribution (Pie Chart)
- **Purpose**: Shows the proportion of different log streams (stdout/stderr)
- **Data Source**: Container logs collected by Promtail
- **Use Case**: 
  - Monitor the balance between standard output and error streams
  - Identify if the application is generating excessive errors
- **Query**: `sum by (stream) (count_over_time({job="containerlogs"}[$__interval]))`

#### 2. Log Rate by Level (Time Series)
- **Purpose**: Displays the rate of log generation over time by stream type
- **Data Source**: Real-time log metrics from Loki
- **Use Case**:
  - Detect spikes in logging activity
  - Monitor application load patterns
  - Identify periods of high error rates
- **Query**: `sum by (stream) (rate({job="containerlogs"}[$__interval]))`

#### 3. Application Logs (Logs Panel)
- **Purpose**: Shows all logs from the Java Product API containers
- **Features**:
  - **Real-time streaming**: Logs update automatically
  - **Search functionality**: Filter logs by keywords
  - **Time navigation**: Browse historical logs
  - **Log details**: Expandable log entries with metadata
- **Use Case**:
  - Debug application issues
  - Monitor API requests and responses
  - Track application startup and shutdown
- **Query**: `{job="containerlogs"}`

#### 4. Error Logs (Logs Panel)
- **Purpose**: Filtered view showing only error-related log entries
- **Features**:
  - **Error-only filtering**: Shows logs containing "ERROR"
  - **Quick error detection**: Immediate visibility of problems
  - **Stack trace viewing**: Full error details available
- **Use Case**:
  - Rapid error identification
  - Monitor application health
  - Troubleshoot production issues
- **Query**: `{job="containerlogs"} |= "ERROR"`

## Log Collection Architecture

### How Logs Flow Through the System

1. **Java Application** → Generates logs via Logback
2. **Docker Container** → Captures stdout/stderr from the application
3. **Promtail** → Scrapes Docker container logs
4. **Loki** → Stores and indexes the collected logs
5. **Grafana** → Queries Loki and displays logs in dashboards

### Log Sources

The system collects logs from multiple sources:

| Source | Description | Labels |
|--------|-------------|---------|
| **Container Logs** | Application stdout/stderr | `job=containerlogs`, `stream=stdout/stderr` |
| **System Logs** | Host system logs (optional) | `job=syslog` |

## Usage Scenarios

### Development Workflow

1. **Start the observability stack**:
   ```bash
   docker compose -f docker-compose.dev.yml up -d
   ```

2. **Access Grafana**: Navigate to http://localhost:3000

3. **Generate application activity**:
   ```bash
   # Make API calls to generate logs
   curl -H "X-API-Key: your-secret-api-key-here" \
        http://localhost:8080/actuator/health
   ```

4. **Monitor logs in real-time**: Use the dashboard panels to observe log patterns

### Production Monitoring

1. **Health Monitoring**: Use the "Log Levels Distribution" to ensure error rates remain low

2. **Performance Tracking**: Monitor "Log Rate by Level" for unusual activity spikes

3. **Error Investigation**: Use the "Error Logs" panel for immediate problem identification

4. **Historical Analysis**: Navigate through time ranges to analyze patterns

## Dashboard Customization

### Modifying Queries

All dashboard queries can be customized to fit specific needs:

- **Filter by specific containers**: Add `container_name=~".*app-name.*"` to queries
- **Filter by log levels**: Use `|= "INFO"`, `|= "WARN"`, etc.
- **Time range filtering**: Adjust the time picker or use `[$__interval]` variables

### Adding New Panels

1. Click **"+"** → **Add Panel**
2. Select **Loki** as data source
3. Write LogQL queries following the pattern: `{job="containerlogs"} |= "search_term"`

### Query Examples

```logql
# All application logs
{job="containerlogs"}

# Only error logs
{job="containerlogs"} |= "ERROR"

# Logs containing specific text
{job="containerlogs"} |= "ProductController"

# Logs from specific time range with filtering
{job="containerlogs"} |= "Exception" | json | level="ERROR"
```

## Troubleshooting

### Common Issues

1. **No data in dashboards**:
   - Verify containers are running: `docker compose ps`
   - Check Loki health: `curl http://localhost:3100/ready`
   - Generate logs: Make API calls to the application

2. **Grafana connection issues**:
   - Restart Grafana: `docker restart java-product-api-grafana-dev`
   - Check datasource configuration in Grafana UI

3. **Missing logs**:
   - Verify Promtail is collecting logs: `docker logs java-product-api-promtail-dev`
   - Check Docker container logs directly: `docker logs java-product-api-app-dev`

### Log Queries for Debugging

```bash
# Check if logs are reaching Loki
curl "http://localhost:3100/loki/api/v1/query_range?query=%7Bjob%3D%22containerlogs%22%7D&start=$(date -d '1 hour ago' -u +%s)000000000&end=$(date -u +%s)000000000"

# List available labels
curl "http://localhost:3100/loki/api/v1/labels"

# Check label values
curl "http://localhost:3100/loki/api/v1/label/job/values"
```

## Performance Considerations

### Resource Usage

- **Loki**: Stores logs efficiently with compression
- **Promtail**: Low CPU overhead for log collection
- **Grafana**: Minimal resource usage for visualization

### Retention Policies

The current configuration uses local storage with default retention. For production:
- Configure log retention periods in Loki configuration
- Set up log rotation for Docker containers
- Monitor disk usage regularly

### Scaling Recommendations

For high-volume applications:
- Use external storage backends (S3, GCS) for Loki
- Implement log sampling for high-frequency debug logs
- Configure multiple Loki instances for high availability

## Integration with CI/CD

The observability stack can be integrated into CI/CD pipelines:

1. **Automated Testing**: Use dashboard queries to verify log patterns in tests
2. **Deployment Monitoring**: Monitor error rates during deployments
3. **Alerting**: Set up Grafana alerts for error thresholds (requires Alertmanager)

---

## Next Steps

- Explore advanced LogQL queries for deeper insights
- Set up alerting rules for critical error conditions
- Integrate with application metrics (Prometheus + Grafana)
- Configure log-based alerts for production monitoring