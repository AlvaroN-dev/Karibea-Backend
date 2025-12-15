# Order Microservice API

Order management microservice for the Karibea ecosystem, built with **Spring Boot 3.5**, **Hexagonal Architecture**, **DDD**, and **Event-Driven Architecture** using Apache Kafka.

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
com.microservice.order
├── application/          # Use cases, DTOs, mappers
│   ├── dto/
│   │   ├── request/     # Input DTOs
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
http://localhost:8084/api/v1/orders
```

## 📖 Swagger UI

```
http://localhost:8084/swagger-ui.html
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
| `POST` | `/api/v1/orders` | Create a new order |
| `GET` | `/api/v1/orders/{orderId}` | Get order by ID |
| `GET` | `/api/v1/orders/number/{orderNumber}` | Get order by order number |
| `GET` | `/api/v1/orders/customer/{customerId}` | Get orders by customer (paginated) |
| `GET` | `/api/v1/orders/store/{storeId}` | Get orders by store (paginated) |
| `POST` | `/api/v1/orders/{orderId}/cancel` | Cancel an order |
| `POST` | `/api/v1/orders/{orderId}/confirm` | Confirm an order |
| `PATCH` | `/api/v1/orders/{orderId}/status` | Change order status |

---

## 📝 Request/Response Examples

### 1. Create Order

**POST** `/api/v1/orders`

**Request Body:**
```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440001",
  "storeId": "550e8400-e29b-41d4-a716-446655440002",
  "currency": "USD",
  "shippingAddress": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "billingAddress": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "items": [
    {
      "productId": "550e8400-e29b-41d4-a716-446655440010",
      "productName": "Wireless Headphones",
      "variantName": "Black",
      "sku": "WH-001-BLK",
      "imageUrl": "https://example.com/images/headphones.jpg",
      "unitPrice": 99.99,
      "quantity": 2
    }
  ],
  "customerNotes": "Please gift wrap"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440100",
  "orderNumber": "ORD-20241215-A1B2C3D4",
  "customer": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": null,
    "lastName": null,
    "email": null,
    "phone": null
  },
  "store": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": null,
    "email": null,
    "phone": null,
    "logoUrl": null
  },
  "payment": null,
  "shipment": null,
  "status": "PENDING",
  "currency": "USD",
  "shippingAddress": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "fullAddress": "123 Main Street, New York, NY 10001, USA"
  },
  "billingAddress": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "fullAddress": "123 Main Street, New York, NY 10001, USA"
  },
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440200",
      "product": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "name": null,
        "description": null,
        "brand": null,
        "sku": null,
        "basePrice": null,
        "currency": null,
        "primaryImageUrl": null,
        "isActive": null
      },
      "productName": "Wireless Headphones",
      "variantName": "Black",
      "sku": "WH-001-BLK",
      "imageUrl": "https://example.com/images/headphones.jpg",
      "unitPrice": 99.99,
      "quantity": 2,
      "discountAmount": 0.00,
      "taxAmount": 0.00,
      "lineTotal": 199.98,
      "status": "ACTIVE"
    }
  ],
  "coupons": [],
  "statusHistory": [],
  "subtotal": 199.98,
  "discountTotal": 0.00,
  "taxTotal": 0.00,
  "shippingTotal": 0.00,
  "grandTotal": 199.98,
  "customerNotes": "Please gift wrap",
  "confirmedAt": null,
  "shippedAt": null,
  "deliveredAt": null,
  "cancelledAt": null,
  "createdAt": "2024-12-15T21:50:00",
  "updatedAt": "2024-12-15T21:50:00"
}
```

> **Note:** External entity fields (customer, store, product, payment, shipment) return only the ID. Full data can be fetched by clients using the respective microservice APIs or enriched via a BFF (Backend For Frontend) pattern.

---

### 2. Get Order by ID

**GET** `/api/v1/orders/{orderId}`

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440100",
  "orderNumber": "ORD-20241215-A1B2C3D4",
  "status": "CONFIRMED",
  "payment": {
    "id": "550e8400-e29b-41d4-a716-446655440300",
    "transactionId": null,
    "paymentMethod": null,
    "paymentStatus": null,
    "amount": null,
    "currency": null,
    "processedAt": null
  },
  ...
}
```

---

### 3. Get Orders by Customer (Paginated)

**GET** `/api/v1/orders/customer/{customerId}?page=0&size=10&sort=createdAt,desc`

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440100",
      "orderNumber": "ORD-20241215-A1B2C3D4",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "direction": "DESC"
    }
  },
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

---

### 4. Cancel Order

**POST** `/api/v1/orders/{orderId}/cancel`

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440100",
  "reason": "Customer requested cancellation"
}
```

**Response:** `204 No Content`

---

### 5. Confirm Order

**POST** `/api/v1/orders/{orderId}/confirm?paymentId={paymentId}`

**Response:** `204 No Content`

---

### 6. Change Order Status

**PATCH** `/api/v1/orders/{orderId}/status`

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440100",
  "newStatus": "SHIPPED",
  "reason": "Package dispatched",
  "relatedEntityId": "550e8400-e29b-41d4-a716-446655440300"
}
```

**Valid Status Transitions:**
| From | To |
|------|-----|
| `PENDING` | `CONFIRMED`, `CANCELLED` |
| `CONFIRMED` | `PROCESSING`, `CANCELLED` |
| `PROCESSING` | `SHIPPED`, `CANCELLED` |
| `SHIPPED` | `DELIVERED`, `RETURNED` |
| `DELIVERED` | `RETURNED`, `COMPLETED` |
| `RETURNED` | `REFUNDED` |

**Response:** `204 No Content`

---

## ❌ Error Responses

### 400 Bad Request - Validation Error
```json
{
  "code": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "items",
      "message": "At least one item is required"
    }
  ],
  "timestamp": "2024-12-15T21:50:00Z"
}
```

### 404 Not Found
```json
{
  "code": "ORDER_NOT_FOUND",
  "message": "Order not found with id: 550e8400-e29b-41d4-a716-446655440100",
  "timestamp": "2024-12-15T21:50:00Z",
  "traceId": "abc123"
}
```

### 409 Conflict - Invalid State Transition
```json
{
  "code": "INVALID_STATE_TRANSITION",
  "message": "Cannot transition from DELIVERED to PROCESSING",
  "timestamp": "2024-12-15T21:50:00Z",
  "traceId": "abc123"
}
```

### 422 Unprocessable Entity - Domain Error
```json
{
  "code": "INVARIANT_VIOLATION",
  "message": "Order must have at least one item",
  "timestamp": "2024-12-15T21:50:00Z",
  "traceId": "abc123"
}
```

---

## 📨 Kafka Events

### Published Events (Topic: `order-events`)

| Event | When Published | Payload |
|-------|----------------|---------|
| `OrderCreated` | New order created | Order details, items, customer, store |
| `OrderConfirmed` | Order confirmed after payment | Order ID, payment ID, confirmed timestamp |
| `OrderStatusChanged` | Status changed | Order ID, previous status, new status, reason |
| `OrderCancelled` | Order cancelled | Order ID, reason, cancelled timestamp |
| `OrderShipped` | Order shipped | Order ID, shipment ID, shipped timestamp |
| `OrderDelivered` | Order delivered | Order ID, delivered timestamp |
| `OrderCouponApplied` | Coupon applied | Order ID, coupon code, discount amount |

### Consumed Events

| Topic | Events | Action |
|-------|--------|--------|
| `payment-events` | `PaymentCompleted` | Confirms order |
| `payment-events` | `PaymentFailed` | Cancels order |
| `payment-events` | `PaymentRefunded` | Updates order to REFUNDED |
| `shipment-events` | `ShipmentCreated` | Updates order to PROCESSING |
| `shipment-events` | `ShipmentShipped` | Updates order to SHIPPED |
| `shipment-events` | `ShipmentDelivered` | Updates order to DELIVERED |
| `shipment-events` | `ShipmentReturned` | Updates order to RETURNED |
| `inventory-events` | `InventoryReservationFailed` | Cancels order |

---

## ⚙️ Configuration

```yaml
spring:
  application:
    name: microservice-order
  
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/karibea_order}
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
  
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    consumer:
      group-id: order-service
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
  port: ${SERVER_PORT:8084}

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka}
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/karibea_order` |
| `DATABASE_USERNAME` | Database username | `postgres` |
| `DATABASE_PASSWORD` | Database password | `postgres` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka broker addresses | `localhost:9092` |
| `OAUTH2_ISSUER_URI` | OAuth2 token issuer URI | - |
| `SERVER_PORT` | Application port | `8084` |
| `EUREKA_SERVER_URL` | Eureka server URL | `http://localhost:8761/eureka` |

---

## 🗄️ Database Schema

### Tables

- **orders** - Main order table
- **order_items** - Order line items
- **order_coupons** - Applied coupons
- **order_status_history** - Status change audit trail
- **status_orders** - Order status reference (lookup table)

### Key Relationships

```
orders (1) ──→ (N) order_items
orders (1) ──→ (N) order_coupons  
orders (1) ──→ (N) order_status_history
orders (N) ──→ (1) status_orders
```

### External References (Foreign Keys to other microservices)

| Field | References |
|-------|------------|
| `external_user_profiles_id` | `user_profiles.id` (User Service) |
| `external_store_id` | `stores.id_store` (Store Service) |
| `external_payment_id` | `transactions.id_transaction` (Payment Service) |
| `external_shipment_id` | `shipments.id` (Shipping Service) |
| `order_items.external_product_id` | `products.id` (Catalog Service) |

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
docker build -t microservice-order .
docker run -p 8084:8084 microservice-order
```

---

## 📄 License

MIT License - see [LICENSE](../LICENSE) for details.
