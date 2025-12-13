# API Documentation - Inventory Microservice

## Base URL
`/api/v1/stock`

## Authentication
All endpoints require OAuth2 Bearer Token.
- **Read Scope**: `inventory:read`
- **Write Scope**: `inventory:write`

---

## 1. Create Stock
Initialize stock for a new product/variant in a warehouse.

- **URL**: `POST /api/v1/stock`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

### Request Body
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

### Response (201 Created)
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
  "createdAt": "2023-10-27T10:00:00",
  "updatedAt": "2023-10-27T10:00:00"
}
```

---

## 2. Adjust Stock
Increase or decrease stock quantity manually (e.g., received shipment, damage, manual correction).

- **URL**: `POST /api/v1/stock/adjust`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

### Request Body
```json
{
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "movementType": "PURCHASE", 
  "quantity": 50,
  "referenceType": "PO",
  "externalReferenceId": "123e4567-e89b-12d3-a456-426614174999",
  "note": "Received from supplier"
}
```
*Valid Movement Types*: `PURCHASE`, `SALE`, `RETURN`, `ADJUSTMENT_IN`, `ADJUSTMENT_OUT`, `TRANSFER_IN`, `TRANSFER_OUT`, `DAMAGED`, `EXPIRED`.

### Response (200 OK)
Returns the updated Stock object (same format as Create Stock).

---

## 3. Reserve Stock
Reserve items for a cart or order. Typically called by Order/Cart service.

- **URL**: `POST /api/v1/stock/reserve`
- **Method**: `POST`
- **Auth**: `SCOPE_inventory:write`

### Request Body
```json
{
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "quantity": 5,
  "reservationType": "ORDER",
  "externalCartId": "123e4567-e89b-12d3-a456-426614174888",
  "externalOrderId": "123e4567-e89b-12d3-a456-426614174777",
  "expiresAt": "2023-10-27T10:15:00"
}
```

### Response (201 Created)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174555",
  "stockId": "123e4567-e89b-12d3-a456-426614174099",
  "externalOrderId": "123e4567-e89b-12d3-a456-426614174777",
  "externalCartId": "123e4567-e89b-12d3-a456-426614174888",
  "quantity": 5,
  "reservationType": "ORDER",
  "status": "PENDING",
  "expiresAt": "2023-10-27T10:15:00",
  "createdAt": "2023-10-27T10:00:00",
  "updatedAt": "2023-10-27T10:00:00"
}
```

---

## 4. Get Stock by Variant
Check availability for a specific product variant across all warehouses.

- **URL**: `GET /api/v1/stock/variant/{variantId}`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

### Response (200 OK)
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174099",
    "warehouseId": "123e4567-e89b-12d3-a456-426614174002",
    "quantityAvailable": 145,
    "quantityReserved": 5
    // ... other fields
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174100",
    "warehouseId": "123e4567-e89b-12d3-a456-426614174003",
    "quantityAvailable": 0,
    "quantityReserved": 0
    // ... other fields
  }
]
```

---

## 5. Get Low Stock Items
Retrieve items that are below their low stock threshold for a specific warehouse.

- **URL**: `GET /api/v1/stock/warehouse/{warehouseId}/low-stock`
- **Method**: `GET`
- **Auth**: `SCOPE_inventory:read`

### Response (200 OK)
List of Stock objects.

---

## Error Responses

### 422 Unprocessable Entity - Insufficient Stock
```json
{
  "timestamp": "2023-10-27T10:05:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "code": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock. Requested: 10, Available: 5",
  "path": "/api/v1/stock/reserve"
}
```

### 404 Not Found - Stock Not Found
```json
{
  "timestamp": "2023-10-27T10:05:00",
  "status": 404,
  "error": "Not Found",
  "code": "STOCK_NOT_FOUND",
  "message": "Stock not found with ID: 123e4567-e89b-12d3-a456-000000000000",
  "path": "/api/v1/stock/adjust"
}
```

### 400 Bad Request - Validation Error
```json
{
  "timestamp": "2023-10-27T10:05:00",
  "status": 400,
  "error": "Validation failed",
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
