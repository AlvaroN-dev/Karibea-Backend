# 🔍 AUDITORÍA DE ARQUITECTURA - KARIBEA BACKEND

## 📋 Resumen Ejecutivo

**Fecha de Auditoría:** Junio 2025  
**Auditor:** Senior Software Architect / DevOps Engineer  
**Objetivo:** Validar configuración para despliegue PRE-PROD/STAGE en VM única  
**Plataforma Target:** Google Cloud Compute Engine (VM única)

---

## ⚠️ ADVERTENCIAS IMPORTANTES

### 🔴 ESTO ES UN ENTORNO PRE-PROD/STAGE

Esta configuración está optimizada para **pruebas y staging**, NO para producción real con alta disponibilidad.

| Aspecto | PRE-PROD (Esta Config) | PRODUCCIÓN (Recomendado) |
|---------|------------------------|--------------------------|
| Infraestructura | VM única | GKE + Cloud SQL + Managed Kafka |
| PostgreSQL | Container Docker | Cloud SQL (HA) |
| Kafka | 3 nodos en misma VM | Confluent Cloud / Cloud Pub/Sub |
| Alta Disponibilidad | ❌ No | ✅ Sí |
| Escalado | Vertical (limitado) | Horizontal (ilimitado) |
| Recovery | Manual | Automático |
| SLA | ~95% | 99.9%+ |

---

## 📊 Consumo de Recursos por Componente

### Tabla de Asignación de Memoria

| Componente | mem_limit | JVM Heap | CPUs | Justificación |
|------------|-----------|----------|------|---------------|
| **PostgreSQL** | 1536M | N/A | 1.0 | shared_buffers=384MB + cache + connections |
| **Kafka-0** | 768M | 256-512m | 0.5 | Broker con controller |
| **Kafka-1** | 768M | 256-512m | 0.5 | Broker con controller |
| **Kafka-2** | 768M | 256-512m | 0.5 | Broker con controller |
| **Elasticsearch** | 1024M | 512m | 0.5 | Single-node dev mode |
| **Config Server** | 384M | 64-256m | 0.3 | Tráfico bajo, solo configs |
| **Eureka** | 384M | 64-256m | 0.3 | Registry service |
| **Gateway** | 512M | 128-384m | 0.5 | Punto de entrada, alto tráfico |
| **Servicios (×13)** | 400M | 96-320m | 0.3 | Workload estándar cada uno |

### Cálculo de Recursos Totales

```
Infraestructura:
  PostgreSQL:      1,536 MB
  Kafka (×3):      2,304 MB (768 × 3)
  Elasticsearch:   1,024 MB
  Config Server:     384 MB
  Eureka:            384 MB
  Gateway:           512 MB
  ─────────────────────────
  Subtotal:        6,144 MB

Microservicios (×13):
  400 MB × 13 =    5,200 MB

═══════════════════════════
TOTAL:            11,344 MB (~11.1 GB)

Recomendación VM: 12-16 GB RAM
```

---

## ✅ Problemas Corregidos en esta Auditoría

### 1. 🐳 Resource Limits (Docker Compose Classic)

**Problema:** `deploy.resources` solo funciona en Docker Swarm mode.

**Antes (INCORRECTO):**
```yaml
deploy:
  resources:
    limits:
      memory: 512M
    reservations:
      memory: 256M
```

**Después (CORRECTO):**
```yaml
mem_limit: 512M
cpus: 0.5
```

**Verificación:** Los límites reales se aplican con:
```bash
docker stats --format "table {{.Name}}\t{{.MemUsage}}\t{{.CPUPerc}}"
```

---

### 2. 🧠 Optimización de Memoria JVM

**Problema:** Misma configuración JVM para todos los servicios.

**Solución:** Diferenciación por tipo de servicio:

| Tipo de Servicio | JAVA_OPTS |
|------------------|-----------|
| Config/Eureka | `-Xms64m -Xmx256m -XX:+UseG1GC` |
| Gateway | `-Xms128m -Xmx384m -XX:+UseG1GC` |
| Business Services | `-Xms96m -Xmx320m -XX:+UseG1GC` |

**Cálculo:**
- `mem_limit` = JVM Heap + ~120MB overhead (metaspace, threads, native)
- Gateway 512MB = 384MB heap + 128MB overhead
- Services 400MB = 320MB heap + 80MB overhead

---

### 3. 🔒 Exposición de Puertos

**Problema:** Todos los servicios exponían puertos al host.

**Antes (INSEGURO):**
```yaml
microservice-identity:
  ports:
    - "8082:8082"
microservice-catalog:
  ports:
    - "8083:8083"
# ... todos expuestos
```

**Después (SEGURO):**
```yaml
# SOLO Gateway expone puerto
microservice-gateway:
  ports:
    - "8080:8080"

# Resto usa red interna Docker
microservice-identity:
  networks:
    - karibea-network
  # NO ports:
```

**Beneficios:**
- ✅ Superficie de ataque reducida
- ✅ Arquitectura de microservicios correcta (todo pasa por Gateway)
- ✅ Rate limiting y autenticación centralizados

---

### 4. 🐘 PostgreSQL Sizing

**Problema:** `effective_cache_size` mayor que memoria del container.

**Antes (INCORRECTO):**
```yaml
command: >
  -c effective_cache_size=2GB  # Container con 1GB!
```

**Después (CORRECTO):**
```yaml
mem_limit: 1536M
command: >
  -c shared_buffers=384MB        # 25% de mem_limit
  -c effective_cache_size=1GB    # ~65% de mem_limit
  -c work_mem=16MB
  -c maintenance_work_mem=128MB
  -c max_connections=150
```

**Fórmula de tuning:**
- `shared_buffers` = 25% de RAM del container
- `effective_cache_size` = 65-75% de RAM del container
- `max_connections` = (servicios × pool_size) + buffer

---

### 5. 🏊 HikariCP Connection Pool

**Problema:** Pools muy grandes agotan conexiones PostgreSQL.

**Cálculo:**
```
13 servicios × 5 conexiones = 65 conexiones base
Con picos: hasta 130+ conexiones
PostgreSQL max_connections=100 → ❌ AGOTAMIENTO
```

**Solución:**
```yaml
# .env
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=3
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=1
```

**Nuevo cálculo:**
```
13 servicios × 3 conexiones = 39 conexiones
Con buffer para admin/monitoring: ~50
PostgreSQL max_connections=150 → ✅ SUFICIENTE
```

---

### 6. 📨 Kafka en VM Única

**Problema:** 3 brokers Kafka en misma VM no provee redundancia real.

**Realidad:**
- ❌ Si la VM falla, todo Kafka falla
- ❌ Los 3 brokers compiten por I/O de disco
- ❌ No hay aislamiento de fallas

**Mitigaciones aplicadas:**
```yaml
kafka-0:
  mem_limit: 768M          # Límites estrictos
  cpus: 0.5                # CPU fair-share
  environment:
    KAFKA_HEAP_OPTS: -Xms256m -Xmx512m
    
  # Replication settings reducidos para single-VM
  KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
  KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
```

**Advertencia:** Para producción usar:
- Google Cloud Pub/Sub, o
- Confluent Cloud (managed Kafka), o
- Al menos 3 VMs separadas para los brokers

---

## 📁 Archivos Modificados

| Archivo | Cambios |
|---------|---------|
| `docker-compose.prod.yml` | Recreado con `mem_limit/cpus`, solo Gateway expuesto |
| `docker-compose.dev.yml` | Recreado con mismos principios de auditoría |
| `.env` | HikariCP ajustado, variables de recursos agregadas |

---

## 🔧 Comandos de Verificación

### Verificar límites de memoria activos
```bash
docker stats --format "table {{.Name}}\t{{.MemUsage}}\t{{.MemPerc}}\t{{.CPUPerc}}"
```

### Verificar conexiones PostgreSQL
```bash
docker exec postgres psql -U postgres -c "SELECT count(*) FROM pg_stat_activity;"
```

### Verificar estado de Kafka
```bash
docker exec kafka-0 kafka-metadata.sh --snapshot /var/lib/kafka/data/__cluster_metadata-0/00000000000000000000.log --command "describe"
```

### Verificar health de todos los servicios
```bash
docker compose -f docker-compose.prod.yml ps
```

### Verificar logs de errores
```bash
docker compose -f docker-compose.prod.yml logs --tail=100 | grep -i error
```

---

## 📈 Monitoreo Recomendado

Para PRE-PROD se recomienda agregar:

1. **Prometheus + Grafana** para métricas
2. **Actuator endpoints** habilitados en cada servicio
3. **Alert rules** para:
   - Memory usage > 80%
   - CPU usage > 70%
   - Conexiones DB > 100
   - Kafka consumer lag > 1000

---

## 🚀 Siguiente Paso: Producción Real

Cuando esté listo para producción, migrar a:

```
┌─────────────────────────────────────────────────────────────┐
│                    Google Cloud Platform                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────┐    ┌───────────────────────────────────┐  │
│  │ Cloud Load   │───▶│  GKE Cluster (Autopilot)          │  │
│  │ Balancer     │    │  ┌─────────┐ ┌─────────┐          │  │
│  └──────────────┘    │  │Gateway  │ │Services │ (×N)     │  │
│                       │  │(3 pods) │ │(3 pods) │          │  │
│                       │  └─────────┘ └─────────┘          │  │
│                       └───────────────────────────────────┘  │
│                                      │                       │
│  ┌──────────────┐                    │                       │
│  │ Cloud SQL    │◀───────────────────┘                       │
│  │ (PostgreSQL) │    ┌───────────────────────────────────┐  │
│  │ HA enabled   │    │  Confluent Cloud / Pub/Sub        │  │
│  └──────────────┘    │  (Managed Kafka)                  │  │
│                       └───────────────────────────────────┘  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## ✅ Checklist de Validación

- [x] Todos los `deploy.resources` reemplazados por `mem_limit/cpus`
- [x] Solo Gateway expone puerto 8080
- [x] JVM optimizada por tipo de servicio
- [x] PostgreSQL tuning acorde a container memory
- [x] HikariCP pool size reducido (3 conexiones por servicio)
- [x] Kafka heap limitado (-Xms256m -Xmx512m)
- [x] Red Docker interna para comunicación inter-servicios
- [x] Healthchecks configurados para todos los servicios
- [x] Variables de entorno sin valores por defecto (sin `:-default`)
- [x] Documentación de advertencias PRE-PROD vs PROD

---

**Auditoría completada.** ✅

*Este documento debe ser revisado antes de cualquier despliegue y actualizado cuando se realicen cambios significativos en la arquitectura.*
