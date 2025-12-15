# Microservice Catalog

## Descripción

Microservicio de catálogo de productos para la plataforma de e-commerce Karibea. Gestiona el ciclo de vida completo de productos incluyendo creación, actualización, publicación, desactivación y manejo de variantes.

## Arquitectura

Este microservicio sigue una **Arquitectura Hexagonal (Ports & Adapters)**:

```
microservice-catalog/
├── application/          # Capa de aplicación
│   ├── dto/             # Data Transfer Objects con @Schema
│   ├── exception/       # Excepciones de aplicación
│   ├── mapper/          # Mappers (Domain ↔ DTO)
│   └── usecases/        # Casos de uso
├── domain/              # Capa de dominio (núcleo)
│   ├── events/          # Eventos de dominio
│   ├── exceptions/      # Excepciones de dominio
│   ├── models/          # Entidades y Value Objects
│   └── port/            # Puertos (in/out)
└── infrastructure/      # Capa de infraestructura
    ├── adapters/        # Adaptadores externos
    ├── configs/         # Configuraciones (Security, OpenAPI)
    ├── controller/      # REST Controllers
    ├── entities/        # Entidades JPA
    ├── exceptions/      # Manejo de excepciones
    ├── kafka/           # Configuración y consumers/producers Kafka
    └── repositories/    # Repositorios JPA
```

## Tecnologías

- **Java 17**
- **Spring Boot 3.5**
- **Spring Cloud** (Config, Eureka)
- **Spring Kafka** - Comunicación event-driven
- **Spring Security OAuth2** - Resource Server con JWT
- **PostgreSQL** - Base de datos
- **H2** - Base de datos para tests
- **springdoc-openapi 2.7.0** - Documentación API

## Event-Driven Architecture

### Eventos Publicados (Kafka Producer)

| Topic | Evento | Descripción |
|-------|--------|-------------|
| `products` | `ProductCreatedEvent` | Producto creado |
| `products` | `ProductUpdatedEvent` | Producto actualizado |
| `products` | `ProductPublishedEvent` | Producto publicado |
| `products` | `ProductDeactivatedEvent` | Producto desactivado |
| `products` | `VariantAddedEvent` | Variante agregada |

### Eventos Consumidos (Kafka Consumer)

| Topic | Descripción |
|-------|-------------|
| `inventory.updated` | Actualización de stock |
| `inventory.low-stock` | Alerta de stock bajo |
| `inventory.out-of-stock` | Producto sin stock |

## API Endpoints

### Products

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/products` | Listar productos (paginado) | ❌ |
| GET | `/api/v1/products/{id}` | Obtener producto por ID | ❌ |
| POST | `/api/v1/products` | Crear producto | ✅ |
| PUT | `/api/v1/products/{id}` | Actualizar producto | ✅ |
| POST | `/api/v1/products/{id}/publish` | Publicar producto | ✅ |
| POST | `/api/v1/products/{id}/deactivate` | Desactivar producto | ✅ |
| POST | `/api/v1/products/{id}/variants` | Agregar variante | ✅ |

## Configuración

### Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `DB_USERNAME` | Usuario PostgreSQL | postgres |
| `DB_PASSWORD` | Contraseña PostgreSQL | postgres |
| `KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka | localhost:9092 |
| `EUREKA_URI` | URL de Eureka | http://localhost:8761/eureka |
| `OAUTH2_ISSUER_URI` | URI del issuer OAuth2 | http://localhost:9000 |
| `OAUTH2_JWK_SET_URI` | URI del JWK Set | http://localhost:9000/oauth2/jwks |

## Ejecución

### Desarrollo Local

```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Con Docker

```bash
docker build -t microservice-catalog .
docker run -p 8082:8082 microservice-catalog
```

## Documentación API

- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8082/v3/api-docs
- **OpenAPI YAML**: http://localhost:8082/v3/api-docs.yaml

## Health Check

- **Actuator Health**: http://localhost:8082/actuator/health

## Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar con coverage
./mvnw test jacoco:report
```

## Dependencias de Servicios

- **microservice-config**: Configuración centralizada
- **microservice-eureka**: Service Discovery
- **microservice-identity**: Autenticación JWT
- **microservice-inventory**: Recibe eventos de stock
