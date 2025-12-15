# Shipping Microservice API

Shipping management microservice for the Karibea ecosystem, built with **Spring Boot 3.5**, **Hexagonal Architecture**, **DDD**, and **Event-Driven Architecture** using Apache Kafka.

## 📋 Table of Contents

- [Architecture](#architecture)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [API Endpoints](#api-endpoints)
- [Request/Response Examples](#requestresponse-examples)
- [Error Responses](#error-responses)
- [Kafka Events](#kafka-events)
- [Configuration](#configuration)
- [Database Schema](#database-schema)

---

## 🏗️ Architecture

This microservice follows **Hexagonal Architecture** (Ports & Adapters) with **Domain-Driven Design** principles:

```
com.microservice.shipping
├── application/          # Use cases, DTOs, mappers
│   ├── dto/
│   │   ├── request/     # Input DTOs with validation
│   │   └── response/    # Output DTOs with enriched data
│   ├── exception/       # Application exceptions
│   ├── mapper/          # Domain ↔ DTO mappers
│   └── usecases/        # Business use case implementations
├── domain/              # Pure domain logic (no framework dependencies)
│   ├── events/          # Domain events
│   ├── exceptions/      # Domain exceptions
│   ├── models/          # Aggregates, entities, value objects
│   └── port/
│       ├── in/          # Inbound ports (use case interfaces)
│       └── out/         # Outbound ports (repository, messaging)
└── infrastructure/      # Framework-specific implementations
    ├── adapters/        # Port implementations
    ├── configs/         # Spring configurations
    ├── controller/      # REST controllers
    ├── entities/        # JPA entities
    ├── exceptions/      # Exception handlers
    ├── kafka/
    │   ├── config/      # Kafka configuration
    │   ├── consumer/    # Event consumers
    │   └── producer/    # Event producers
    └── repositories/    # JPA repositories
```

### Key Principles

- ✅ **Event-Driven**: All inter-service communication via Kafka
- ✅ **No WebClient/RestTemplate**: Removed in favor of event-driven patterns
- ✅ **Domain Isolation**: Domain layer has zero framework dependencies
- ✅ **Eventual Consistency**: Accepted for inter-service operations
- ✅ **Idempotent Producers**: Kafka producers configured for exactly-once semantics

---

## 🌐 Base URL

```
http://localhost:8086/api/v1/shipments
```

## 📖 Swagger UI

```
http://localhost:8086/swagger-ui.html
```

## 🔐 Authentication

All endpoints require JWT Bearer token authentication via OAuth2 Resource Server.

```
Authorization: Bearer <jwt_token>
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/shipments` | Create a new shipment |
| `GET` | `/api/v1/shipments/{shipmentId}` | Get shipment by ID |
| `GET` | `/api/v1/shipments/tracking/{trackingNumber}` | Get shipment by tracking number |
| `GET` | `/api/v1/shipments/order/{orderId}` | Get shipments by order (paginated) |
| `GET` | `/api/v1/shipments/customer/{customerId}` | Get shipments by customer (paginated) |
| `PATCH` | `/api/v1/shipments/{shipmentId}/status` | Update shipment status |
| `POST` | `/api/v1/shipments/{shipmentId}/cancel` | Cancel a shipment |
| `POST` | `/api/v1/shipments/{shipmentId}/tracking` | Add tracking event |

---

## 📝 Request/Response Examples

### 1. Create Shipment

**POST** `/api/v1/shipments`

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440001",
  "storeId": "550e8400-e29b-41d4-a716-446655440002",
  "customerId": "550e8400-e29b-41d4-a716-446655440003",
  "carrierId": "550e8400-e29b-41d4-a716-446655440004",
  "shippingMethodId": "550e8400-e29b-41d4-a716-446655440005",
  "carrierCode": "FEDEX",
  "carrierName": "FedEx Express",
  "shippingMethodName": "Express Overnight",
  "originAddress": {
    "street": "100 Warehouse Blvd",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA"
  },
  "destinationAddress": {
    "street": "123 Main Street, Suite 400",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "shippingCost": 15.99,
  "items": [
    {
      "orderItemId": "550e8400-e29b-41d4-a716-446655440010",
      "productId": "550e8400-e29b-41d4-a716-446655440011",
      "productName": "Wireless Bluetooth Headphones",
      "sku": "WBH-001-BLK",
      "quantity": 2
    }
  ],
  "notes": "Handle with care - fragile items"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440100",
  "trackingNumber": "FEDEX-20241215-A1B2C3D4",
  "order": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "orderNumber": null,
    "orderStatus": null,
    "grandTotal": null,
    "currency": null
  },
  "store": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": null,
    "email": null,
    "phone": null,
    "logoUrl": null
  },
  "customer": {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "firstName": null,
    "lastName": null,
    "email": null,
    "phone": null
  },
  "carrier": {
    "id": "550e8400-e29b-41d4-a716-446655440004",
    "name": "FedEx Express",
    "code": "FEDEX",
    "trackingUrlTemplate": null,
    "isActive": null,
    "logoUrl": null
  },
  "shippingMethodName": "Express Overnight",
  "status": "PENDING",
  "originAddress": {
    "street": "100 Warehouse Blvd",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA",
    "fullAddress": "100 Warehouse Blvd, Los Angeles, CA 90001, USA"
  },
  "destinationAddress": {
    "street": "123 Main Street, Suite 400",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "fullAddress": "123 Main Street, Suite 400, New York, NY 10001, USA"
  },
  "shippingCost": 15.99,
  "weightKg": null,
  "dimensions": null,
  "notes": "Handle with care - fragile items",
  "estimatedDeliveryDate": null,
  "pickedUpAt": null,
  "deliveredAt": null,
  "cancelledAt": null,
  "createdAt": "2024-12-15T10:00:00",
  "updatedAt": "2024-12-15T10:00:00",
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440200",
      "product": {
        "id": "550e8400-e29b-41d4-a716-446655440011",
        "name": null,
        "description": null,
        "brand": null,
        "sku": null,
        "basePrice": null,
        "primaryImageUrl": null,
        "weightKg": null
      },
      "productName": "Wireless Bluetooth Headphones",
      "sku": "WBH-001-BLK",
      "quantity": 2
    }
  ],
  "trackingEvents": []
}
```

> **Note:** External entity fields (order, store, customer, product) return only the ID. Full data can be fetched by clients using the respective microservice APIs or enriched via a BFF (Backend For Frontend) pattern.

---

### 2. Get Shipment by Tracking Number

**GET** `/api/v1/shipments/tracking/FEDEX-20241215-A1B2C3D4`

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440100",
  "trackingNumber": "FEDEX-20241215-A1B2C3D4",
  "status": "IN_TRANSIT",
  "trackingEvents": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440300",
      "status": "LABEL_CREATED",
      "location": "Los Angeles, CA",
      "description": "Shipment label created",
      "occurredAt": "2024-12-15T10:00:00",
      "createdAt": "2024-12-15T10:01:00"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440301",
      "status": "PICKED_UP",
      "location": "Los Angeles, CA",
      "description": "Package picked up by carrier",
      "occurredAt": "2024-12-15T14:30:00",
      "createdAt": "2024-12-15T14:31:00"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440302",
      "status": "IN_TRANSIT",
      "location": "Distribution Center, Phoenix, AZ",
      "description": "Package in transit",
      "occurredAt": "2024-12-16T08:00:00",
      "createdAt": "2024-12-16T08:01:00"
    }
  ]
}
```

---

### 3. Update Shipment Status

**PATCH** `/api/v1/shipments/{shipmentId}/status`

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440100",
  "newStatus": "IN_TRANSIT",
  "location": "Distribution Center, Chicago, IL",
  "reason": "Package in transit to destination"
}
```

**Valid Status Transitions:**
| From | To |
|------|-----|
| `PENDING` | `CONFIRMED`, `CANCELLED` |
| `CONFIRMED` | `PICKED_UP`, `CANCELLED` |
| `PICKED_UP` | `IN_TRANSIT`, `CANCELLED` |
| `IN_TRANSIT` | `OUT_FOR_DELIVERY`, `RETURNED` |
| `OUT_FOR_DELIVERY` | `DELIVERED`, `RETURNED`, `FAILED_ATTEMPT` |
| `FAILED_ATTEMPT` | `OUT_FOR_DELIVERY`, `RETURNED` |
| `DELIVERED` | `RETURNED` |
| `RETURNED` | - (final state) |
| `CANCELLED` | - (final state) |

**Response:** `204 No Content`

---

### 4. Cancel Shipment

**POST** `/api/v1/shipments/{shipmentId}/cancel`

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440100",
  "reason": "Customer requested cancellation before dispatch"
}
```

**Response:** `204 No Content`

---

### 5. Add Tracking Event

**POST** `/api/v1/shipments/{shipmentId}/tracking`

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440100",
  "status": "IN_TRANSIT",
  "location": "Distribution Center, Miami, FL",
  "description": "Package arrived at distribution center and is being processed",
  "occurredAt": "2024-12-15T14:30:00"
}
```

**Response:** `201 Created`

---

## ❌ Error Responses

### 400 Bad Request - Validation Error
```json
{
  "code": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "destinationAddress",
      "message": "Destination address is required"
    }
  ],
  "timestamp": "2024-12-15T10:00:00Z"
}
```

### 404 Not Found
```json
{
  "code": "SHIPMENT_NOT_FOUND",
  "message": "Shipment not found with id: 550e8400-e29b-41d4-a716-446655440100",
  "timestamp": "2024-12-15T10:00:00Z",
  "traceId": "abc123"
}
```

### 409 Conflict - Invalid State Transition
```json
{
  "code": "INVALID_STATE_TRANSITION",
  "message": "Cannot transition from DELIVERED to PENDING",
  "timestamp": "2024-12-15T10:00:00Z",
  "traceId": "abc123"
}
```

---

## 📨 Kafka Events

### Published Events (Topic: `shipment-events`)

| Event | When Published | Payload |
|-------|----------------|---------|
| `ShipmentCreated` | New shipment created | Shipment details, items, addresses |
| `ShipmentPickedUp` | Carrier picks up package | Shipment ID, pickup timestamp |
| `ShipmentStatusChanged` | Status changed | Shipment ID, previous status, new status |
| `ShipmentDelivered` | Package delivered | Shipment ID, delivery timestamp |
| `ShipmentCancelled` | Shipment cancelled | Shipment ID, reason, cancelled timestamp |
| `ShipmentReturned` | Package returned | Shipment ID, return reason |
| `TrackingEventAdded` | New tracking event | Shipment ID, event details |

### Consumed Events

| Topic | Events | Action |
|-------|--------|--------|
| `order-events` | `OrderConfirmed` | Creates shipment for the order |
| `order-events` | `OrderCancelled` | Cancels pending shipments |

---

## ⚙️ Configuration

```yaml
spring:
  application:
    name: microservice-shipping
  
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/karibea_shipping}
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
  
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    consumer:
      group-id: shipping-service
      auto-offset-reset: earliest
      enable-auto-commit: false
    producer:
      acks: all
      retries: 3
      properties:
        enable.idempotence: true
  
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${OAUTH2_ISSUER_URI}

server:
  port: ${SERVER_PORT:8086}

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka}
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/karibea_shipping` |
| `DATABASE_USERNAME` | Database username | `postgres` |
| `DATABASE_PASSWORD` | Database password | `postgres` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka broker addresses | `localhost:9092` |
| `OAUTH2_ISSUER_URI` | OAuth2 token issuer URI | - |
| `SERVER_PORT` | Application port | `8086` |
| `EUREKA_SERVER_URL` | Eureka server URL | `http://localhost:8761/eureka` |

---

## 🗄️ Database Schema

### Tables

- **shipments** - Main shipment table
- **shipment_items** - Items in each shipment
- **tracking_events** - Tracking history
- **carriers** - Carrier information (lookup)
- **shipping_methods** - Available shipping methods
- **status_shipments** - Shipment status reference

### Key Relationships

```
shipments (1) ──→ (N) shipment_items
shipments (1) ──→ (N) tracking_events
shipments (N) ──→ (1) carriers
shipments (N) ──→ (1) shipping_methods
shipments (N) ──→ (1) status_shipments
tracking_events (N) ──→ (1) status_tracking_events
```

### External References (Foreign Keys to other microservices)

| Field | References |
|-------|------------|
| `external_order_id` | `orders.id_order` (Order Service) |
| `external_store_id` | `stores.id_store` (Store Service) |
| `external_customer_id` | `user_profiles.id` (User Service) |
| `shipment_items.external_order_item_id` | `order_items.id_order_item` (Order Service) |
| `shipment_items.external_product_id` | `products.id` (Catalog Service) |

---

## 🚀 Running the Service

### Prerequisites
- Java 17+
- PostgreSQL 15+
- Apache Kafka 3.x
- Eureka Server (optional for service discovery)

### Local Development
```bash
# Build
./mvnw clean package -DskipTests

# Run
./mvnw spring-boot:run

# Run with profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker
```bash
docker build -t microservice-shipping .
docker run -p 8086:8086 microservice-shipping
```

---

## 📄 License

MIT License - see [LICENSE](../LICENSE) for details.
