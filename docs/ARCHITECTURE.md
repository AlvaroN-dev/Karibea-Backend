# 🏗️ Karibea Backend - Arquitectura de Microservicios

## 📋 Tabla de Contenidos

1. [Descripción General](#descripción-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Servicios Core](#servicios-core)
4. [Microservicios de Negocio](#microservicios-de-negocio)
5. [Comunicación entre Servicios](#comunicación-entre-servicios)
6. [Configuración y Despliegue](#configuración-y-despliegue)
7. [Seguridad](#seguridad)
8. [Monitoreo y Health Checks](#monitoreo-y-health-checks)
9. [API Gateway y Swagger](#api-gateway-y-swagger)
10. [Guía de Inicio Rápido](#guía-de-inicio-rápido)

---

## 📖 Descripción General

Karibea es una plataforma de e-commerce construida con una arquitectura de microservicios utilizando **Spring Cloud**. La plataforma está diseñada para ser escalable, resiliente y fácil de mantener.

### Tecnologías Principales

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.5.8 | Framework base |
| Spring Cloud | 2025.0.0 | Herramientas de microservicios |
| PostgreSQL | 15 | Base de datos |
| Apache Kafka | 4.0.1 | Mensajería asíncrona |
| Docker | Latest | Containerización |

---

## 🏛️ Arquitectura del Sistema

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                                  CLIENTS                                      │
│                    (Web App, Mobile App, External APIs)                       │
└─────────────────────────────────────┬────────────────────────────────────────┘
                                      │
                                      ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                           API GATEWAY (Port 8080)                             │
│                      Spring Cloud Gateway + Security                          │
│                   - JWT Validation - Rate Limiting - CORS                     │
│                   - Circuit Breaker - Load Balancing                          │
└─────────────────────────────────────┬────────────────────────────────────────┘
                                      │
          ┌───────────────────────────┼───────────────────────────┐
          │                           │                           │
          ▼                           ▼                           ▼
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────────────┐
│ EUREKA SERVER   │      │ CONFIG SERVER   │      │    MICROSERVICES        │
│   (Port 8761)   │◄────►│   (Port 8888)   │◄────►│  (Ports 8082-8094)      │
│ Service Registry│      │ Centralized     │      │  Business Logic         │
│ & Discovery     │      │ Configuration   │      │                         │
└─────────────────┘      └─────────────────┘      └───────────┬─────────────┘
                                                              │
          ┌───────────────────────────┬───────────────────────┘
          │                           │
          ▼                           ▼
┌─────────────────────┐      ┌─────────────────────┐
│    POSTGRESQL       │      │    APACHE KAFKA     │
│   (Port 5432)       │      │  (Ports 9092-9096)  │
│   Unified DB with   │      │   Event Streaming   │
│   Multiple Schemas  │      │   Message Queue     │
└─────────────────────┘      └─────────────────────┘
```

---

## 🎯 Servicios Core

### 1. Eureka Server (microservice-eureka)
**Puerto:** 8761

El servidor de descubrimiento de servicios permite que los microservicios se registren y descubran entre sí automáticamente.

```yaml
# Acceso al dashboard
http://localhost:8761
```

**Características:**
- Auto-registro de servicios
- Health checks automáticos
- Load balancing basado en instancias disponibles
- Self-preservation deshabilitado para desarrollo

### 2. Config Server (microservice-config)
**Puerto:** 8888

Servidor de configuración centralizada que proporciona configuraciones a todos los microservicios.

```yaml
# Verificar configuración de un servicio
http://localhost:8888/microservice-catalog/default
```

**Características:**
- Configuración centralizada en `src/main/resources/config/`
- Soporte para múltiples perfiles (dev, prod, docker)
- Refresh de configuración sin reinicio
- Registro automático en Eureka

### 3. API Gateway (microservice-gateway)
**Puerto:** 8080

Punto de entrada único para todas las peticiones de los clientes.

**Características:**
- Enrutamiento dinámico basado en Eureka
- Validación JWT integrada
- Circuit Breaker con Resilience4j
- CORS configurado
- Agregación de Swagger/OpenAPI
- Fallback controllers para alta disponibilidad

---

## 💼 Microservicios de Negocio

| Servicio | Puerto | Descripción | Endpoints Base |
|----------|--------|-------------|----------------|
| **Identity** | 9000 | OAuth2, Autenticación, JWT | `/api/auth/**` |
| **User** | 8083 | Gestión de usuarios y perfiles | `/api/users/**` |
| **Catalog** | 8082 | Catálogo de productos | `/api/products/**`, `/api/categories/**` |
| **Order** | 8084 | Gestión de pedidos | `/api/orders/**` |
| **Payment** | 8085 | Procesamiento de pagos | `/api/payments/**` |
| **Inventory** | 8086 | Control de inventario | `/api/inventory/**` |
| **Shopcart** | 8087 | Carrito de compras | `/api/cart/**` |
| **Marketing** | 8088 | Promociones y cupones | `/api/marketing/**` |
| **Shipping** | 8089 | Envíos y entregas | `/api/shipping/**` |
| **Notification** | 8090 | Notificaciones (email, push) | `/api/notifications/**` |
| **Review** | 8091 | Reseñas de productos | `/api/reviews/**` |
| **Search** | 8092 | Búsqueda full-text | `/api/search/**` |
| **Store** | 8093 | Gestión de tiendas | `/api/stores/**` |
| **Chatbot** | 8094 | Asistente IA | `/api/chatbot/**` |

---

## 📡 Comunicación entre Servicios

### Comunicación Síncrona (REST)
Los microservicios se comunican entre sí usando **Feign Clients** o **WebClient** a través del Gateway o directamente usando Eureka para descubrimiento.

```java
// Ejemplo de Feign Client
@FeignClient(name = "microservice-catalog")
public interface CatalogClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProduct(@PathVariable Long id);
}
```

### Comunicación Asíncrona (Kafka)
Para operaciones que no requieren respuesta inmediata:

| Topic | Productores | Consumidores |
|-------|-------------|--------------|
| `orders` | Order Service | Payment, Inventory, Notification |
| `payments` | Payment Service | Order, Notification |
| `inventory` | Inventory Service | Order, Catalog |
| `notifications` | Todos | Notification Service |
| `shipping` | Shipping Service | Order, Notification |
| `products` | Catalog Service | Search, Inventory |

---

## ⚙️ Configuración y Despliegue

### Variables de Entorno

Copiar el archivo de ejemplo:
```bash
cp .env.example .env
```

Principales variables:

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `DB_USERNAME` | Usuario PostgreSQL | postgres |
| `DB_PASSWORD` | Contraseña PostgreSQL | postgres123 |
| `JWT_SECRET` | Clave secreta para JWT | (generada) |
| `KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka | kafka-0:9092,kafka-1:9092,kafka-2:9092 |

### Despliegue con Docker Compose

**Desarrollo:**
```bash
docker-compose -f docker-compose.dev.yml up -d
```

**Producción:**
```bash
docker-compose -f docker-compose.prod.yml up -d
```

### Orden de Inicio de Servicios

1. **Infraestructura:** PostgreSQL, Kafka (cluster)
2. **Core:** Config Server → Eureka Server
3. **Gateway:** API Gateway
4. **Negocio:** Todos los microservicios de negocio

---

## 🔐 Seguridad

### Autenticación JWT

El sistema utiliza JWT (JSON Web Tokens) para autenticación:

1. **Login:** `POST /api/auth/login`
2. **Registro:** `POST /api/auth/register`
3. **Refresh Token:** `POST /api/auth/refresh`

### Flujo de Autenticación

```
┌────────┐       ┌─────────┐       ┌──────────┐       ┌─────────────┐
│ Client │──────►│ Gateway │──────►│ Identity │──────►│ JWT Token   │
│        │◄──────│         │◄──────│ Service  │◄──────│ Generated   │
└────────┘       └─────────┘       └──────────┘       └─────────────┘
     │
     │  (Subsequent requests with JWT)
     ▼
┌────────┐       ┌─────────┐       ┌──────────────────┐
│ Client │──────►│ Gateway │──────►│ Any Microservice │
│ + JWT  │       │ Validate│       │ (Protected)      │
└────────┘       └─────────┘       └──────────────────┘
```

### Endpoints Públicos (sin autenticación)

- `/api/auth/**` - Autenticación
- `/actuator/health` - Health checks
- `/swagger-ui/**` - Documentación
- `GET /api/products/**` - Catálogo público
- `GET /api/stores/**` - Tiendas públicas
- `GET /api/reviews/**` - Reseñas públicas

---

## 📊 Monitoreo y Health Checks

### Actuator Endpoints

Cada microservicio expone endpoints de monitoreo:

| Endpoint | Descripción |
|----------|-------------|
| `/actuator/health` | Estado del servicio |
| `/actuator/info` | Información del servicio |
| `/actuator/metrics` | Métricas de rendimiento |
| `/actuator/prometheus` | Métricas para Prometheus |

### Health Checks en Docker

Todos los contenedores tienen health checks configurados:

```yaml
healthcheck:
  test: ["CMD-SHELL", "wget --spider http://localhost:PORT/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 60s
```

---

## 📚 API Gateway y Swagger

### Swagger UI Agregado

Accede a la documentación de TODAS las APIs desde el Gateway:

```
http://localhost:8080/swagger-ui.html
```

### Rutas de Documentación por Servicio

| Servicio | Swagger UI |
|----------|------------|
| Gateway | `/swagger-ui.html` |
| Identity | `/identity/swagger-ui.html` |
| User | `/user/swagger-ui.html` |
| Catalog | `/catalog/swagger-ui.html` |
| Order | `/order/swagger-ui.html` |
| Payment | `/payment/swagger-ui.html` |
| ... | ... |

### API Docs (OpenAPI JSON)

```
http://localhost:8080/v3/api-docs        # Gateway
http://localhost:8080/catalog/v3/api-docs # Catalog
http://localhost:8080/order/v3/api-docs   # Order
```

---

## 🚀 Guía de Inicio Rápido

### Prerequisitos

- Docker y Docker Compose
- Java 17+ (para desarrollo local)
- Maven 3.8+ (para desarrollo local)

### Paso 1: Clonar y Configurar

```bash
git clone https://github.com/AlvaroN-dev/Karibea-Backend.git
cd Karibea-Backend
cp .env.example .env
```

### Paso 2: Construir los Proyectos

```bash
# Construir todos los módulos
mvn clean package -DskipTests
```

### Paso 3: Iniciar con Docker

```bash
# Desarrollo
docker-compose -f docker-compose.dev.yml up -d

# Ver logs
docker-compose -f docker-compose.dev.yml logs -f
```

### Paso 4: Verificar Servicios

1. **Eureka Dashboard:** http://localhost:8761
2. **Swagger UI:** http://localhost:8080/swagger-ui.html
3. **Health Check:** http://localhost:8080/actuator/health

### Paso 5: Probar la API

```bash
# Registrar usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@karibea.com", "password": "password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@karibea.com", "password": "password123"}'

# Usar el token para acceder a recursos protegidos
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📁 Estructura del Proyecto

```
Karibea-Backend/
├── docker-compose.dev.yml      # Docker Compose desarrollo
├── docker-compose.prod.yml     # Docker Compose producción
├── init.sql                    # Script inicialización DB
├── pom.xml                     # POM padre
├── .env.example                # Variables de entorno ejemplo
│
├── microservice-eureka/        # Service Discovery
├── microservice-config/        # Configuration Server
│   └── src/main/resources/
│       └── config/             # Configuraciones centralizadas
│           ├── application.yaml
│           ├── microservice-catalog.yaml
│           ├── microservice-identity.yaml
│           └── ...
├── microservice-gateway/       # API Gateway
│   └── src/main/java/
│       └── config/
│           ├── SecurityConfig.java
│           └── OpenApiConfig.java
│       └── controller/
│           └── FallbackController.java
│       └── filter/
│           └── LoggingFilter.java
│
├── microservice-catalog/       # Catálogo
├── microservice-identity/      # OAuth2/Auth
├── microservice-user/          # Usuarios
├── microservice-order/         # Pedidos
├── microservice-payment/       # Pagos
├── microservice-inventory/     # Inventario
├── microservice-shopcart/      # Carrito
├── microservice-marketing/     # Marketing
├── microservice-shipping/      # Envíos
├── microservice-notification/  # Notificaciones
├── microservice-review/        # Reseñas
├── microservice-search/        # Búsqueda
├── microservice-store/         # Tiendas
└── microservice-chatbot/       # Chatbot IA
```

---

## 🛠️ Troubleshooting

### El servicio no se registra en Eureka

1. Verificar que Eureka está corriendo: `http://localhost:8761`
2. Revisar la configuración de `eureka.client.service-url.defaultZone`
3. Verificar que el servicio tiene el Eureka client habilitado

### Errores de conexión a la base de datos

1. Verificar que PostgreSQL está corriendo
2. Revisar las credenciales en `.env`
3. Verificar que el schema existe: `\dn` en psql

### El Gateway no enruta correctamente

1. Verificar que el servicio está registrado en Eureka
2. Revisar los logs del Gateway: `docker logs microservice-gateway`
3. Verificar la configuración de rutas en `application.yaml`

### Kafka no conecta

1. Verificar que los 3 nodos de Kafka están healthy
2. Revisar `KAFKA_BOOTSTRAP_SERVERS` en `.env`
3. Verificar que los topics existen: `kafka-topics.sh --list`

---

## 📞 Soporte

- **GitHub Issues:** [https://github.com/AlvaroN-dev/Karibea-Backend/issues](https://github.com/AlvaroN-dev/Karibea-Backend/issues)
- **Email:** support@karibea.com

---

**© 2024 Karibea Team - Todos los derechos reservados**
