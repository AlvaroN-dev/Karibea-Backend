# API Documentation - Inventory Microservice

## Overview
The Inventory Microservice manages stock levels, reservations, and warehouse operations for the Karibea e-commerce platform. It follows a hexagonal architecture pattern and communicates with other microservices via Kafka events.

## Base URL
```
/api/v1/stock
```

## Authentication
All endpoints require OAuth2 Bearer Token authentication.
- **Read Scope**: `inventory:read`
- **Write Scope**: `inventory:write`

### Authorization Header
```
Authorization: Bearer <JWT_TOKEN>
```

---

## Endpoints

### 1. Create Stock
Initialize stock for a new product/variant in a warehouse.

- **URL**: `POST /api/v1/stock`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

#### Request Body
```json
{
  "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
  "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
  "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
  "initialQuantity": 100,
  "lowStockThreshold": 10,
  "reorderPoint": 20
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| externalProductId | UUID | Yes | External product identifier from catalog service |
| externalVariantId | UUID | Yes | External variant identifier from catalog service |
| warehouseId | UUID | Yes | Warehouse where stock will be stored |
| initialQuantity | int | Yes | Initial stock quantity (>= 0) |
| lowStockThreshold | int | No | Threshold for low stock alerts |
| reorderPoint | int | No | Quantity at which reorder is suggested |

#### Response (201 Created)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174099",
  "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
  "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
  "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
  "quantityAvailable": 100,
  "quantityReserved": 0,
  "quantityIncoming": 0,
  "totalQuantity": 100,
  "lowStockThreshold": 10,
  "reorderPoint": 20,
  "isLowStock": false,
  "needsReorder": false,
  "lastRestockedAt": null,
  "createdAt": "2024-12-15T10:00:00",
  "updatedAt": "2024-12-15T10:00:00"
}
```

#### cURL Example
```bash
curl -X POST "http://localhost:8086/api/v1/stock" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
    "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
    "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
    "initialQuantity": 100,
    "lowStockThreshold": 10,
    "reorderPoint": 20
  }'
```

---

### 2. Get Stock by ID
Retrieve details of a specific stock record.

- **URL**: `GET /api/v1/stock/{stockId}`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockId | UUID | Yes | Unique stock identifier |

#### Response (200 OK)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174099",
  "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
  "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
  "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
  "quantityAvailable": 95,
  "quantityReserved": 5,
  "quantityIncoming": 0,
  "totalQuantity": 100,
  "lowStockThreshold": 10,
  "reorderPoint": 20,
  "isLowStock": false,
  "needsReorder": false,
  "lastRestockedAt": "2024-12-14T15:30:00",
  "createdAt": "2024-12-10T08:00:00",
  "updatedAt": "2024-12-15T10:00:00"
}
```

#### cURL Example
```bash
curl -X GET "http://localhost:8086/api/v1/stock/123e4567-e89b-12d3-a456-426614174099" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 3. Get Stock by Variant
Retrieve all stock records for a product variant across all warehouses.

- **URL**: `GET /api/v1/stock/variant/{variantId}`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| variantId | UUID | Yes | External variant identifier |

#### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174099",
    "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
    "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
    "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
    "quantityAvailable": 95,
    "quantityReserved": 5,
    "quantityIncoming": 0,
    "totalQuantity": 100,
    "lowStockThreshold": 10,
    "reorderPoint": 20,
    "isLowStock": false,
    "needsReorder": false,
    "lastRestockedAt": "2024-12-14T15:30:00",
    "createdAt": "2024-12-10T08:00:00",
    "updatedAt": "2024-12-15T10:00:00"
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174100",
    "externalProductId": "123e4567-e89b-12d3-a456-426614174000",
    "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
    "warehouseId": "123e4567-e89b-12d3-a456-426614174003",
    "quantityAvailable": 50,
    "quantityReserved": 0,
    "quantityIncoming": 25,
    "totalQuantity": 50,
    "lowStockThreshold": 10,
    "reorderPoint": 20,
    "isLowStock": false,
    "needsReorder": false,
    "lastRestockedAt": "2024-12-13T12:00:00",
    "createdAt": "2024-12-05T08:00:00",
    "updatedAt": "2024-12-13T12:00:00"
  }
]
```

#### cURL Example
```bash
curl -X GET "http://localhost:8086/api/v1/stock/variant/123e4567-e89b-12d3-a456-426614174001" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 4. Get Stock by Warehouse
Retrieve all stock records in a specific warehouse.

- **URL**: `GET /api/v1/stock/warehouse/{warehouseId}`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| warehouseId | UUID | Yes | Warehouse identifier |

#### Response (200 OK)
Array of StockResponse objects (same format as Get Stock by Variant).

#### cURL Example
```bash
curl -X GET "http://localhost:8086/api/v1/stock/warehouse/123e4567-e89b-12d3-a456-426614174002" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 5. Get Low Stock Items
Retrieve items below their low stock threshold in a specific warehouse.

- **URL**: `GET /api/v1/stock/warehouse/{warehouseId}/low-stock`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| warehouseId | UUID | Yes | Warehouse identifier |

#### Response (200 OK)
Array of StockResponse objects where `isLowStock = true`.

#### cURL Example
```bash
curl -X GET "http://localhost:8086/api/v1/stock/warehouse/123e4567-e89b-12d3-a456-426614174002/low-stock" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 6. Adjust Stock
Manually increase or decrease stock quantity.

- **URL**: `POST /api/v1/stock/adjust`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

#### Request Body
```json
{
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "movementType": "PURCHASE",
  "quantity": 50,
  "referenceType": "PO",
  "externalReferenceId": "123e4567-e89b-12d3-a456-426614174999",
  "note": "Received from supplier ABC"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| stockId | UUID | Yes | Stock record to adjust |
| movementType | enum | Yes | Type of movement (see below) |
| quantity | int | Yes | Quantity to adjust (>= 1) |
| referenceType | string | No | Type of reference document (e.g., "PO", "ORDER") |
| externalReferenceId | UUID | No | External reference document ID |
| note | string | No | Notes about the adjustment |

#### Movement Types
| Type | Description | Effect |
|------|-------------|--------|
| PURCHASE | Items purchased from supplier | Increases stock |
| SALE | Items sold to customer | Decreases stock |
| RETURN | Customer returns | Increases stock |
| ADJUSTMENT_IN | Manual positive adjustment | Increases stock |
| ADJUSTMENT_OUT | Manual negative adjustment | Decreases stock |
| TRANSFER_IN | Items transferred in | Increases stock |
| TRANSFER_OUT | Items transferred out | Decreases stock |
| DAMAGED | Damaged items | Decreases stock |
| EXPIRED | Expired items | Decreases stock |

#### Response (200 OK)
Returns the updated StockResponse object.

#### cURL Example
```bash
curl -X POST "http://localhost:8086/api/v1/stock/adjust" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "stockId": "123e4567-e89b-12d3-a456-426614174099",
    "movementType": "PURCHASE",
    "quantity": 50,
    "referenceType": "PO",
    "externalReferenceId": "123e4567-e89b-12d3-a456-426614174999",
    "note": "Received from supplier ABC"
  }'
```

---

### 7. Reserve Stock
Reserve stock for an order or cart.

- **URL**: `POST /api/v1/stock/reserve`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

#### Request Body
```json
{
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "quantity": 5,
  "reservationType": "ORDER",
  "externalCartId": "123e4567-e89b-12d3-a456-426614174888",
  "externalOrderId": "123e4567-e89b-12d3-a456-426614174777",
  "expiresAt": "2024-12-15T10:15:00"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| stockId | UUID | Yes | Stock record to reserve from |
| quantity | int | Yes | Quantity to reserve (>= 1) |
| reservationType | enum | Yes | Type: ORDER or CART |
| externalCartId | UUID | No | External cart identifier |
| externalOrderId | UUID | No | External order identifier |
| expiresAt | datetime | No | When the reservation expires |

#### Response (201 Created)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174555",
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "externalOrderId": "123e4567-e89b-12d3-a456-426614174777",
  "externalCartId": "123e4567-e89b-12d3-a456-426614174888",
  "quantity": 5,
  "reservationType": "ORDER",
  "status": "PENDING",
  "expiresAt": "2024-12-15T10:15:00",
  "createdAt": "2024-12-15T10:00:00",
  "updatedAt": "2024-12-15T10:00:00"
}
```

#### cURL Example
```bash
curl -X POST "http://localhost:8086/api/v1/stock/reserve" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "stockId": "123e4567-e89b-12d3-a456-426614174099",
    "quantity": 5,
    "reservationType": "ORDER",
    "externalOrderId": "123e4567-e89b-12d3-a456-426614174777",
    "expiresAt": "2024-12-15T10:15:00"
  }'
```

---

### 8. Release Reservation
Release a stock reservation, making the quantity available again.

- **URL**: `POST /api/v1/stock/reservations/{reservationId}/release`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| reservationId | UUID | Yes | Reservation identifier |

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| reason | string | No | Reason for releasing |

#### Response (204 No Content)
No response body.

#### cURL Example
```bash
curl -X POST "http://localhost:8086/api/v1/stock/reservations/123e4567-e89b-12d3-a456-426614174555/release?reason=Order%20cancelled" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 9. Confirm Reservation
Confirm a reservation after successful payment.

- **URL**: `POST /api/v1/stock/reservations/{reservationId}/confirm`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| reservationId | UUID | Yes | Reservation identifier |

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| orderId | UUID | Yes | Confirmed order identifier |

#### Response (200 OK)
No response body.

#### cURL Example
```bash
curl -X POST "http://localhost:8086/api/v1/stock/reservations/123e4567-e89b-12d3-a456-426614174555/confirm?orderId=123e4567-e89b-12d3-a456-426614174777" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## Error Responses

### 400 Bad Request - Validation Error
```json
{
  "timestamp": "2024-12-15T10:05:00",
  "status": 400,
  "error": "Validation failed",
  "code": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/v1/stock",
  "fieldErrors": [
    {
      "field": "initialQuantity",
      "message": "Initial quantity cannot be negative"
    }
  ]
}
```

### 404 Not Found - Stock Not Found
```json
{
  "timestamp": "2024-12-15T10:05:00",
  "status": 404,
  "error": "Not Found",
  "code": "STOCK_NOT_FOUND",
  "message": "Stock not found with ID: 123e4567-e89b-12d3-a456-000000000000",
  "path": "/api/v1/stock/123e4567-e89b-12d3-a456-000000000000"
}
```

### 404 Not Found - Reservation Not Found
```json
{
  "timestamp": "2024-12-15T10:05:00",
  "status": 404,
  "error": "Not Found",
  "code": "RESERVATION_NOT_FOUND",
  "message": "Reservation not found with ID: 123e4567-e89b-12d3-a456-000000000000",
  "path": "/api/v1/stock/reservations/123e4567-e89b-12d3-a456-000000000000/release"
}
```

### 422 Unprocessable Entity - Insufficient Stock
```json
{
  "timestamp": "2024-12-15T10:05:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "code": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock. Requested: 10, Available: 5",
  "path": "/api/v1/stock/reserve"
}
```

---

## Kafka Events

### Published Events (Outbound)
The inventory service publishes the following events to Kafka:

| Topic | Event Type | Description |
|-------|------------|-------------|
| `inventory.stock.created` | StockCreatedEvent | When new stock is initialized |
| `inventory.stock.adjusted` | StockAdjustedEvent | When stock quantity changes |
| `inventory.stock.reserved` | StockReservedEvent | When stock is reserved |
| `inventory.stock.released` | StockReleasedEvent | When reservation is released |
| `inventory.low-stock.alert` | LowStockAlertEvent | When stock falls below threshold |

#### Event Payload Example - StockReservedEvent
```json
{
  "eventId": "123e4567-e89b-12d3-a456-426614174000",
  "eventType": "stock.reserved",
  "occurredAt": "2024-12-15T10:00:00",
  "aggregateId": "123e4567-e89b-12d3-a456-426614174099",
  "reservationId": "123e4567-e89b-12d3-a456-426614174555",
  "externalVariantId": "123e4567-e89b-12d3-a456-426614174001",
  "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
  "quantity": 5,
  "reservationType": "ORDER",
  "externalCartId": "123e4567-e89b-12d3-a456-426614174888",
  "externalOrderId": "123e4567-e89b-12d3-a456-426614174777"
}
```

### Consumed Events (Inbound)
The inventory service consumes the following events:

| Topic | Event Type | Action |
|-------|------------|--------|
| `order.confirmed` | OrderConfirmedEvent | Confirms reservation |
| `order.cancelled` | OrderCancelledEvent | Releases reservation |
| `cart.expired` | CartExpiredEvent | Releases reservation |

---

## Database Schema

### Tables

#### stock
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| external_product_id | UUID | Product reference (catalog) |
| external_variant_id | UUID | Variant reference (catalog) |
| warehouse_id | UUID | Warehouse reference |
| quantity_available | int | Available quantity |
| quantity_reserved | int | Reserved quantity |
| quantity_incoming | int | Incoming quantity |
| low_stock_threshold | int | Low stock alert threshold |
| reorder_point | int | Reorder suggestion point |
| last_restocked_at | timestamp | Last restock time |
| created_at | timestamp | Creation time |
| updated_at | timestamp | Last update time |

#### stock_reservations
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| stock_id | UUID | FK to stock |
| external_order_id | UUID | Order reference |
| external_cart_id | UUID | Cart reference |
| quantity | int | Reserved quantity |
| reservation_type_id | varchar | ORDER/CART |
| id_status_reservations | varchar | PENDING/CONFIRMED/CANCELLED/EXPIRED |
| expires_at | timestamp | Expiration time |
| created_at | timestamp | Creation time |
| updated_at | timestamp | Last update time |

#### stock_movements
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| stock_id | UUID | FK to stock |
| movement_type_id | UUID | FK to movement_type |
| quantity | int | Movement quantity |
| reference_type | varchar | Reference document type |
| external_reference_id | UUID | External reference |
| note | text | Movement notes |
| external_performed_by_id | UUID | User who performed |
| created_at | timestamp | Creation time |

#### warehouses
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| external_store_id | UUID | Store reference |
| name | varchar(50) | Warehouse name |
| code | varchar(20) | Unique code |
| address | text | Address |
| city | varchar(100) | City |
| country | varchar(100) | Country |
| is_active | boolean | Active status |
| created_at | timestamp | Creation time |
| updated_at | timestamp | Last update time |

---

## OpenAPI / Swagger
Access the interactive API documentation at:
- Swagger UI: `http://localhost:8086/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8086/v3/api-docs`
