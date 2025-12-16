# API Gateway Documentation

## Overview

El API Gateway de Karibea es el punto de entrada único para todas las solicitudes a la plataforma. Está construido con Spring Cloud Gateway y proporciona:

- **Enrutamiento dinámico** a todos los microservicios
- **Balanceo de carga** mediante Eureka
- **Seguridad centralizada** con OAuth2/JWT
- **Rate limiting** para protección contra abusos
- **Circuit breaker** para tolerancia a fallos
- **Agregación de Swagger** para documentación unificada

## Endpoints Base

| Servicio | Ruta Gateway | Puerto Directo |
|----------|--------------|----------------|
| **Catalog** | `/api/catalog/**` | 8081 |
| **Identity** | `/api/identity/**` | 8082 |
| **User** | `/api/user/**` | 8083 |
| **Order** | `/api/order/**` | 8084 |
| **Payment** | `/api/payment/**` | 8085 |
| **Inventory** | `/api/inventory/**` | 8086 |
| **Shopcart** | `/api/shopcart/**` | 8087 |
| **Shipping** | `/api/shipping/**` | 8088 |
| **Notification** | `/api/notification/**` | 8089 |
| **Marketing** | `/api/marketing/**` | 8090 |
| **Review** | `/api/review/**` | 8091 |
| **Search** | `/api/search/**` | 8092 |
| **Store** | `/api/store/**` | 8093 |
| **Chatbot** | `/api/chatbot/**` | 8094 |

## Documentación Swagger

### Swagger UI Agregado

Accede a toda la documentación de APIs en un solo lugar:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints OpenAPI por Servicio

```
GET http://localhost:8080/v3/api-docs                    # Gateway
GET http://localhost:8080/v3/api-docs/catalog            # Catalog Service
GET http://localhost:8080/v3/api-docs/identity           # Identity Service
GET http://localhost:8080/v3/api-docs/user               # User Service
GET http://localhost:8080/v3/api-docs/order              # Order Service
GET http://localhost:8080/v3/api-docs/payment            # Payment Service
GET http://localhost:8080/v3/api-docs/inventory          # Inventory Service
GET http://localhost:8080/v3/api-docs/shopcart           # Shopcart Service
GET http://localhost:8080/v3/api-docs/shipping           # Shipping Service
GET http://localhost:8080/v3/api-docs/notification       # Notification Service
GET http://localhost:8080/v3/api-docs/marketing          # Marketing Service
GET http://localhost:8080/v3/api-docs/review             # Review Service
GET http://localhost:8080/v3/api-docs/search             # Search Service
GET http://localhost:8080/v3/api-docs/store              # Store Service
GET http://localhost:8080/v3/api-docs/chatbot            # Chatbot Service
```

## Autenticación

### Flujo de Autenticación

1. **Login** - Obtener token JWT:
```http
POST http://localhost:8080/api/identity/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400,
    "tokenType": "Bearer"
}
```

2. **Registro** - Crear nueva cuenta:
```http
POST http://localhost:8080/api/identity/auth/register
Content-Type: application/json

{
    "email": "newuser@example.com",
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe"
}
```

3. **Usar token en requests**:
```http
GET http://localhost:8080/api/catalog/products
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### OAuth2 (Google)

```http
GET http://localhost:8080/api/identity/oauth2/authorization/google
```

## Endpoints Públicos

Los siguientes endpoints NO requieren autenticación:

```
# Health checks
GET /actuator/health
GET /actuator/info

# Swagger/OpenAPI
GET /swagger-ui.html
GET /swagger-ui/**
GET /v3/api-docs/**
GET /webjars/**

# Autenticación
POST /api/identity/auth/login
POST /api/identity/auth/register
POST /api/identity/auth/refresh
GET  /api/identity/oauth2/**

# Catálogo público
GET /api/catalog/products
GET /api/catalog/products/{id}
GET /api/catalog/categories
GET /api/catalog/categories/{id}

# Búsqueda pública
GET /api/search/**

# Tiendas públicas
GET /api/store/public/**

# Reviews públicas
GET /api/review/public/**
```

## Endpoints Protegidos

Requieren token JWT válido:

```
# Usuario
GET    /api/user/profile
PUT    /api/user/profile
DELETE /api/user/account

# Carrito
GET    /api/shopcart/cart
POST   /api/shopcart/cart/items
PUT    /api/shopcart/cart/items/{id}
DELETE /api/shopcart/cart/items/{id}

# Órdenes
GET    /api/order/orders
POST   /api/order/orders
GET    /api/order/orders/{id}
PUT    /api/order/orders/{id}/cancel

# Pagos
POST   /api/payment/process
GET    /api/payment/history
POST   /api/payment/webhook/stripe

# Envíos
GET    /api/shipping/shipments
POST   /api/shipping/shipments/{orderId}/track
GET    /api/shipping/rates

# Notificaciones
GET    /api/notification/notifications
PUT    /api/notification/notifications/{id}/read
POST   /api/notification/preferences

# Reviews
POST   /api/review/reviews
PUT    /api/review/reviews/{id}
DELETE /api/review/reviews/{id}

# Chatbot
POST   /api/chatbot/chat
GET    /api/chatbot/history
```

## Rate Limiting

El Gateway implementa rate limiting por IP:

| Endpoint Pattern | Límite | Ventana |
|------------------|--------|---------|
| `/api/identity/auth/**` | 10 req | 1 min |
| `/api/payment/**` | 5 req | 1 min |
| `/api/**` (otros) | 100 req | 1 min |

Headers de respuesta:
```
X-RateLimit-Remaining: 95
X-RateLimit-Limit: 100
X-RateLimit-Reset: 1640000000
```

Cuando se excede el límite:
```http
HTTP/1.1 429 Too Many Requests
Retry-After: 60
```

## Circuit Breaker

El Gateway usa Resilience4j para circuit breaker con fallback:

### Estados del Circuit Breaker

- **CLOSED**: Funcionamiento normal
- **OPEN**: Servicio no disponible, se activa fallback
- **HALF_OPEN**: Probando si el servicio se recuperó

### Fallback Responses

Cuando un servicio no está disponible:

```http
GET /api/catalog/products
```

Response (503 Service Unavailable):
```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 503,
    "error": "Service Unavailable",
    "message": "Catalog service is temporarily unavailable. Please try again later.",
    "service": "catalog"
}
```

## Logging

El Gateway registra todas las requests:

```
[Gateway] Request: GET /api/catalog/products
[Gateway] Headers: Authorization=Bearer ***, X-Request-Id=abc123
[Gateway] Response: 200 OK (150ms)
```

### Request Tracing

Cada request recibe un ID único:

```
X-Request-Id: 550e8400-e29b-41d4-a716-446655440000
X-Trace-Id: abc123def456
```

## Ejemplos de Uso

### cURL

```bash
# Login
curl -X POST http://localhost:8080/api/identity/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# Obtener productos (público)
curl http://localhost:8080/api/catalog/products

# Obtener productos con filtros
curl "http://localhost:8080/api/catalog/products?category=clothing&gender=male&page=0&size=20"

# Agregar al carrito (autenticado)
curl -X POST http://localhost:8080/api/shopcart/cart/items \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"productId":"123","quantity":2}'

# Crear orden
curl -X POST http://localhost:8080/api/order/orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"shippingAddressId":"addr_123","paymentMethodId":"pm_123"}'
```

### JavaScript (Fetch)

```javascript
// Login
const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/identity/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  return response.json();
};

// Fetch con token
const fetchWithAuth = async (url, token) => {
  const response = await fetch(url, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return response.json();
};

// Ejemplo de uso
const token = (await login('user@example.com', 'pass123')).accessToken;
const cart = await fetchWithAuth('http://localhost:8080/api/shopcart/cart', token);
```

### Python (requests)

```python
import requests

BASE_URL = 'http://localhost:8080'

# Login
def login(email, password):
    response = requests.post(f'{BASE_URL}/api/identity/auth/login', 
        json={'email': email, 'password': password})
    return response.json()

# Request autenticada
def get_cart(token):
    headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(f'{BASE_URL}/api/shopcart/cart', headers=headers)
    return response.json()

# Uso
auth = login('user@example.com', 'password123')
cart = get_cart(auth['accessToken'])
```

## Monitoreo

### Health Endpoints

```http
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/health/liveness
GET http://localhost:8080/actuator/health/readiness
```

### Métricas (Prometheus)

```http
GET http://localhost:8080/actuator/prometheus
```

Métricas disponibles:
- `gateway_requests_total` - Total de requests
- `gateway_requests_seconds` - Latencia de requests
- `resilience4j_circuitbreaker_state` - Estado de circuit breakers
- `jvm_memory_used_bytes` - Uso de memoria JVM

## Errores Comunes

### 401 Unauthorized

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid or expired token"
}
```

**Solución**: Obtener nuevo token con refresh token o re-login.

### 403 Forbidden

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 403,
    "error": "Forbidden",
    "message": "Access denied. Insufficient permissions."
}
```

**Solución**: Verificar roles del usuario.

### 503 Service Unavailable

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 503,
    "error": "Service Unavailable",
    "message": "Service temporarily unavailable"
}
```

**Solución**: Reintentar después del tiempo indicado en `Retry-After`.

### 504 Gateway Timeout

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 504,
    "error": "Gateway Timeout",
    "message": "Service did not respond in time"
}
```

**Solución**: El microservicio está tardando demasiado. Verificar logs del servicio.
