# Microservice Inventory

## Descripción
Microservicio de gestión de inventario para la plataforma e-commerce Karibea. Maneja niveles de stock, reservaciones, movimientos de inventario y almacenes.

## Arquitectura
- **Arquitectura Hexagonal** (Ports & Adapters)
- **Domain-Driven Design** (DDD)
- **Event-Driven Architecture** con Kafka

### Estructura del Proyecto
```
com.microservice.inventory
├── application/
│   ├── dto/           # Data Transfer Objects
│   ├── exception/     # Application exceptions
│   ├── mapper/        # Domain to DTO mappers
│   └── usecases/      # Application services (use cases)
├── domain/
│   ├── events/        # Domain events
│   ├── exceptions/    # Domain exceptions
│   ├── models/        # Domain models (aggregates, entities, value objects)
│   └── port/
│       ├── in/        # Input ports (use case interfaces)
│       └── out/       # Output ports (repository interfaces)
├── infrastructure/
│   ├── adapters/      # Repository implementations
│   ├── configs/       # Spring configurations
│   ├── controller/    # REST controllers
│   ├── entities/      # JPA entities
│   ├── exceptions/    # Infrastructure exceptions
│   └── repositories/  # JPA repositories
└── kafka/
    ├── config/        # Kafka configuration
    ├── consumer/      # Kafka consumers
    └── producer/      # Kafka producers
```

## Tecnologías
- Java 17
- Spring Boot 3.5
- Spring Data JPA
- Spring Kafka
- Spring Security OAuth2
- PostgreSQL
- H2 (tests)
- springdoc-openapi 2.7.0

## Requisitos
- JDK 17+
- Maven 3.9+
- PostgreSQL 15+
- Apache Kafka 3.x
- Docker (opcional)

## Configuración

### Variables de Entorno
| Variable | Descripción | Default |
|----------|-------------|---------|
| DB_HOST | Host de PostgreSQL | localhost |
| DB_PORT | Puerto de PostgreSQL | 5432 |
| DB_NAME | Nombre de la base de datos | karibea_stock |
| DB_USERNAME | Usuario de PostgreSQL | postgres |
| DB_PASSWORD | Contraseña de PostgreSQL | postgres |
| KAFKA_SERVERS | Servidores Kafka | localhost:9092 |
| EUREKA_SERVER_URI | URI de Eureka | http://localhost:8761/eureka/ |
| CONFIG_SERVER_URI | URI de Config Server | http://microservice-config:8888 |
| OAUTH2_ISSUER_URI | URI del issuer OAuth2 | http://localhost:8080/realms/karibea |
| SERVER_PORT | Puerto del servidor | 8086 |

### Base de Datos
```sql
-- Crear base de datos
CREATE DATABASE karibea_stock;
```

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

### Docker
```bash
# Construir imagen
docker build -t microservice-inventory .

# Ejecutar contenedor
docker run -p 8086:8086 \
  -e DB_HOST=host.docker.internal \
  -e KAFKA_SERVERS=host.docker.internal:9092 \
  microservice-inventory
```

## API Documentation
- **Swagger UI**: http://localhost:8086/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8086/v3/api-docs
- **Documentación Detallada**: [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)

## Endpoints Principales

### Stock Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/stock | Create stock |
| GET | /api/v1/stock/{id} | Get stock by ID |
| GET | /api/v1/stock/variant/{variantId} | Get stock by variant |
| GET | /api/v1/stock/warehouse/{warehouseId} | Get stock by warehouse |
| GET | /api/v1/stock/warehouse/{warehouseId}/low-stock | Get low stock items |
| POST | /api/v1/stock/adjust | Adjust stock quantity |
| POST | /api/v1/stock/reserve | Reserve stock |
| POST | /api/v1/stock/reservations/{id}/release | Release reservation |
| POST | /api/v1/stock/reservations/{id}/confirm | Confirm reservation |

## Eventos Kafka

### Eventos Publicados
| Topic | Evento | Descripción |
|-------|--------|-------------|
| inventory.stock.created | StockCreatedEvent | Stock inicializado |
| inventory.stock.adjusted | StockAdjustedEvent | Stock ajustado |
| inventory.stock.reserved | StockReservedEvent | Stock reservado |
| inventory.stock.released | StockReleasedEvent | Reservación liberada |
| inventory.low-stock.alert | LowStockAlertEvent | Alerta de bajo stock |

### Eventos Consumidos
| Topic | Acción |
|-------|--------|
| order.confirmed | Confirmar reservación |
| order.cancelled | Liberar reservación |
| cart.expired | Liberar reservación |

## Modelo de Dominio

### Aggregate: Stock
- **Stock**: Aggregate root que maneja inventario
- **StockMovement**: Movimientos de inventario
- **StockReservation**: Reservaciones de stock

### Enums
- **MovementType**: PURCHASE, SALE, RETURN, ADJUSTMENT_IN, ADJUSTMENT_OUT, etc.
- **ReservationType**: ORDER, CART
- **ReservationStatus**: PENDING, CONFIRMED, COMPLETED, CANCELLED, EXPIRED

## Tests
```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar tests con cobertura
./mvnw test jacoco:report
```

## Healthcheck
```bash
curl http://localhost:8086/actuator/health
```

## Métricas
```bash
curl http://localhost:8086/actuator/metrics
```

## Licencia
Apache 2.0
