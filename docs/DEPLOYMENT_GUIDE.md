# Guía de Despliegue - Karibea Backend

## Tabla de Contenidos

1. [Requisitos Previos](#requisitos-previos)
2. [Desarrollo Local](#desarrollo-local)
3. [Producción (Docker)](#producción-docker)
4. [Troubleshooting](#troubleshooting)

---

## Requisitos Previos

### Software Requerido

| Software | Versión Mínima | Verificar |
|----------|----------------|-----------|
| Java JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Docker | 20+ | `docker --version` |
| Docker Compose | 2.0+ | `docker compose version` |
| Git | 2.0+ | `git --version` |

### Recursos de Sistema

#### Desarrollo (Mínimo)
- **CPU**: 4 cores
- **RAM**: 12 GB
- **Disco**: 20 GB libres

#### Producción (Recomendado)
- **CPU**: 4-8 cores
- **RAM**: 24 GB
- **Disco**: 50 GB SSD

---

## Desarrollo Local

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/karibea-backend.git
cd karibea-backend
```

### 2. Configurar Variables de Entorno

```bash
# Copiar template
cp .env.example .env

# Editar con tus valores
nano .env  # o tu editor preferido
```

#### Variables Mínimas Requeridas

```env
# Base de datos
DB_USERNAME=postgres
DB_PASSWORD=tu_password_seguro

# Kafka
KAFKA_CLUSTER_ID=MkU3OEVBNTcwNTJENDM2Qk

# JWT
JWT_SECRET=tu-secret-muy-largo-de-al-menos-256-bits-para-seguridad
```

### 3. Iniciar con Docker Compose

#### Opción A: Script Python (Recomendado)

```bash
python run_compose.py
# Seleccionar opción 1 para desarrollo
```

El script inicia los servicios en orden correcto:
1. PostgreSQL
2. Kafka Cluster (3 brokers)
3. Config Server
4. Eureka Server
5. Microservicios de negocio
6. API Gateway

#### Opción B: Docker Compose Directo

```bash
docker compose -f docker-compose.dev.yml up -d
```

### 4. Verificar Servicios

```bash
# Ver estado de contenedores
docker compose -f docker-compose.dev.yml ps

# Ver logs en tiempo real
docker compose -f docker-compose.dev.yml logs -f

# Verificar health
curl http://localhost:8080/actuator/health
```

### 5. Acceder a los Servicios

| Servicio | URL |
|----------|-----|
| **API Gateway** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **Eureka Dashboard** | http://localhost:8761 |
| **Config Server** | http://localhost:8888 |

---

## Producción (Docker)

### 1. Preparar el Servidor

```bash
# Actualizar sistema (Ubuntu/Debian)
sudo apt update && sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER

# Instalar Docker Compose
sudo apt install docker-compose-plugin -y

# Verificar
docker compose version
```

### 2. Configurar Firewall

```bash
# UFW (Ubuntu)
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw allow 8080/tcp  # Gateway (temporal, usar reverse proxy)
sudo ufw enable
```

### 3. Configurar Variables de Producción

Crear archivo `.env` con valores de producción:

```env
# ===========================================
# DATABASE
# ===========================================
DB_USERNAME=karibea_admin
DB_PASSWORD=<CONTRASEÑA_MUY_SEGURA>

# ===========================================
# KAFKA
# ===========================================
KAFKA_CLUSTER_ID=<CLUSTER_ID_ÚNICO>
KAFKA_LOG_RETENTION_HOURS=168
KAFKA_LOG_RETENTION_BYTES=1073741824

# ===========================================
# JWT / SEGURIDAD
# ===========================================
JWT_SECRET=<SECRET_256_BITS_GENERADO>
JWT_EXPIRATION=86400000
JWT_ISSUER_URI=https://api.tudominio.com

# ===========================================
# OAUTH2 (Producción)
# ===========================================
GOOGLE_CLIENT_ID=<TU_CLIENT_ID>
GOOGLE_CLIENT_SECRET=<TU_CLIENT_SECRET>

# ===========================================
# PAGOS
# ===========================================
STRIPE_API_KEY=sk_live_xxx
STRIPE_WEBHOOK_SECRET=whsec_xxx
PAYPAL_CLIENT_ID=<PAYPAL_PROD_CLIENT_ID>
PAYPAL_CLIENT_SECRET=<PAYPAL_PROD_SECRET>

# ===========================================
# NOTIFICACIONES
# ===========================================
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_USERNAME=apikey
MAIL_PASSWORD=<SENDGRID_API_KEY>

TWILIO_ACCOUNT_SID=<TWILIO_SID>
TWILIO_AUTH_TOKEN=<TWILIO_TOKEN>
TWILIO_PHONE_NUMBER=+1234567890

# ===========================================
# AI / CHATBOT
# ===========================================
OPENAI_API_KEY=sk-xxx
OPENAI_MODEL=gpt-4o-mini

# ===========================================
# DOCKER REGISTRY (Opcional)
# ===========================================
DOCKER_REGISTRY=ghcr.io/tu-usuario/
VERSION=1.0.0
```

### 4. Generar Secrets Seguros

```bash
# Generar JWT Secret (256 bits = 32 bytes)
openssl rand -base64 32

# Generar Kafka Cluster ID
openssl rand -base64 16 | tr '+/' '-_' | tr -d '='

# Generar password seguro
openssl rand -base64 24
```

### 5. Build de Imágenes

```bash
# Build todas las imágenes
docker compose -f docker-compose.prod.yml build

# O build con push a registry
docker compose -f docker-compose.prod.yml build --push
```

### 6. Iniciar en Producción

```bash
# Iniciar todos los servicios
docker compose -f docker-compose.prod.yml up -d

# Ver logs
docker compose -f docker-compose.prod.yml logs -f

# Ver estado
docker compose -f docker-compose.prod.yml ps
```

### 7. Configurar Nginx Reverse Proxy

```nginx
# /etc/nginx/sites-available/karibea
upstream gateway {
    server localhost:8080;
    keepalive 32;
}

server {
    listen 80;
    server_name api.tudominio.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.tudominio.com;

    ssl_certificate /etc/letsencrypt/live/api.tudominio.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.tudominio.com/privkey.pem;

    # SSL optimizations
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256;
    ssl_prefer_server_ciphers on;

    # Security headers
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;

    location / {
        proxy_pass http://gateway;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Rate limiting para login
    location /api/identity/auth/ {
        limit_req zone=login burst=5 nodelay;
        proxy_pass http://gateway;
    }
}
```

### 8. Obtener Certificado SSL

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtener certificado
sudo certbot --nginx -d api.tudominio.com

# Auto-renovación
sudo certbot renew --dry-run
```

### 9. Configurar Backups

```bash
#!/bin/bash
# /opt/scripts/backup.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/opt/backups

# Backup PostgreSQL
docker exec karibea-postgres pg_dumpall -U postgres > $BACKUP_DIR/postgres_$DATE.sql

# Comprimir
gzip $BACKUP_DIR/postgres_$DATE.sql

# Mantener últimos 7 días
find $BACKUP_DIR -name "*.gz" -mtime +7 -delete

echo "Backup completado: postgres_$DATE.sql.gz"
```

Agregar a cron:
```bash
# Backup diario a las 3 AM
0 3 * * * /opt/scripts/backup.sh >> /var/log/backup.log 2>&1
```

---

## Troubleshooting

### Problema: Servicios no inician

**Síntoma**: Contenedores en estado "Restarting" o "Exit"

**Solución**:
```bash
# Ver logs del servicio específico
docker logs karibea-config --tail 100

# Verificar recursos
docker stats --no-stream

# Reiniciar servicio específico
docker compose -f docker-compose.dev.yml restart microservice-config
```

### Problema: Connection refused a Config Server

**Síntoma**: Microservicios no pueden conectar a Config Server

**Solución**:
```bash
# Verificar que Config esté healthy
docker ps | grep config

# Verificar red
docker network inspect karibea-network

# Reiniciar en orden
docker compose -f docker-compose.dev.yml restart microservice-config
sleep 30
docker compose -f docker-compose.dev.yml restart microservice-eureka
```

### Problema: Eureka no registra servicios

**Síntoma**: Dashboard de Eureka vacío

**Solución**:
```bash
# Verificar DNS interno
docker exec karibea-gateway ping microservice-eureka

# Ver logs de eureka
docker logs karibea-eureka --tail 100

# Verificar application.yaml
docker exec karibea-catalog cat /app/config/application.yaml
```

### Problema: Kafka topics no se crean

**Síntoma**: Errores de "Topic not found"

**Solución**:
```bash
# Listar topics
docker exec karibea-kafka-0 /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 --list

# Crear topic manualmente
docker exec karibea-kafka-0 /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --create --topic orders \
  --partitions 3 --replication-factor 1
```

### Problema: OutOfMemoryError

**Síntoma**: Servicios crashean con OOM

**Solución**:
```bash
# Verificar uso de memoria
docker stats --no-stream

# Aumentar memoria en docker-compose.yml
deploy:
  resources:
    limits:
      memory: 512M  # Aumentar
    reservations:
      memory: 256M
```

### Problema: Puerto 5432 ocupado

**Síntoma**: PostgreSQL no puede iniciar

**Solución (Linux)**:
```bash
# Ver qué usa el puerto
sudo lsof -i :5432

# Detener PostgreSQL local
sudo systemctl stop postgresql
```

**Solución (Windows)**:
```powershell
# Ver qué usa el puerto
netstat -ano | findstr :5432

# Detener servicio PostgreSQL
Stop-Service postgresql-x64-15
```

### Comandos Útiles

```bash
# Ver todos los logs
docker compose -f docker-compose.dev.yml logs -f

# Ver logs de un servicio
docker logs -f karibea-gateway

# Reiniciar todo
docker compose -f docker-compose.dev.yml down
docker compose -f docker-compose.dev.yml up -d

# Limpiar todo (¡CUIDADO! Borra datos)
docker compose -f docker-compose.dev.yml down -v
docker system prune -af

# Entrar a un contenedor
docker exec -it karibea-gateway /bin/sh

# Ver métricas
curl http://localhost:8080/actuator/metrics | jq

# Health check completo
curl http://localhost:8080/actuator/health | jq
```

---

## Orden de Inicio Manual

Si necesitas iniciar servicios uno por uno:

```bash
# 1. Infraestructura
docker compose -f docker-compose.dev.yml up -d postgres
docker compose -f docker-compose.dev.yml up -d kafka-0 kafka-1 kafka-2
sleep 60  # Esperar quorum

# 2. Core
docker compose -f docker-compose.dev.yml up -d microservice-config
sleep 30  # Esperar health
docker compose -f docker-compose.dev.yml up -d microservice-eureka
sleep 30  # Esperar health

# 3. Microservicios
docker compose -f docker-compose.dev.yml up -d \
  microservice-catalog \
  microservice-identity \
  microservice-user \
  microservice-order \
  microservice-payment \
  microservice-inventory \
  microservice-shopcart \
  microservice-shipping \
  microservice-notification \
  microservice-marketing \
  microservice-review \
  microservice-search \
  microservice-store \
  microservice-chatbot

# 4. Gateway
sleep 30  # Esperar registro en Eureka
docker compose -f docker-compose.dev.yml up -d microservice-gateway
```
