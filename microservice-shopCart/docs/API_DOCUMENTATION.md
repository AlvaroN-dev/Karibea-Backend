# Shopping Cart Microservice API Documentation

## Overview

The Shopping Cart Microservice provides a RESTful API for managing shopping carts, cart items, and applied coupons. This service is built following **Domain-Driven Design (DDD)** and **Hexagonal Architecture** principles.

**Base URL:** `http://localhost:8085/api/v1`

**Swagger UI:** `http://localhost:8085/swagger-ui.html`

---

## Authentication

All endpoints (except health and documentation) require OAuth2 JWT Bearer token authentication.

```
Authorization: Bearer <jwt_token>
```

---

## Endpoints

### Cart Operations

#### 1. Create Shopping Cart

Creates a new shopping cart for an authenticated user or guest session.

**POST** `/carts`

**Request Body:**
```json
{
  "userProfileId": "550e8400-e29b-41d4-a716-446655440010",
  "sessionId": "sess_abc123xyz789",
  "currency": "USD"
}
```

**Response (201 Created):**
```json
{
  "cartId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Shopping cart created successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for 1 field(s)",
  "errors": [
    {
      "field": "currency",
      "message": "Currency must be a 3-letter ISO code"
    }
  ],
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts"
}
```

---

#### 2. Get Cart by ID

Retrieves the shopping cart with enriched product and user information.

**GET** `/carts/{cartId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| cartId | UUID | Yes | Cart identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "Active",
  "statusDescription": "Cart is active and can receive items",
  "currency": "USD",
  "subtotal": 149.97,
  "totalDiscount": 15.00,
  "total": 134.97,
  "itemCount": 3,
  "notes": "Please gift wrap the items",
  "userProfile": {
    "id": "550e8400-e29b-41d4-a716-446655440010",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  },
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "product": {
        "productId": "550e8400-e29b-41d4-a716-446655440020",
        "variantId": "550e8400-e29b-41d4-a716-446655440021",
        "name": "Wireless Bluetooth Headphones",
        "variantName": "Black - Large",
        "sku": "WBH-BLK-L-001",
        "imageUrl": "https://cdn.example.com/products/headphones.jpg"
      },
      "store": {
        "storeId": "550e8400-e29b-41d4-a716-446655440030",
        "name": "TechStore Pro",
        "logoUrl": "https://cdn.example.com/stores/techstore-logo.png"
      },
      "quantity": 2,
      "unitPrice": 49.99,
      "lineTotal": 99.98,
      "addedAt": "2024-12-13T10:30:00Z",
      "updatedAt": "2024-12-13T11:00:00Z"
    }
  ],
  "coupons": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440050",
      "code": "SUMMER2024",
      "discountType": "PERCENTAGE",
      "discountAmount": 15.00,
      "appliedAt": "2024-12-13T11:30:00Z"
    }
  ],
  "createdAt": "2024-12-13T10:30:00Z",
  "updatedAt": "2024-12-13T11:45:00Z",
  "expiresAt": "2024-12-20T10:30:00Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "code": "CART_NOT_FOUND",
  "message": "Shopping cart with ID '550e8400-e29b-41d4-a716-446655440000' not found",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000"
}
```

---

#### 3. Get Cart by User Profile

**GET** `/carts/user/{userProfileId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| userProfileId | UUID | Yes | User profile ID |

**Response:** Same as Get Cart by ID

---

#### 4. Get Cart by Session

**GET** `/carts/session/{sessionId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| sessionId | String | Yes | Session identifier |

**Response:** Same as Get Cart by ID

---

#### 5. Clear Cart

Removes all items from the cart.

**DELETE** `/carts/{cartId}/clear`

**Response (200 OK):**
```json
{
  "message": "Cart cleared successfully",
  "success": true
}
```

---

#### 6. Delete Cart (Soft Delete)

Performs a soft delete on the shopping cart. The cart is marked as deleted but remains in the database for audit purposes.

**DELETE** `/carts/{cartId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| cartId | UUID | Yes | Cart identifier |

**Response (200 OK):**
```json
{
  "message": "Cart deleted successfully",
  "success": true
}
```

**Error Response (404 Not Found):**
```json
{
  "code": "CART_NOT_FOUND",
  "message": "Shopping cart with ID '550e8400-e29b-41d4-a716-446655440000' not found",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000"
}
```

> **Note:** Soft deleted carts:
> - Are marked with `isDeleted = true` and `deletedAt` timestamp
> - Are excluded from all active cart queries
> - Remain in the database for audit and recovery purposes
> - Generate a `CartDeleted` domain event

---

### Item Operations

#### 7. Add Item to Cart

Adds a product to the cart. If the same product/variant exists, quantities are merged.

**POST** `/carts/{cartId}/items`

**Request Body:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440020",
  "variantId": "550e8400-e29b-41d4-a716-446655440021",
  "storeId": "550e8400-e29b-41d4-a716-446655440030",
  "quantity": 2
}
```

**Response (200 OK):**
Returns the updated cart (same format as Get Cart by ID).

**Error Responses:**

*Invalid Quantity (400):*
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for 1 field(s)",
  "errors": [
    {
      "field": "quantity",
      "message": "Quantity must be at least 1"
    }
  ],
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/items"
}
```

*External Service Unavailable (503):*
```json
{
  "code": "EXTERNAL_SERVICE_ERROR",
  "message": "External service temporarily unavailable. Please try again later.",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/items"
}
```

---

#### 8. Update Item Quantity

**PATCH** `/carts/{cartId}/items/{itemId}`

**Request Body:**
```json
{
  "quantity": 5
}
```

**Response (200 OK):**
Returns the updated cart.

**Error Response (404 Not Found):**
```json
{
  "code": "ITEM_NOT_FOUND",
  "message": "Item with ID '550e8400-e29b-41d4-a716-446655440001' not found in cart",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/items/550e8400-e29b-41d4-a716-446655440001"
}
```

---

#### 9. Remove Item from Cart

**DELETE** `/carts/{cartId}/items/{itemId}`

**Response (200 OK):**
Returns the updated cart.

---

### Coupon Operations

#### 10. Apply Coupon

Validates and applies a coupon to the cart.

**POST** `/carts/{cartId}/coupons`

**Request Body:**
```json
{
  "couponCode": "SUMMER2024"
}
```

**Response (200 OK):**
Returns the updated cart with coupon applied.

**Error Responses:**

*Coupon Already Applied (409):*
```json
{
  "code": "COUPON_ALREADY_APPLIED",
  "message": "Coupon 'SUMMER2024' has already been applied to this cart",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/coupons"
}
```

*Invalid Coupon (400):*
```json
{
  "code": "INVALID_CART_OPERATION",
  "message": "Coupon code is not valid or has expired",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/coupons"
}
```

---

#### 11. Remove Coupon

**DELETE** `/carts/{cartId}/coupons/{couponId}`

**Response (200 OK):**
Returns the updated cart.

**Error Response (404 Not Found):**
```json
{
  "code": "COUPON_NOT_FOUND",
  "message": "Coupon with ID '550e8400-e29b-41d4-a716-446655440050' not found in cart",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/carts/550e8400-e29b-41d4-a716-446655440000/coupons/550e8400-e29b-41d4-a716-446655440050"
}
```

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `CART_NOT_FOUND` | 404 | Cart with specified ID does not exist or was deleted |
| `ITEM_NOT_FOUND` | 404 | Item with specified ID not found in cart |
| `COUPON_NOT_FOUND` | 404 | Coupon with specified ID not found in cart |
| `CART_EXPIRED` | 410 | Cart has expired due to inactivity |
| `COUPON_ALREADY_APPLIED` | 409 | Coupon is already applied to the cart |
| `INVALID_CART_OPERATION` | 400 | Operation not allowed on cart (e.g., cart is not active) |
| `INVALID_QUANTITY` | 400 | Quantity is outside valid range (1-999) |
| `VALIDATION_ERROR` | 400 | Request body validation failed |
| `EXTERNAL_SERVICE_ERROR` | 503 | External microservice is unavailable |
| `INTERNAL_ERROR` | 500 | Unexpected server error |

---

## Domain Events (Kafka)

The microservice publishes the following domain events to Kafka:

| Event | Topic | Description |
|-------|-------|-------------|
| `CartCreated` | `shopping-cart.cart-created` | New cart created |
| `CartDeleted` | `shopping-cart.cart-deleted` | Cart soft deleted |
| `ItemAddedToCart` | `shopping-cart.item-added-to-cart` | Item added |
| `ItemRemovedFromCart` | `shopping-cart.item-removed-from-cart` | Item removed |
| `ItemQuantityUpdated` | `shopping-cart.item-quantity-updated` | Item quantity changed |
| `CouponAppliedToCart` | `shopping-cart.coupon-applied-to-cart` | Coupon applied |
| `CartExpired` | `shopping-cart.cart-expired` | Cart expired |

**Event Envelope Format:**
```json
{
  "eventId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "eventType": "ItemAddedToCart",
  "aggregateId": "550e8400-e29b-41d4-a716-446655440000",
  "occurredAt": "2024-12-13T10:30:00Z",
  "payload": {
    "cartId": "550e8400-e29b-41d4-a716-446655440000",
    "itemId": "550e8400-e29b-41d4-a716-446655440001",
    "productId": "550e8400-e29b-41d4-a716-446655440020",
    "quantity": 2,
    "unitPrice": 49.99
  }
}
```

---

## Cart Status Lifecycle

```
     ┌─────────────────────────────────────────┐
     │                                         │
     ▼                                         │
  ┌──────┐    ┌───────────┐    ┌───────────┐  │
  │ACTIVE│────│ ABANDONED │────│  EXPIRED  │  │
  └──────┘    └───────────┘    └───────────┘  │
     │                                         │
     │         ┌───────────┐                   │
     └─────────│ CONVERTED │                   │
               └───────────┘                   │
                    │                          │
                    │         ┌────────┐       │
                    └─────────│ MERGED │───────┘
                              └────────┘
```

| Status | Description | Can Modify |
|--------|-------------|------------|
| ACTIVE | Cart is active and can receive items | ✅ |
| ABANDONED | Cart was abandoned by user | ❌ |
| EXPIRED | Cart expired due to inactivity (7 days) | ❌ |
| CONVERTED | Cart was converted to an order | ❌ |
| MERGED | Cart was merged into another cart | ❌ |

---

## Rate Limiting

- **Standard Rate:** 100 requests per minute per user
- **Burst Rate:** 200 requests per minute per user

---

## Health Check

**GET** `/actuator/health`

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "kafka": { "status": "UP" },
    "discoveryComposite": { "status": "UP" }
  }
}
```
