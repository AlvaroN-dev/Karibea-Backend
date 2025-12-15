# Review Microservice - API Documentation

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

1. [Reviews](#reviews)
   - [Create Review](#create-review)
   - [Get Review by ID](#get-review-by-id)
   - [List Reviews by Product](#list-reviews-by-product)
   - [Moderate Review](#moderate-review)
   - [Vote on Review](#vote-on-review)
   - [Report Review](#report-review)
   - [Delete Review](#delete-review)
2. [Error Responses](#error-responses)

---

## Reviews

### Create Review

Creates a new product review.

**Endpoint:** `POST /api/v1/reviews`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "orderId": "123e4567-e89b-12d3-a456-426614174002",
  "orderItemId": "123e4567-e89b-12d3-a456-426614174003",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations. The quality is amazing and arrived quickly. Would definitely recommend to anyone looking for a reliable purchase.",
  "imageUrls": [
    "https://cdn.example.com/reviews/image1.jpg",
    "https://cdn.example.com/reviews/image2.jpg"
  ],
  "verifiedPurchase": true
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| productId | UUID | Yes | Product identifier |
| userId | UUID | Yes | User profile identifier |
| orderId | UUID | No | Associated order identifier |
| orderItemId | UUID | No | Associated order item identifier |
| rating | Integer | Yes | Rating (1-5 stars) |
| title | String | No | Review title (max 255 chars) |
| comment | String | Yes | Review comment (10-5000 chars) |
| imageUrls | Array | No | List of image URLs |
| verifiedPurchase | Boolean | No | Whether this is a verified purchase |

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "orderId": "123e4567-e89b-12d3-a456-426614174002",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations. The quality is amazing and arrived quickly. Would definitely recommend to anyone looking for a reliable purchase.",
  "status": "PENDING",
  "helpfulVoteCount": 0,
  "unhelpfulVoteCount": 0,
  "reportedCount": 0,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z",
  "images": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "url": "https://cdn.example.com/reviews/image1.jpg",
      "displayOrder": 0,
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "660e8400-e29b-41d4-a716-446655440002",
      "url": "https://cdn.example.com/reviews/image2.jpg",
      "displayOrder": 1,
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

**Error Responses:**
- `400 Bad Request` - Invalid request data
- `409 Conflict` - User has already reviewed this product

---

### Get Review by ID

Retrieves detailed review information by ID.

**Endpoint:** `GET /api/v1/reviews/{id}`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Review identifier |

**Example Request:**
```
GET /api/v1/reviews/550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "orderId": "123e4567-e89b-12d3-a456-426614174002",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations.",
  "status": "APPROVED",
  "helpfulVoteCount": 42,
  "unhelpfulVoteCount": 3,
  "reportedCount": 0,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T14:45:00Z",
  "images": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "url": "https://cdn.example.com/reviews/image1.jpg",
      "displayOrder": 0,
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

**Error Responses:**
- `404 Not Found` - Review not found

---

### List Reviews by Product

Retrieves a paginated list of approved reviews for a product.

**Endpoint:** `GET /api/v1/reviews/product/{productId}`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| productId | UUID | Product identifier |

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 10 | Page size |

**Example Request:**
```
GET /api/v1/reviews/product/123e4567-e89b-12d3-a456-426614174000?page=0&size=10
```

**Response (200 OK):**
```json
{
  "reviews": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "userId": "123e4567-e89b-12d3-a456-426614174001",
      "orderId": "123e4567-e89b-12d3-a456-426614174002",
      "rating": 5,
      "title": "Excellent product!",
      "comment": "This product exceeded my expectations.",
      "status": "APPROVED",
      "helpfulVoteCount": 42,
      "unhelpfulVoteCount": 3,
      "reportedCount": 0,
      "verifiedPurchase": true,
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T14:45:00Z",
      "images": []
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440099",
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "userId": "123e4567-e89b-12d3-a456-426614174005",
      "orderId": null,
      "rating": 4,
      "title": "Good value",
      "comment": "Good product for the price.",
      "status": "APPROVED",
      "helpfulVoteCount": 15,
      "unhelpfulVoteCount": 1,
      "reportedCount": 0,
      "verifiedPurchase": false,
      "createdAt": "2024-01-16T09:00:00Z",
      "updatedAt": "2024-01-16T09:00:00Z",
      "images": []
    }
  ],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0,
  "pageSize": 10,
  "averageRating": 4.5
}
```

---

### Moderate Review

Approves, rejects, or flags a review.

**Endpoint:** `POST /api/v1/reviews/{id}/moderate`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Review identifier |

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "moderatorId": "123e4567-e89b-12d3-a456-426614174004",
  "action": "APPROVED",
  "reason": null
}
```

**Request Body (Rejection):**
```json
{
  "moderatorId": "123e4567-e89b-12d3-a456-426614174004",
  "action": "REJECTED",
  "reason": "Contains inappropriate content that violates community guidelines."
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| moderatorId | UUID | Yes | Moderator user identifier |
| action | String | Yes | APPROVED, REJECTED, or FLAGGED |
| reason | String | No* | Required for REJECTED and FLAGGED |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations.",
  "status": "APPROVED",
  "helpfulVoteCount": 0,
  "unhelpfulVoteCount": 0,
  "reportedCount": 0,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T14:45:00Z",
  "images": []
}
```

**Error Responses:**
- `400 Bad Request` - Invalid moderation action
- `404 Not Found` - Review not found

---

### Vote on Review

Marks a review as helpful or not helpful.

**Endpoint:** `POST /api/v1/reviews/{id}/vote`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Review identifier |

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174005",
  "voteType": "HELPFUL"
}
```

**Request Body Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userId | UUID | Yes | User identifier |
| voteType | String | Yes | HELPFUL or NOT_HELPFUL |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations.",
  "status": "APPROVED",
  "helpfulVoteCount": 43,
  "unhelpfulVoteCount": 3,
  "reportedCount": 0,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T16:00:00Z",
  "images": []
}
```

**Error Responses:**
- `404 Not Found` - Review not found
- `409 Conflict` - User has already voted on this review

---

### Report Review

Reports a review as inappropriate.

**Endpoint:** `POST /api/v1/reviews/{id}/report`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Review identifier |

**Example Request:**
```
POST /api/v1/reviews/550e8400-e29b-41d4-a716-446655440000/report
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations.",
  "status": "APPROVED",
  "helpfulVoteCount": 42,
  "unhelpfulVoteCount": 3,
  "reportedCount": 1,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T17:00:00Z",
  "images": []
}
```

**Error Responses:**
- `404 Not Found` - Review not found

---

### Delete Review

Soft deletes a review.

**Endpoint:** `DELETE /api/v1/reviews/{id}`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Review identifier |

**Example Request:**
```
DELETE /api/v1/reviews/550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "rating": 5,
  "title": "Excellent product!",
  "comment": "This product exceeded my expectations.",
  "status": "DELETED",
  "helpfulVoteCount": 42,
  "unhelpfulVoteCount": 3,
  "reportedCount": 0,
  "verifiedPurchase": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T18:00:00Z",
  "images": []
}
```

**Error Responses:**
- `404 Not Found` - Review not found

---

## Error Responses

### Standard Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "code": "ERROR_CODE",
  "message": "Descriptive error message",
  "path": "/api/v1/reviews",
  "traceId": "abc123def456"
}
```

### Validation Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/v1/reviews",
  "traceId": "abc123def456",
  "fieldErrors": [
    {
      "field": "comment",
      "rejectedValue": "short",
      "message": "Comment must be between 10 and 5000 characters"
    },
    {
      "field": "rating",
      "rejectedValue": 6,
      "message": "Rating must be at most 5"
    }
  ]
}
```

### Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| VALIDATION_ERROR | 400 | Request validation failed |
| INVALID_ARGUMENT | 400 | Invalid argument provided |
| INVALID_REVIEW_STATE | 400 | Invalid review state transition |
| REVIEW_NOT_FOUND | 404 | Review not found |
| DUPLICATE_REVIEW | 409 | User has already reviewed this product |
| DUPLICATE_VOTE | 409 | User has already voted on this review |
| INVALID_STATE | 409 | Invalid state transition |
| INTERNAL_ERROR | 500 | Unexpected server error |

---

## Review Status Values

| Status | Description |
|--------|-------------|
| PENDING | Review awaiting moderation |
| APPROVED | Review approved and visible |
| REJECTED | Review rejected by moderator |
| FLAGGED | Review flagged for additional review |
| DELETED | Review soft deleted |

---

## Example Workflows

### Submit and Approve a Review

1. **Create the review:**
```bash
curl -X POST http://localhost:8080/api/v1/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "123e4567-e89b-12d3-a456-426614174001",
    "rating": 5,
    "comment": "This product exceeded my expectations. Highly recommend!"
  }'
```

2. **Approve the review:**
```bash
curl -X POST http://localhost:8080/api/v1/reviews/{reviewId}/moderate \
  -H "Content-Type: application/json" \
  -d '{
    "moderatorId": "123e4567-e89b-12d3-a456-426614174004",
    "action": "APPROVED"
  }'
```

3. **Vote on the review:**
```bash
curl -X POST http://localhost:8080/api/v1/reviews/{reviewId}/vote \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "123e4567-e89b-12d3-a456-426614174005",
    "voteType": "HELPFUL"
  }'
```

---

## Notes

- All UUIDs are in standard format: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`
- Timestamps are in ISO 8601 format with UTC timezone
- Only APPROVED reviews are returned in public listings
- Soft delete pattern preserves data for analytics
