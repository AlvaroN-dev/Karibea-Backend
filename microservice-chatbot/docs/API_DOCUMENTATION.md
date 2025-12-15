# Chatbot Microservice API Documentation

## Overview

The Chatbot Microservice provides a **RAG (Retrieval-Augmented Generation)** chatbot API for handling customer conversations. Built with AI integration, conversation management, and human agent escalation capabilities.

**Base URL:** `http://localhost:8086/chat`

**Swagger UI:** `http://localhost:8086/swagger-ui.html`

---

## Authentication

All endpoints require OAuth2 JWT Bearer token authentication:

```
Authorization: Bearer <jwt_token>
```

---

## Endpoints Summary

| # | Method | Endpoint | Description |
|---|--------|----------|-------------|
| 1 | POST | `/messages` | Send message to chatbot |
| 2 | GET | `/conversations/{id}` | Get conversation by ID |
| 3 | POST | `/conversations` | Create new conversation |
| 4 | GET | `/sources` | Get available RAG sources |
| 5 | POST | `/escalate` | Escalate to human agent |

---

## 1. Send Message

Sends a message to the chatbot and receives an AI-generated response.

**POST** `/messages`

**Request Body:**
```json
{
  "conversationId": "550e8400-e29b-41d4-a716-446655440000",
  "content": "¿Cuáles son los horarios de atención?",
  "externalSenderId": "550e8400-e29b-41d4-a716-446655440001",
  "messageTypeId": "550e8400-e29b-41d4-a716-446655440002"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| conversationId | UUID | ✅ | Must be valid UUID |
| content | string | ✅ | Max 4000 characters |
| externalSenderId | UUID | ❌ | Optional sender reference |
| messageTypeId | UUID | ❌ | Optional message type |

**Response (200 OK):**
```json
{
  "answerText": "Nuestros horarios de atención son de lunes a viernes de 8:00 AM a 6:00 PM, y sábados de 9:00 AM a 2:00 PM.",
  "sources": [
    {
      "id": "kb-001",
      "type": "knowledge_base",
      "score": 0.95
    },
    {
      "id": "faq-042",
      "type": "faq",
      "score": 0.87
    }
  ],
  "actions": [
    {
      "type": "show_schedule",
      "payload": {
        "department": "support",
        "timezone": "America/Bogota"
      }
    }
  ],
  "intent": {
    "id": "business_hours",
    "confidence": 0.92
  }
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| answerText | string | AI-generated response text |
| sources | array | Knowledge sources used for the answer |
| sources[].id | string | Source identifier |
| sources[].type | string | Source type (knowledge_base, faq, document) |
| sources[].score | decimal | Relevance score (0.0 - 1.0) |
| actions | array | Suggested actions for the frontend |
| actions[].type | string | Action type |
| actions[].payload | object | Action-specific data |
| intent | object | Detected user intent |
| intent.id | string | Intent identifier |
| intent.confidence | decimal | Confidence score (0.0 - 1.0) |

**Error Responses:**
- `400` - Invalid request (missing content or conversationId)
- `404` - Conversation not found
- `500` - Internal server error (AI provider unavailable)

---

## 2. Get Conversation

Retrieves a conversation with its complete message history.

**GET** `/conversations/{conversationId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| conversationId | UUID | ✅ | Conversation identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalUserProfileId": "550e8400-e29b-41d4-a716-446655440010",
  "externalSessionId": "550e8400-e29b-41d4-a716-446655440011",
  "channel": "web",
  "status": "ACTIVE",
  "startedAt": "2024-12-13T10:30:00",
  "endedAt": null,
  "createdAt": "2024-12-13T10:30:00",
  "messageCount": 4,
  "userInfo": {
    "id": "550e8400-e29b-41d4-a716-446655440010",
    "username": "juan.perez",
    "email": "juan.perez@example.com"
  },
  "messages": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440020",
      "senderType": "USER",
      "content": "Hola, necesito ayuda con mi pedido",
      "intent": "order_help",
      "confidence": 0.89,
      "read": true,
      "createdAt": "2024-12-13T10:30:00"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440021",
      "senderType": "BOT",
      "content": "¡Hola! Con gusto te ayudo. ¿Puedes proporcionarme tu número de pedido?",
      "intent": null,
      "confidence": null,
      "read": true,
      "createdAt": "2024-12-13T10:30:05"
    }
  ]
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Conversation unique identifier |
| externalUserProfileId | UUID | Reference to user profile |
| externalSessionId | UUID | Reference to user session |
| channel | string | Channel (web, mobile, whatsapp) |
| status | string | ACTIVE, CLOSED, ESCALATED |
| startedAt | datetime | Conversation start time |
| endedAt | datetime | Conversation end time (null if active) |
| createdAt | datetime | Record creation timestamp |
| messageCount | integer | Total number of messages |
| userInfo | object | User details from Identity service |
| userInfo.id | UUID | User unique identifier |
| userInfo.username | string | Username |
| userInfo.email | string | User email address |
| messages | array | List of messages in chronological order |

**Message Fields:**

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Message unique identifier |
| senderType | string | USER or BOT |
| content | string | Message text content |
| intent | string | Detected intent (null for bot messages) |
| confidence | double | Intent confidence score |
| read | boolean | Whether message has been read |
| createdAt | datetime | Message timestamp |

**Error Response:** `404` - Conversation not found

---

## 3. Create Conversation

Starts a new chat conversation.

**POST** `/conversations`

**Request Body:**
```json
{
  "externalUserProfileId": "550e8400-e29b-41d4-a716-446655440010",
  "externalSessionId": "550e8400-e29b-41d4-a716-446655440011",
  "channel": "web",
  "title": "Consulta sobre productos"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| externalUserProfileId | UUID | ❌ | User profile reference |
| externalSessionId | UUID | ❌ | Session reference |
| channel | string | ✅ | Max 50 chars (web, mobile, whatsapp) |
| title | string | ❌ | Max 255 characters |

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalUserProfileId": "550e8400-e29b-41d4-a716-446655440010",
  "externalSessionId": "550e8400-e29b-41d4-a716-446655440011",
  "channel": "web",
  "status": "ACTIVE",
  "startedAt": "2024-12-13T10:30:00",
  "endedAt": null,
  "createdAt": "2024-12-13T10:30:00",
  "messageCount": 0,
  "messages": []
}
```

**Error Response:** `400` - Invalid request

---

## 4. Get Sources

Retrieves available knowledge sources for the RAG chatbot.

**GET** `/sources`

**Response (200 OK):**
```json
[
  {
    "id": "kb-products-001",
    "type": "KNOWLEDGE_BASE",
    "content": "Product catalog and specifications",
    "score": 1.0
  },
  {
    "id": "faq-general",
    "type": "FAQ",
    "content": "Frequently asked questions database",
    "score": 1.0
  },
  {
    "id": "policy-shipping",
    "type": "DOCUMENT",
    "content": "Shipping and returns policy document",
    "score": 0.95
  }
]
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| id | string | Source unique identifier |
| type | string | KNOWLEDGE_BASE, FAQ, DOCUMENT, PRODUCT |
| content | string | Description of the source content |
| score | decimal | Base relevance score |

---

## 5. Escalate Conversation

Escalates a conversation to a human agent.

**POST** `/escalate`

**Request Body:**
```json
{
  "conversationId": "550e8400-e29b-41d4-a716-446655440000",
  "reason": "Customer requested human agent for complex issue",
  "priority": "HIGH"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| conversationId | UUID | ✅ | Must exist |
| reason | string | ✅ | Max 255 characters |
| priority | string | ✅ | LOW, MEDIUM, HIGH, URGENT |

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440099",
  "conversationId": "550e8400-e29b-41d4-a716-446655440000",
  "reason": "Customer requested human agent for complex issue",
  "priority": "HIGH",
  "escalatedAt": "2024-12-13T11:45:00"
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Escalation ticket ID |
| conversationId | UUID | Associated conversation |
| reason | string | Escalation reason |
| priority | string | Priority level |
| escalatedAt | datetime | Escalation timestamp |

**Error Responses:**
- `400` - Invalid request
- `404` - Conversation not found

---

## Error Response Format

All error responses follow this format:

```json
{
  "code": "ERROR_CODE",
  "message": "Human-readable error message",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/chat/messages"
}
```

**Validation Error Format:**
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for 1 field(s)",
  "errors": [
    {
      "field": "content",
      "message": "Message content cannot be empty"
    }
  ],
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/chat/messages"
}
```

---

## Conversation Status Values

| Status | Description |
|--------|-------------|
| ACTIVE | Conversation is ongoing |
| CLOSED | Conversation has ended |
| ESCALATED | Transferred to human agent |

---

## Priority Levels

| Priority | Description |
|----------|-------------|
| LOW | Standard response time |
| MEDIUM | Elevated priority |
| HIGH | Urgent attention needed |
| URGENT | Immediate response required |

---

## Source Types

| Type | Description |
|------|-------------|
| KNOWLEDGE_BASE | Internal knowledge base documents |
| FAQ | Frequently asked questions |
| DOCUMENT | Policy and procedure documents |
| PRODUCT | Product catalog and specifications |

---

## Health Check

**GET** `/actuator/health`

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "ai-provider": { "status": "UP" },
    "discoveryComposite": { "status": "UP" }
  }
}
```
