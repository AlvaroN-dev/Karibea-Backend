# Catalog Microservice - API Documentation

## Base URL

```
http://localhost:8082/api/v1
```

## Authentication

Protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

---

## Products API

### Create Product

Creates a new product in the catalog.

**Endpoint:** `POST /products`

**Authentication:** Required

**Request Body:**
```json
{
  "storeId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "DRAFT",
  "featured": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z",
  "variants": [],
  "images": []
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "storeId": "123e4567-e89b-12d3-a456-426614174000",
    "sku": "PROD-001",
    "name": "Premium Cotton T-Shirt",
    "description": "High-quality cotton t-shirt",
    "brand": "Nike",
    "basePrice": 29.99,
    "compareAtPrice": 39.99,
    "currency": "USD"
  }'
```

---

### Get Product by ID

Retrieves a product by its unique identifier.

**Endpoint:** `GET /products/{id}`

**Authentication:** Not required

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product unique identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "PUBLISHED",
  "featured": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T12:45:00Z",
  "variants": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-M-RED",
      "name": "Medium / Red",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "barcode": "1234567890123",
      "active": true,
      "createdAt": "2024-01-15T11:00:00Z",
      "updatedAt": "2024-01-15T11:00:00Z",
      "images": []
    }
  ],
  "images": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440002",
      "url": "https://cdn.example.com/images/product.jpg",
      "displayOrder": 1,
      "primary": true,
      "createdAt": "2024-01-15T10:35:00Z"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8082/api/v1/products/550e8400-e29b-41d4-a716-446655440000"
```

---

### List Products

Retrieves a paginated list of products.

**Endpoint:** `GET /products`

**Authentication:** Not required

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| storeId | UUID | No | - | Filter by store ID |
| page | int | No | 0 | Page number (0-indexed) |
| size | int | No | 10 | Page size |

**Response (200 OK):**
```json
{
  "products": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
      "sku": "PROD-001",
      "name": "Premium Cotton T-Shirt",
      "description": "High-quality cotton t-shirt",
      "brand": "Nike",
      "basePrice": 29.99,
      "compareAtPrice": 39.99,
      "currency": "USD",
      "status": "PUBLISHED",
      "featured": true,
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T12:45:00Z",
      "variants": [],
      "images": []
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "pageSize": 10
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8082/api/v1/products?storeId=123e4567-e89b-12d3-a456-426614174000&page=0&size=10"
```

---

### Update Product

Updates an existing product.

**Endpoint:** `PUT /products/{id}`

**Authentication:** Required

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product unique identifier |

**Request Body:**
```json
{
  "name": "Premium Cotton T-Shirt V2",
  "description": "Updated high-quality cotton t-shirt",
  "brand": "Adidas",
  "basePrice": 34.99,
  "compareAtPrice": 44.99,
  "currency": "USD",
  "featured": true
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt V2",
  "description": "Updated high-quality cotton t-shirt",
  "brand": "Adidas",
  "basePrice": 34.99,
  "compareAtPrice": 44.99,
  "currency": "USD",
  "status": "DRAFT",
  "featured": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T14:00:00Z",
  "variants": [],
  "images": []
}
```

**cURL Example:**
```bash
curl -X PUT "http://localhost:8082/api/v1/products/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Premium Cotton T-Shirt V2",
    "description": "Updated high-quality cotton t-shirt",
    "brand": "Adidas",
    "basePrice": 34.99,
    "compareAtPrice": 44.99,
    "currency": "USD",
    "featured": true
  }'
```

---

### Publish Product

Publishes a product, making it visible to customers.

**Endpoint:** `POST /products/{id}/publish`

**Authentication:** Required

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product unique identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PUBLISHED",
  ...
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/products/550e8400-e29b-41d4-a716-446655440000/publish" \
  -H "Authorization: Bearer <token>"
```

---

### Deactivate Product

Deactivates a product, removing it from the storefront.

**Endpoint:** `POST /products/{id}/deactivate`

**Authentication:** Required

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product unique identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "INACTIVE",
  ...
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/products/550e8400-e29b-41d4-a716-446655440000/deactivate" \
  -H "Authorization: Bearer <token>"
```

---

### Add Variant

Adds a new variant to an existing product.

**Endpoint:** `POST /products/{id}/variants`

**Authentication:** Required

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product unique identifier |

**Request Body:**
```json
{
  "sku": "PROD-001-M-RED",
  "name": "Medium / Red",
  "price": 29.99,
  "compareAtPrice": 39.99,
  "barcode": "1234567890123"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "variants": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-M-RED",
      "name": "Medium / Red",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "barcode": "1234567890123",
      "active": true,
      "createdAt": "2024-01-15T15:00:00Z",
      "updatedAt": "2024-01-15T15:00:00Z",
      "images": []
    }
  ],
  ...
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/products/550e8400-e29b-41d4-a716-446655440000/variants" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "sku": "PROD-001-M-RED",
    "name": "Medium / Red",
    "price": 29.99,
    "compareAtPrice": 39.99,
    "barcode": "1234567890123"
  }'
```

---

## Kafka Events

### Published Events (Topic: products)

#### ProductCreatedEvent
```json
{
  "eventType": "product.created",
  "aggregateId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "sku": "PROD-001",
    "name": "Premium Cotton T-Shirt",
    "storeId": "123e4567-e89b-12d3-a456-426614174000"
  }
}
```

#### ProductPublishedEvent
```json
{
  "eventType": "product.published",
  "aggregateId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T12:00:00Z",
  "data": {
    "previousStatus": "DRAFT",
    "newStatus": "PUBLISHED"
  }
}
```

#### VariantAddedEvent
```json
{
  "eventType": "variant.added",
  "aggregateId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T15:00:00Z",
  "data": {
    "variantId": "660e8400-e29b-41d4-a716-446655440001",
    "sku": "PROD-001-M-RED",
    "name": "Medium / Red",
    "price": 29.99
  }
}
```

### Consumed Events

#### inventory.updated
```json
{
  "eventType": "inventory.updated",
  "variantId": "660e8400-e29b-41d4-a716-446655440001",
  "sku": "PROD-001-M-RED",
  "quantity": 100,
  "available": true,
  "timestamp": "2024-01-15T16:00:00Z"
}
```

#### inventory.low-stock
```json
{
  "eventType": "inventory.low-stock",
  "variantId": "660e8400-e29b-41d4-a716-446655440001",
  "sku": "PROD-001-M-RED",
  "currentQuantity": 5,
  "threshold": 10,
  "timestamp": "2024-01-15T17:00:00Z"
}
```

#### inventory.out-of-stock
```json
{
  "eventType": "inventory.out-of-stock",
  "variantId": "660e8400-e29b-41d4-a716-446655440001",
  "sku": "PROD-001-M-RED",
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T18:00:00Z"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed: SKU is required",
  "path": "/api/v1/products"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/v1/products"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/products/550e8400-e29b-41d4-a716-446655440000"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Product with SKU PROD-001 already exists",
  "path": "/api/v1/products"
}
```
