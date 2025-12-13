# Order Microservice API

Order management microservice for the Karibea ecosystem, built with **Spring Boot 3.5**, **Hexagonal Architecture**, and **DDD**.

## Base URL

```
http://localhost:8080/api/v1/orders
```

## Authentication

All endpoints require JWT Bearer token authentication via OAuth2 Resource Server.

```
Authorization: Bearer <jwt_token>
```

---

## Endpoints

### 1. Create Order

**POST** `/api/v1/orders`

Creates a new order with the specified items.

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
  "orderNumber": "ORD-20241212-A1B2C3D4",
  "customerId": "550e8400-e29b-41d4-a716-446655440001",
  "storeId": "550e8400-e29b-41d4-a716-446655440002",
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
      "productId": "550e8400-e29b-41d4-a716-446655440010",
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
  "createdAt": "2024-12-12T21:50:00",
  "updatedAt": "2024-12-12T21:50:00"
}
```

---

### 2. Get Order by ID

**GET** `/api/v1/orders/{orderId}`

**Path Parameters:**
- `orderId` (UUID) - Order ID

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440100",
  "orderNumber": "ORD-20241212-A1B2C3D4",
  "status": "PENDING",
  ...
}
```

---

### 3. Get Order by Order Number

**GET** `/api/v1/orders/number/{orderNumber}`

**Path Parameters:**
- `orderNumber` (String) - Order number (e.g., ORD-20241212-A1B2C3D4)

**Response:** `200 OK`

---

### 4. Get Orders by Customer

**GET** `/api/v1/orders/customer/{customerId}?page=0&size=10&sort=createdAt,desc`

**Path Parameters:**
- `customerId` (UUID) - Customer ID

**Query Parameters:**
- `page` (int, default: 0) - Page number
- `size` (int, default: 10) - Page size
- `sort` (string) - Sort field and direction

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440100",
      "orderNumber": "ORD-20241212-A1B2C3D4",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3
}
```

---

### 5. Get Orders by Store

**GET** `/api/v1/orders/store/{storeId}?page=0&size=10`

**Path Parameters:**
- `storeId` (UUID) - Store ID

**Response:** `200 OK` (Paginated)

---

### 6. Cancel Order

**POST** `/api/v1/orders/{orderId}/cancel`

Cancels an order if it's in a cancellable state (PENDING, CONFIRMED, PROCESSING).

**Path Parameters:**
- `orderId` (UUID) - Order ID

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440100",
  "reason": "Customer requested cancellation"
}
```

**Response:** `204 No Content`

---

### 7. Confirm Order

**POST** `/api/v1/orders/{orderId}/confirm?paymentId={paymentId}`

Confirms an order after payment (typically called by payment service).

**Path Parameters:**
- `orderId` (UUID) - Order ID

**Query Parameters:**
- `paymentId` (UUID) - Payment ID

**Response:** `204 No Content`

---

### 8. Change Order Status

**PATCH** `/api/v1/orders/{orderId}/status`

Changes order status manually (admin use).

**Path Parameters:**
- `orderId` (UUID) - Order ID

**Request Body:**
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440100",
  "newStatus": "SHIPPED",
  "reason": "Package dispatched",
  "relatedEntityId": "550e8400-e29b-41d4-a716-446655440300"
}
```

**Valid Status Values:**
- `PENDING` → `CONFIRMED`, `CANCELLED`
- `CONFIRMED` → `PROCESSING`, `CANCELLED`
- `PROCESSING` → `SHIPPED`, `CANCELLED`
- `SHIPPED` → `DELIVERED`, `RETURNED`
- `DELIVERED` → `RETURNED`, `COMPLETED`
- `RETURNED` → `REFUNDED`

**Response:** `204 No Content`

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
  "timestamp": "2024-12-12T21:50:00Z"
}
```

### 404 Not Found
```json
{
  "code": "ORDER_NOT_FOUND",
  "message": "Order not found with id: 550e8400-e29b-41d4-a716-446655440100",
  "timestamp": "2024-12-12T21:50:00Z",
  "traceId": "abc123"
}
```

### 409 Conflict - Invalid State Transition
```json
{
  "code": "INVALID_STATE_TRANSITION",
  "message": "Cannot transition from DELIVERED to PROCESSING",
  "timestamp": "2024-12-12T21:50:00Z",
  "traceId": "abc123"
}
```

### 422 Unprocessable Entity - Domain Error
```json
{
  "code": "INVARIANT_VIOLATION",
  "message": "Order must have at least one item",
  "timestamp": "2024-12-12T21:50:00Z",
  "traceId": "abc123"
}
```

---

## Kafka Events

The service publishes the following events to `order-events` topic:

| Event | When Published |
|-------|----------------|
| `OrderCreated` | New order created |
| `OrderConfirmed` | Order confirmed after payment |
| `OrderStatusChanged` | Status changed |
| `OrderCancelled` | Order cancelled |
| `OrderShipped` | Order shipped |
| `OrderDelivered` | Order delivered |
| `OrderCouponApplied` | Coupon applied |

---

## Configuration

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/order_db
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
