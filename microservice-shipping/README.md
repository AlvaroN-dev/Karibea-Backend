# Shipping Microservice API

Shipping management microservice for the Karibea ecosystem, built with **Spring Boot 3.5**, **Hexagonal Architecture**, and **DDD**.

## Base URL

```
http://localhost:8080/api/v1/shipments
```

## Authentication

All endpoints require JWT Bearer token authentication via OAuth2 Resource Server.

```
Authorization: Bearer <jwt_token>
```

---

## Endpoints

### 1. Create Shipment

**POST** `/api/v1/shipments`

Creates a new shipment for an order.

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440001",
  "storeId": "550e8400-e29b-41d4-a716-446655440002",
  "customerId": "550e8400-e29b-41d4-a716-446655440003",
  "carrierId": "550e8400-e29b-41d4-a716-446655440010",
  "shippingMethodId": "550e8400-e29b-41d4-a716-446655440020",
  "carrierCode": "FEDEX",
  "carrierName": "FedEx",
  "shippingMethodName": "Express Delivery",
  "originAddress": {
    "street": "100 Warehouse Blvd",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA"
  },
  "destinationAddress": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "shippingCost": 15.99,
  "items": [
    {
      "orderItemId": "550e8400-e29b-41d4-a716-446655440100",
      "productId": "550e8400-e29b-41d4-a716-446655440200",
      "productName": "Wireless Headphones",
      "sku": "WH-001-BLK",
      "quantity": 2
    }
  ],
  "notes": "Fragile - handle with care"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440500",
  "trackingNumber": "FEDEX-20241212-A1B2C3D4",
  "orderId": "550e8400-e29b-41d4-a716-446655440001",
  "storeId": "550e8400-e29b-41d4-a716-446655440002",
  "customerId": "550e8400-e29b-41d4-a716-446655440003",
  "carrierId": "550e8400-e29b-41d4-a716-446655440010",
  "carrierCode": "FEDEX",
  "carrierName": "FedEx",
  "shippingMethodName": "Express Delivery",
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
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "fullAddress": "123 Main Street, New York, NY 10001, USA"
  },
  "shippingCost": 15.99,
  "weightKg": null,
  "dimensions": null,
  "notes": "Fragile - handle with care",
  "estimatedDeliveryDate": null,
  "pickedUpAt": null,
  "deliveredAt": null,
  "cancelledAt": null,
  "createdAt": "2024-12-12T22:50:00",
  "updatedAt": "2024-12-12T22:50:00",
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440600",
      "productId": "550e8400-e29b-41d4-a716-446655440200",
      "productName": "Wireless Headphones",
      "sku": "WH-001-BLK",
      "quantity": 2
    }
  ],
  "trackingEvents": []
}
```

---

### 2. Get Shipment by ID

**GET** `/api/v1/shipments/{shipmentId}`

**Path Parameters:**
- `shipmentId` (UUID) - Shipment ID

**Response:** `200 OK`

---

### 3. Get Shipment by Tracking Number

**GET** `/api/v1/shipments/tracking/{trackingNumber}`

**Path Parameters:**
- `trackingNumber` (String) - Tracking number (e.g., FEDEX-20241212-A1B2C3D4)

**Response:** `200 OK`

---

### 4. Get Shipments by Order

**GET** `/api/v1/shipments/order/{orderId}?page=0&size=10`

**Path Parameters:**
- `orderId` (UUID) - Order ID

**Query Parameters:**
- `page` (int, default: 0) - Page number
- `size` (int, default: 10) - Page size

**Response:** `200 OK` (Paginated)

---

### 5. Get Shipments by Customer

**GET** `/api/v1/shipments/customer/{customerId}?page=0&size=10`

**Path Parameters:**
- `customerId` (UUID) - Customer ID

**Response:** `200 OK` (Paginated)

---

### 6. Update Shipment Status

**PATCH** `/api/v1/shipments/{shipmentId}/status`

Updates shipment status with transition validation.

**Path Parameters:**
- `shipmentId` (UUID) - Shipment ID

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440500",
  "newStatus": "PICKED_UP",
  "location": "Los Angeles Distribution Center",
  "reason": "Package picked up by carrier"
}
```

**Valid Status Transitions:**
- `PENDING` → `CONFIRMED`, `CANCELLED`
- `CONFIRMED` → `PICKED_UP`, `CANCELLED`
- `PICKED_UP` → `IN_TRANSIT`, `CANCELLED`
- `IN_TRANSIT` → `OUT_FOR_DELIVERY`, `RETURNED`
- `OUT_FOR_DELIVERY` → `DELIVERED`, `FAILED_DELIVERY`
- `FAILED_DELIVERY` → `OUT_FOR_DELIVERY`, `RETURNED`

**Response:** `204 No Content`

---

### 7. Cancel Shipment

**POST** `/api/v1/shipments/{shipmentId}/cancel`

Cancels a shipment if it's in a cancellable state.

**Path Parameters:**
- `shipmentId` (UUID) - Shipment ID

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440500",
  "reason": "Customer requested cancellation"
}
```

**Response:** `204 No Content`

---

### 8. Add Tracking Event

**POST** `/api/v1/shipments/{shipmentId}/tracking`

Adds a tracking event to a shipment.

**Path Parameters:**
- `shipmentId` (UUID) - Shipment ID

**Request Body:**
```json
{
  "shipmentId": "550e8400-e29b-41d4-a716-446655440500",
  "status": "IN_TRANSIT",
  "location": "Chicago Distribution Center",
  "description": "Package arrived at facility",
  "occurredAt": "2024-12-12T18:30:00"
}
```

**Valid Tracking Event Statuses:**
- `LABEL_CREATED`
- `PICKED_UP`
- `ARRIVED_AT_FACILITY`
- `DEPARTED_FACILITY`
- `IN_TRANSIT`
- `OUT_FOR_DELIVERY`
- `DELIVERY_ATTEMPTED`
- `DELIVERED`
- `EXCEPTION`
- `RETURNED_TO_SENDER`

**Response:** `201 Created`

---

## Error Responses

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
  "timestamp": "2024-12-12T22:50:00Z"
}
```

### 404 Not Found
```json
{
  "code": "SHIPMENT_NOT_FOUND",
  "message": "Shipment not found with id: 550e8400-e29b-41d4-a716-446655440500",
  "timestamp": "2024-12-12T22:50:00Z",
  "traceId": "abc123"
}
```

### 409 Conflict - Invalid State Transition
```json
{
  "code": "INVALID_STATE_TRANSITION",
  "message": "Cannot transition from DELIVERED to IN_TRANSIT",
  "timestamp": "2024-12-12T22:50:00Z",
  "traceId": "abc123"
}
```

### 422 Unprocessable Entity - Domain Error
```json
{
  "code": "INVARIANT_VIOLATION",
  "message": "Shipment must have at least one item",
  "timestamp": "2024-12-12T22:50:00Z",
  "traceId": "abc123"
}
```

---

## Kafka Events

### Published Events (Topic: `shipment-events`)

| Event | When Published |
|-------|----------------|
| `ShipmentCreated` | New shipment created |
| `ShipmentStatusChanged` | Status changed |
| `ShipmentPickedUp` | Shipment picked up by carrier |
| `ShipmentDelivered` | Shipment delivered |
| `ShipmentCancelled` | Shipment cancelled |
| `ShipmentReturned` | Shipment returned |
| `TrackingEventAdded` | Tracking event added |

### Consumed Events (Topic: `order-events`)

| Event | Action |
|-------|--------|
| `OrderConfirmed` | May trigger shipment creation |
| `OrderCancelled` | May cancel pending shipments |

---

## Configuration

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shipping_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
  kafka:
    bootstrap-servers: localhost:9092
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${OAUTH_ISSUER_URI}

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---

## Architecture

This microservice follows **Hexagonal Architecture** with **DDD**:

```
com.microservice.shipping
├── domain (Pure Java - No frameworks)
│   ├── models (Shipment, ShipmentItem, TrackingEvent, Carrier, etc.)
│   ├── events (Domain events)
│   ├── exceptions (Domain exceptions)
│   └── port (Inbound/Outbound ports)
├── application (Use cases, DTOs, Mappers)
└── infrastructure (Spring, JPA, Kafka adapters)
```
