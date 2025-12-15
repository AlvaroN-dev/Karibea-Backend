# Payment Microservice - API Documentation

## Interactive API Documentation (Swagger UI)
Access the interactive API documentation at:
- **Local:** [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)
- **OpenAPI Spec:** [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)

## Base URL
```
http://localhost:8085/api/v1
```

## Authentication
Todos los endpoints requieren token JWT OAuth2:
```
Authorization: Bearer <access_token>
```

---

## 1. Transactions

### POST /transactions
**Crear una nueva transacción**

**Scopes requeridos:** `payment:write`

**Request:**
```json
{
  "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 149.99,
  "currency": "USD",
  "transactionType": "PAYMENT",
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789"
}
```

**Response (201 Created):**
```json
{
  "id": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 149.99,
  "currency": "USD",
  "status": "PENDING",
  "statusDescription": "Transaction pending processing",
  "type": "PAYMENT",
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "providerTransactionId": null,
  "failureReason": null,
  "refunds": [],
  "totalRefunded": 0.00,
  "refundableAmount": 149.99,
  "createdAt": "2025-12-13T09:30:00",
  "updatedAt": "2025-12-13T09:30:00"
}
```

---

### POST /transactions/{transactionId}/process
**Procesar una transacción pendiente**

**Scopes requeridos:** `payment:write`

**Request:**
```json
{
  "transactionId": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "cardToken": "tok_visa_4242424242424242",
  "cvv": "123"
}
```

**Response (200 OK):**
```json
{
  "id": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 149.99,
  "currency": "USD",
  "status": "COMPLETED",
  "statusDescription": "Transaction completed successfully",
  "type": "PAYMENT",
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "providerTransactionId": "stripe_1702456200123",
  "failureReason": null,
  "refunds": [],
  "totalRefunded": 0.00,
  "refundableAmount": 149.99,
  "createdAt": "2025-12-13T09:30:00",
  "updatedAt": "2025-12-13T09:31:00"
}
```

---

### GET /transactions/{transactionId}
**Obtener transacción por ID**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
{
  "id": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "amount": 149.99,
  "currency": "USD",
  "status": "COMPLETED",
  "statusDescription": "Transaction completed successfully",
  "type": "PAYMENT",
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "providerTransactionId": "stripe_1702456200123",
  "failureReason": null,
  "refunds": [],
  "totalRefunded": 0.00,
  "refundableAmount": 149.99,
  "createdAt": "2025-12-13T09:30:00",
  "updatedAt": "2025-12-13T09:31:00"
}
```

---

### GET /transactions/order/{orderId}
**Obtener transacciones por orden**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
[
  {
    "id": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
    "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "amount": 149.99,
    "currency": "USD",
    "status": "COMPLETED",
    "statusDescription": "Transaction completed successfully",
    "type": "PAYMENT",
    "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
    "providerTransactionId": "stripe_1702456200123",
    "failureReason": null,
    "refunds": [],
    "totalRefunded": 0.00,
    "refundableAmount": 149.99,
    "createdAt": "2025-12-13T09:30:00",
    "updatedAt": "2025-12-13T09:31:00"
  }
]
```

---

### GET /transactions/user/{userId}
**Obtener transacciones por usuario**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
[
  {
    "id": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
    "externalOrderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "externalUserId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "amount": 149.99,
    "currency": "USD",
    "status": "COMPLETED",
    "statusDescription": "Transaction completed successfully",
    "type": "PAYMENT",
    "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
    "providerTransactionId": "stripe_1702456200123",
    "failureReason": null,
    "refunds": [],
    "totalRefunded": 0.00,
    "refundableAmount": 149.99,
    "createdAt": "2025-12-13T09:30:00",
    "updatedAt": "2025-12-13T09:31:00"
  }
]
```

---

## 2. Refunds

### POST /refunds
**Crear un reembolso**

**Scopes requeridos:** `payment:refund`

**Request:**
```json
{
  "transactionId": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "amount": 50.00,
  "currency": "USD",
  "reason": "Customer requested partial refund - item damaged"
}
```

**Response (201 Created):**
```json
{
  "id": "e5f6a7b8-c9d0-1234-5678-9abcdef01234",
  "transactionId": "d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "amount": 50.00,
  "currency": "USD",
  "status": "PENDING",
  "statusDescription": "Refund pending processing",
  "reason": "Customer requested partial refund - item damaged",
  "providerRefundId": null,
  "failureReason": null,
  "createdAt": "2025-12-13T10:00:00",
  "processedAt": null
}
```

---

## 3. Payment Methods

### GET /payment-methods
**Obtener métodos de pago activos del sistema**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
[
  {
    "id": "c8d9e0f1-2345-6789-abcd-ef0123456789",
    "name": "Visa Credit Card",
    "type": "CREDIT_CARD",
    "typeDisplayName": "Credit Card",
    "providerCode": "stripe_visa",
    "isActive": true,
    "description": "Pay with Visa credit card",
    "iconUrl": "https://cdn.example.com/icons/visa.png",
    "displayOrder": 1,
    "requiresCardDetails": true,
    "supportsRecurring": true,
    "supportsRefund": true
  },
  {
    "id": "d9e0f123-4567-89ab-cdef-0123456789ab",
    "name": "PayPal",
    "type": "PAYPAL",
    "typeDisplayName": "PayPal",
    "providerCode": "paypal_standard",
    "isActive": true,
    "description": "Pay with your PayPal account",
    "iconUrl": "https://cdn.example.com/icons/paypal.png",
    "displayOrder": 2,
    "requiresCardDetails": false,
    "supportsRecurring": true,
    "supportsRefund": true
  }
]
```

---

### GET /payment-methods/{paymentMethodId}
**Obtener método de pago por ID**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
{
  "id": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "name": "Visa Credit Card",
  "type": "CREDIT_CARD",
  "typeDisplayName": "Credit Card",
  "providerCode": "stripe_visa",
  "isActive": true,
  "description": "Pay with Visa credit card",
  "iconUrl": "https://cdn.example.com/icons/visa.png",
  "displayOrder": 1,
  "requiresCardDetails": true,
  "supportsRecurring": true,
  "supportsRefund": true
}
```

---

### GET /payment-methods/user/{userId}
**Obtener métodos de pago guardados del usuario**

**Scopes requeridos:** `payment:read`

**Response (200 OK):**
```json
[
  {
    "id": "f0a1b2c3-d4e5-6789-abcd-ef0123456789",
    "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
    "paymentMethodName": "Visa Credit Card",
    "alias": "My Personal Visa",
    "maskedCardNumber": "****4242",
    "cardBrand": "VISA",
    "expiryMonth": "12",
    "expiryYear": "2026",
    "isDefault": true,
    "isActive": true,
    "createdAt": "2025-11-01T14:30:00"
  },
  {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
    "paymentMethodName": "Visa Credit Card",
    "alias": "Work Card",
    "maskedCardNumber": "****1234",
    "cardBrand": "VISA",
    "expiryMonth": "08",
    "expiryYear": "2025",
    "isDefault": false,
    "isActive": true,
    "createdAt": "2025-10-15T09:00:00"
  }
]
```

---

### POST /payment-methods/user
**Guardar método de pago para el usuario autenticado**

**Scopes requeridos:** `payment:write`

**Request:**
```json
{
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "tokenizedData": "tok_visa_encrypted_abc123xyz789",
  "alias": "My New Card",
  "maskedCardNumber": "****5555",
  "cardBrand": "MASTERCARD",
  "expiryMonth": "03",
  "expiryYear": "2027",
  "setAsDefault": true
}
```

**Response (201 Created):**
```json
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f01234567890",
  "paymentMethodId": "c8d9e0f1-2345-6789-abcd-ef0123456789",
  "paymentMethodName": "Visa Credit Card",
  "alias": "My New Card",
  "maskedCardNumber": "****5555",
  "cardBrand": "MASTERCARD",
  "expiryMonth": "03",
  "expiryYear": "2027",
  "isDefault": true,
  "isActive": true,
  "createdAt": "2025-12-13T10:30:00"
}
```

---

### DELETE /payment-methods/user/{userPaymentMethodId}
**Eliminar método de pago guardado**

**Scopes requeridos:** `payment:write`

**Response (204 No Content)**

---

### PUT /payment-methods/user/{userPaymentMethodId}/default
**Establecer método de pago como predeterminado**

**Scopes requeridos:** `payment:write`

**Response (200 OK)**

---

## Error Responses

### Validation Error (400)
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Validation failed",
  "path": "/api/v1/transactions",
  "fieldErrors": [
    {"field": "amount", "message": "Amount must be greater than zero"},
    {"field": "currency", "message": "Currency must be a 3-letter ISO code"}
  ]
}
```

### Not Found (404)
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 404,
  "error": "Not Found",
  "code": "TRANSACTION_NOT_FOUND",
  "message": "Transaction not found with ID: d4e5f6a7-b8c9-0123-4567-89abcdef0123",
  "path": "/api/v1/transactions/d4e5f6a7-b8c9-0123-4567-89abcdef0123"
}
```

### Conflict (409) - Invalid State Transition
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 409,
  "error": "Conflict",
  "code": "INVALID_TRANSACTION_STATE",
  "message": "Cannot transition from COMPLETED to PROCESSING",
  "path": "/api/v1/transactions/d4e5f6a7-b8c9-0123-4567-89abcdef0123/process"
}
```

### Unprocessable Entity (422) - Business Rule Violation
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "code": "REFUND_EXCEEDS_TRANSACTION",
  "message": "Refund amount 200.00 USD exceeds available refundable amount 149.99 USD",
  "path": "/api/v1/refunds"
}
```

### Forbidden (403)
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 403,
  "error": "Forbidden",
  "code": "ACCESS_DENIED",
  "message": "You do not have permission to perform this action",
  "path": "/api/v1/refunds"
}
```

### Bad Gateway (502) - Payment Provider Error
```json
{
  "timestamp": "2025-12-13T09:30:00",
  "status": 502,
  "error": "Bad Gateway",
  "code": "PAYMENT_PROVIDER_ERROR",
  "message": "Payment processing failed. Please try again later.",
  "path": "/api/v1/transactions/d4e5f6a7-b8c9-0123-4567-89abcdef0123/process"
}
```

---

## Enums Reference

### TransactionStatus
| Value | Description |
|-------|-------------|
| `PENDING` | Awaiting processing |
| `PROCESSING` | Being processed |
| `COMPLETED` | Successfully completed |
| `FAILED` | Processing failed |
| `CANCELLED` | Cancelled |
| `PARTIALLY_REFUNDED` | Partially refunded |
| `REFUNDED` | Fully refunded |

### TransactionType
| Value | Description |
|-------|-------------|
| `PAYMENT` | Standard payment |
| `REFUND` | Full refund |
| `PARTIAL_REFUND` | Partial refund |
| `CHARGEBACK` | Disputed charge |
| `AUTHORIZATION` | Pre-authorization |
| `CAPTURE` | Capture authorized funds |
| `VOID` | Void transaction |

### PaymentMethodType
| Value | Description |
|-------|-------------|
| `CREDIT_CARD` | Credit card |
| `DEBIT_CARD` | Debit card |
| `PAYPAL` | PayPal |
| `BANK_TRANSFER` | Bank transfer |
| `DIGITAL_WALLET` | Digital wallet |
| `CRYPTO` | Cryptocurrency |
| `CASH_ON_DELIVERY` | Cash payment |

### RefundStatus
| Value | Description |
|-------|-------------|
| `PENDING` | Awaiting processing |
| `APPROVED` | Approved |
| `PROCESSING` | Being processed |
| `COMPLETED` | Successfully completed |
| `REJECTED` | Rejected |
| `FAILED` | Processing failed |
