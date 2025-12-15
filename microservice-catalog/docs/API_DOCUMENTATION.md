# Catalog Microservice - API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## Table of Contents

1. [Products](#products)
   - [Create Product](#create-product)
   - [Get Product by ID](#get-product-by-id)
   - [List Products](#list-products)
   - [Update Product](#update-product)
   - [Publish Product](#publish-product)
   - [Deactivate Product](#deactivate-product)
   - [Add Variant](#add-variant)
2. [Error Responses](#error-responses)

---

## Products

### Create Product

Creates a new product in draft status.

**Endpoint:** `POST /api/v1/products`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "storeId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design. Made from 100% organic cotton.",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD"
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| storeId | UUID | Yes | External store identifier |
| sku | String | Yes | Unique product SKU (3-100 chars) |
| name | String | Yes | Product name (max 255 chars) |
| description | String | No | Product description |
| brand | String | No | Brand name (max 100 chars) |
| basePrice | Decimal | Yes | Base price (must be positive) |
| compareAtPrice | Decimal | No | Original price before discount |
| currency | String | Yes | ISO 4217 currency code (3 chars) |

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design. Made from 100% organic cotton.",
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

**Error Responses:**
- `400 Bad Request` - Invalid request data
- `409 Conflict` - Product with same SKU already exists in store

---

### Get Product by ID

Retrieves detailed product information by ID.

**Endpoint:** `GET /api/v1/products/{id}`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product identifier |

**Example Request:**
```
GET /api/v1/products/550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design. Made from 100% organic cotton.",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "PUBLISHED",
  "featured": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T14:45:00Z",
  "variants": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-S-BLK",
      "name": "Small / Black",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "barcode": "1234567890123",
      "active": true,
      "createdAt": "2024-01-15T11:00:00Z",
      "updatedAt": "2024-01-15T11:00:00Z",
      "images": []
    },
    {
      "id": "660e8400-e29b-41d4-a716-446655440002",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-M-BLK",
      "name": "Medium / Black",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "barcode": "1234567890124",
      "active": true,
      "createdAt": "2024-01-15T11:05:00Z",
      "updatedAt": "2024-01-15T11:05:00Z",
      "images": []
    }
  ],
  "images": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440001",
      "url": "https://cdn.example.com/products/tshirt-front.jpg",
      "displayOrder": 1,
      "primary": true,
      "createdAt": "2024-01-15T10:35:00Z"
    },
    {
      "id": "770e8400-e29b-41d4-a716-446655440002",
      "url": "https://cdn.example.com/products/tshirt-back.jpg",
      "displayOrder": 2,
      "primary": false,
      "createdAt": "2024-01-15T10:36:00Z"
    }
  ]
}
```

**Error Responses:**
- `404 Not Found` - Product not found

---

### List Products

Retrieves a paginated list of products.

**Endpoint:** `GET /api/v1/products`

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| storeId | UUID | No | - | Filter by store |
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 10 | Page size |

**Example Request:**
```
GET /api/v1/products?storeId=123e4567-e89b-12d3-a456-426614174000&page=0&size=10
```

**Response (200 OK):**
```json
{
  "products": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
      "sku": "PROD-001",
      "name": "Premium Cotton T-Shirt",
      "description": "High-quality cotton t-shirt with modern design.",
      "brand": "Nike",
      "basePrice": 29.99,
      "compareAtPrice": 39.99,
      "currency": "USD",
      "status": "PUBLISHED",
      "featured": true,
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T14:45:00Z",
      "variants": [],
      "images": []
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440099",
      "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
      "sku": "PROD-002",
      "name": "Slim Fit Jeans",
      "description": "Classic slim fit jeans in dark wash.",
      "brand": "Levi's",
      "basePrice": 59.99,
      "compareAtPrice": 79.99,
      "currency": "USD",
      "status": "DRAFT",
      "featured": false,
      "createdAt": "2024-01-16T09:00:00Z",
      "updatedAt": "2024-01-16T09:00:00Z",
      "variants": [],
      "images": []
    }
  ],
  "totalElements": 25,
  "totalPages": 3,
  "currentPage": 0,
  "pageSize": 10
}
```

---

### Update Product

Updates an existing product's information.

**Endpoint:** `PUT /api/v1/products/{id}`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product identifier |

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Premium Cotton T-Shirt V2",
  "description": "Updated premium quality cotton t-shirt with enhanced comfort.",
  "brand": "Nike",
  "basePrice": 34.99,
  "compareAtPrice": 44.99,
  "currency": "USD",
  "featured": true
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | String | No | Product name |
| description | String | No | Product description |
| brand | String | No | Brand name |
| basePrice | Decimal | No | Base price (must be positive) |
| compareAtPrice | Decimal | No | Compare at price |
| currency | String | No | Currency code |
| featured | Boolean | No | Featured flag |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt V2",
  "description": "Updated premium quality cotton t-shirt with enhanced comfort.",
  "brand": "Nike",
  "basePrice": 34.99,
  "compareAtPrice": 44.99,
  "currency": "USD",
  "status": "PUBLISHED",
  "featured": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-17T16:20:00Z",
  "variants": [],
  "images": []
}
```

**Error Responses:**
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Product not found

---

### Publish Product

Publishes a product, making it visible to customers.

**Endpoint:** `POST /api/v1/products/{id}/publish`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product identifier |

**Example Request:**
```
POST /api/v1/products/550e8400-e29b-41d4-a716-446655440000/publish
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design.",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "PUBLISHED",
  "featured": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T15:00:00Z",
  "variants": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-S-BLK",
      "name": "Small / Black",
      "price": 29.99,
      "active": true
    }
  ],
  "images": []
}
```

**Business Rules:**
- Product must have at least one active variant to be published
- Cannot publish an inactive product (must reactivate first)

**Error Responses:**
- `400 Bad Request` - Cannot publish product without active variants
- `404 Not Found` - Product not found

---

### Deactivate Product

Deactivates a product, removing it from the storefront.

**Endpoint:** `POST /api/v1/products/{id}/deactivate`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product identifier |

**Example Request:**
```
POST /api/v1/products/550e8400-e29b-41d4-a716-446655440000/deactivate
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design.",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "INACTIVE",
  "featured": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-18T09:15:00Z",
  "variants": [],
  "images": []
}
```

**Error Responses:**
- `404 Not Found` - Product not found

---

### Add Variant

Adds a new variant to an existing product.

**Endpoint:** `POST /api/v1/products/{id}/variants`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Product identifier |

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "sku": "PROD-001-L-RED",
  "name": "Large / Red",
  "price": 29.99,
  "compareAtPrice": 39.99,
  "barcode": "1234567890125"
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| sku | String | Yes | Unique variant SKU (3-100 chars) |
| name | String | Yes | Variant name (max 255 chars) |
| price | Decimal | Yes | Variant price (must be positive) |
| compareAtPrice | Decimal | No | Compare at price |
| barcode | String | No | Barcode (max 100 chars) |

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalStoreId": "123e4567-e89b-12d3-a456-426614174000",
  "sku": "PROD-001",
  "name": "Premium Cotton T-Shirt",
  "description": "High-quality cotton t-shirt with modern design.",
  "brand": "Nike",
  "basePrice": 29.99,
  "compareAtPrice": 39.99,
  "currency": "USD",
  "status": "DRAFT",
  "featured": false,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T11:30:00Z",
  "variants": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440003",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "sku": "PROD-001-L-RED",
      "name": "Large / Red",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "barcode": "1234567890125",
      "active": true,
      "createdAt": "2024-01-15T11:30:00Z",
      "updatedAt": "2024-01-15T11:30:00Z",
      "images": []
    }
  ],
  "images": []
}
```

**Business Rules:**
- Variant SKU must be unique within the product

**Error Responses:**
- `400 Bad Request` - Invalid variant data or duplicate SKU within product
- `404 Not Found` - Product not found

---

## Error Responses

All error responses follow a consistent format.

### Standard Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "code": "ERROR_CODE",
  "message": "Descriptive error message",
  "path": "/api/v1/products",
  "traceId": "abc123def456"
}
```

### Validation Error Response

When request validation fails, the response includes field-level details:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/v1/products",
  "traceId": "abc123def456",
  "fieldErrors": [
    {
      "field": "name",
      "rejectedValue": "",
      "message": "Name is required"
    },
    {
      "field": "basePrice",
      "rejectedValue": -10,
      "message": "Base price must be positive"
    }
  ]
}
```

### Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| VALIDATION_ERROR | 400 | Request validation failed |
| INVALID_ARGUMENT | 400 | Invalid argument provided |
| INVALID_PRODUCT_STATE | 400 | Invalid product state transition |
| PRODUCT_NOT_FOUND | 404 | Product not found |
| VARIANT_NOT_FOUND | 404 | Variant not found |
| ATTRIBUTE_NOT_FOUND | 404 | Attribute not found |
| RESOURCE_NOT_FOUND | 404 | Generic resource not found |
| DUPLICATE_SKU | 409 | SKU already exists |
| INTERNAL_ERROR | 500 | Unexpected server error |

---

## Product Status Values

| Status | Description |
|--------|-------------|
| DRAFT | Product is being created/edited, not visible to customers |
| PUBLISHED | Product is active and visible in the storefront |
| INACTIVE | Product is temporarily disabled but can be reactivated |
| ARCHIVED | Product is permanently archived for historical purposes |

---

## Example Workflows

### Create and Publish a Product

1. **Create the product:**
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "storeId": "123e4567-e89b-12d3-a456-426614174000",
    "sku": "PROD-001",
    "name": "Premium Cotton T-Shirt",
    "basePrice": 29.99,
    "currency": "USD"
  }'
```

2. **Add variants:**
```bash
curl -X POST http://localhost:8080/api/v1/products/{productId}/variants \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD-001-M-BLK",
    "name": "Medium / Black",
    "price": 29.99
  }'
```

3. **Publish the product:**
```bash
curl -X POST http://localhost:8080/api/v1/products/{productId}/publish
```

---

## Notes

- All UUIDs are in standard format: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`
- Timestamps are in ISO 8601 format with UTC timezone
- Currency codes follow ISO 4217 standard
- Prices use decimal precision (2 decimal places recommended)
