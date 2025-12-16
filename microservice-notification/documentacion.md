# Documentación API - Microservice Notification

## Descripción General

API RESTful para gestión de notificaciones del sistema Karibea. Todos los IDs utilizan formato **UUID** para mayor seguridad y escalabilidad.

## URL Base

- **Desarrollo local**: `http://localhost:8085`
- **API Gateway**: `http://api-gateway:8080`
- **Swagger UI**: `http://localhost:8085/swagger-ui.html`

---

## Endpoints de Notificaciones

### POST `/api/v1/notifications`
Crea una nueva notificación.

**Request:**
```json
{
  "externalUserProfileId": "user-profile-123",
  "channel": "EMAIL",
  "title": "Tu pedido ha sido confirmado",
  "message": "Tu pedido #12345 ha sido procesado exitosamente",
  "recipientEmail": "usuario@ejemplo.com",
  "recipientPhone": "+1234567890",
  "priority": "HIGH",
  "referenceType": "ORDER",
  "externalReferenceId": "order-12345",
  "actionUrl": "https://app.example.com/orders/12345",
  "metadata": "{\"orderId\": 12345}"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalUserProfileId": "user-profile-123",
  "channel": "EMAIL",
  "title": "Tu pedido ha sido confirmado",
  "message": "Tu pedido #12345 ha sido procesado exitosamente",
  "status": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "PENDING",
    "description": "Notificación pendiente de envío"
  },
  "template": null,
  "recipientEmail": "usuario@ejemplo.com",
  "recipientPhone": "+1234567890",
  "priority": "HIGH",
  "referenceType": "ORDER",
  "externalReferenceId": "order-12345",
  "actionUrl": "https://app.example.com/orders/12345",
  "metadata": "{\"orderId\": 12345}",
  "sentAt": null,
  "deliveredAt": null,
  "readAt": null,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

### GET `/api/v1/notifications/{id}`
Obtiene una notificación por su UUID.

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "externalUserProfileId": "user-profile-123",
  "channel": "EMAIL",
  "title": "Tu pedido ha sido confirmado",
  "message": "Tu pedido #12345 ha sido procesado exitosamente",
  "status": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "SENT",
    "description": "Notificación enviada"
  },
  "template": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "code": "ORDER_CONFIRMATION",
    "name": "Confirmación de Pedido",
    "channel": "EMAIL"
  },
  "recipientEmail": "usuario@ejemplo.com",
  "priority": "HIGH",
  "sentAt": "2024-01-15T10:31:00Z",
  "deliveredAt": "2024-01-15T10:31:05Z",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

### GET `/api/v1/notifications/user/{externalUserId}`
Lista todas las notificaciones de un usuario.

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "externalUserProfileId": "user-profile-123",
    "channel": "EMAIL",
    "title": "Tu pedido ha sido confirmado",
    "status": {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "SENT",
      "description": "Notificación enviada"
    },
    "createdAt": "2024-01-15T10:30:00Z"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "externalUserProfileId": "user-profile-123",
    "channel": "PUSH",
    "title": "Nueva promoción disponible",
    "status": {
      "id": "550e8400-e29b-41d4-a716-446655440004",
      "name": "DELIVERED",
      "description": "Notificación entregada"
    },
    "createdAt": "2024-01-16T14:00:00Z"
  }
]
```

---

## Endpoints de Device Tokens

### POST `/api/v1/device-tokens`
Registra un nuevo token de dispositivo para push notifications.

**Request:**
```json
{
  "externalUserId": "user-123",
  "deviceToken": "fcm_token_abc123xyz...",
  "platform": "ANDROID"
}
```

**Response (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440000",
  "externalUserId": "user-123",
  "deviceToken": "fcm_token_abc123xyz...",
  "platform": "ANDROID",
  "active": true,
  "lastUsedAt": "2024-01-15T12:00:00Z",
  "createdAt": "2024-01-15T12:00:00Z",
  "updatedAt": null
}
```

---

### GET `/api/v1/device-tokens/user/{externalUserId}`
Lista todos los tokens de dispositivo de un usuario.

**Response (200 OK):**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440000",
    "externalUserId": "user-123",
    "deviceToken": "fcm_token_abc123xyz...",
    "platform": "ANDROID",
    "active": true,
    "lastUsedAt": "2024-01-15T12:00:00Z"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "externalUserId": "user-123",
    "deviceToken": "apns_token_def456...",
    "platform": "IOS",
    "active": true,
    "lastUsedAt": "2024-01-10T08:30:00Z"
  }
]
```

---

### DELETE `/api/v1/device-tokens/{id}`
Desactiva un token de dispositivo.

**Response:** `204 No Content`

---

## Endpoints de Preferencias de Usuario

### POST `/api/v1/preferences`
Crea preferencias de notificación para un usuario.

**Request:**
```json
{
  "externalUserId": "user-123",
  "emailEnabled": true,
  "pushEnabled": true,
  "inAppEnabled": true,
  "preferences": "{\"marketing\": false, \"transactional\": true}",
  "quietHoursStart": "22:00:00",
  "quietHoursEnd": "08:00:00",
  "timezone": "America/Bogota"
}
```

**Response (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "externalUserId": "user-123",
  "emailEnabled": true,
  "pushEnabled": true,
  "inAppEnabled": true,
  "preferences": "{\"marketing\": false, \"transactional\": true}",
  "quietHoursStart": "22:00:00",
  "quietHoursEnd": "08:00:00",
  "timezone": "America/Bogota",
  "createdAt": "2024-01-15T09:00:00Z",
  "updatedAt": null
}
```

---

### GET `/api/v1/preferences/{externalUserId}`
Obtiene las preferencias de un usuario.

**Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "externalUserId": "user-123",
  "emailEnabled": true,
  "pushEnabled": true,
  "inAppEnabled": true,
  "preferences": "{\"marketing\": false, \"transactional\": true}",
  "quietHoursStart": "22:00:00",
  "quietHoursEnd": "08:00:00",
  "timezone": "America/Bogota",
  "createdAt": "2024-01-15T09:00:00Z",
  "updatedAt": "2024-01-16T10:00:00Z"
}
```

---

### PUT `/api/v1/preferences/{externalUserId}`
Actualiza las preferencias de un usuario.

**Request:**
```json
{
  "externalUserId": "user-123",
  "emailEnabled": false,
  "pushEnabled": true,
  "inAppEnabled": true,
  "preferences": "{\"marketing\": false, \"transactional\": true}",
  "quietHoursStart": "21:00:00",
  "quietHoursEnd": "07:00:00",
  "timezone": "America/Bogota"
}
```

**Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "externalUserId": "user-123",
  "emailEnabled": false,
  "pushEnabled": true,
  "inAppEnabled": true,
  "preferences": "{\"marketing\": false, \"transactional\": true}",
  "quietHoursStart": "21:00:00",
  "quietHoursEnd": "07:00:00",
  "timezone": "America/Bogota",
  "createdAt": "2024-01-15T09:00:00Z",
  "updatedAt": "2024-01-16T11:00:00Z"
}
```

---

## Canales de Notificación

| Canal   | Descripción                              |
|---------|------------------------------------------|
| EMAIL   | Notificaciones por correo electrónico    |
| SMS     | Notificaciones por mensaje de texto      |
| PUSH    | Notificaciones push móviles (FCM/APNS)   |
| IN_APP  | Notificaciones dentro de la aplicación   |

## Códigos de Estado HTTP

| Código | Descripción                              |
|--------|------------------------------------------|
| 200    | Operación exitosa                        |
| 201    | Recurso creado exitosamente              |
| 204    | Operación exitosa sin contenido          |
| 400    | Datos de entrada inválidos               |
| 404    | Recurso no encontrado                    |
| 409    | Conflicto (recurso ya existe)            |
| 500    | Error interno del servidor               |
