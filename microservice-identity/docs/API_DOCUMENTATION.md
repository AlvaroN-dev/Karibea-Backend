# Identity Microservice API Documentation

## Overview

The Identity Microservice provides authentication, user management, and role-based access control (RBAC). Built with **Spring Boot**, **JWT authentication**, and **OAuth2** social login support.

**Base URL:** `http://localhost:8081/api/v1`

**Swagger UI:** `http://localhost:8081/swagger-ui.html`

---

## Authentication

Most endpoints require JWT Bearer token authentication:

```
Authorization: Bearer <jwt_token>
```

---

## Endpoints Summary

| Tag | Endpoints | Description |
|-----|-----------|-------------|
| Authentication | 4 | Login, logout, token management |
| OAuth2 | 2 | Social login (Google, GitHub, etc.) |
| Users | 7 | User registration and management |
| Roles | 8 | Role CRUD and user assignments |

---

## 1. Authentication Endpoints

### 1.1 User Login

Authenticates a user with username and password.

**POST** `/auth/login`

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "SecurePass123!"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "name": "John Doe",
    "enabled": true,
    "emailVerified": true,
    "roles": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174001",
        "name": "USER",
        "description": "Standard user access",
        "accessLevel": "USER"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "lastLogin": "2024-12-13T14:25:00"
  }
}
```

**Error Responses:**
- `400` - Invalid input data
- `401` - Invalid credentials
- `403` - Account disabled or not verified

---

### 1.2 Refresh Token

Generates a new access token using a valid refresh token.

**POST** `/auth/refresh`

**Headers:**
```
Authorization: Bearer <refresh_token>
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

**Error Response:** `401` - Invalid or expired refresh token

---

### 1.3 Validate Token

Validates if an access token is valid and not expired.

**GET** `/auth/validate`

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response:**
- `200` - Token is valid
- `401` - Token is invalid or expired

---

### 1.4 Logout

Logs out a user by invalidating their token.

**POST** `/auth/logout`

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response:** `200 OK`

---

## 2. OAuth2 Endpoints

### 2.1 OAuth2 Success Callback

Handles successful OAuth2 authentication from social providers.

**GET** `/auth/oauth2/success`

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "name": "John Doe",
    "enabled": true,
    "emailVerified": true,
    "roles": [],
    "createdAt": "2024-01-15T10:30:00",
    "lastLogin": "2024-12-13T14:25:00"
  }
}
```

**Error Responses:**
- `400` - Invalid OAuth2 user data
- `401` - User not registered
- `500` - Error processing authentication

---

### 2.2 OAuth2 Failure Callback

Handles failed OAuth2 authentication.

**GET** `/auth/oauth2/failure`

**Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "OAuth2 authentication failed",
  "error": "Authentication with OAuth2 provider failed"
}
```

---

## 3. User Endpoints

### 3.1 Register User

Creates a new user account.

**POST** `/users/register`

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| username | string | ✅ | 3-50 characters |
| email | string | ✅ | Valid email format |
| password | string | ✅ | 8-100 characters |

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "name": null,
  "enabled": true,
  "emailVerified": false,
  "roles": [],
  "createdAt": "2024-12-13T10:30:00",
  "lastLogin": null
}
```

**Error Responses:**
- `400` - Invalid input data
- `409` - Username or email already exists

---

### 3.2 Get User by ID

Retrieves a user by their unique identifier.

**GET** `/users/{id}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | UUID | ✅ | User unique identifier |

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "name": "John Doe",
  "enabled": true,
  "emailVerified": true,
  "roles": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "name": "ADMIN",
      "description": "Administrator with full system access",
      "accessLevel": "ADMIN"
    }
  ],
  "createdAt": "2024-01-15T10:30:00",
  "lastLogin": "2024-12-13T14:25:00"
}
```

**Error Response:** `404` - User not found

---

### 3.3 Get User by Username

Retrieves a user by their username.

**GET** `/users/username/{username}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| username | string | ✅ | Username to search |

**Response:** Same as Get User by ID

---

### 3.4 Get All Users

Retrieves a list of all registered users.

**GET** `/users`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "name": "John Doe",
    "enabled": true,
    "emailVerified": true,
    "roles": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174001",
        "name": "USER",
        "description": "Standard user access",
        "accessLevel": "USER"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "lastLogin": "2024-12-13T14:25:00"
  }
]
```

---

### 3.5 Update User Profile

Updates user profile information (email and name).

**PUT** `/users/{id}`

**Request Body:**
```json
{
  "email": "newemail@example.com",
  "name": "John Doe Updated"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| email | string | ❌ | Valid email format |
| name | string | ❌ | Max 100 characters |

**Response:** `200 OK`

**Error Responses:**
- `400` - Invalid input data
- `404` - User not found
- `409` - Email already in use

---

### 3.6 Delete User (Soft Delete)

Performs a soft delete on the user account.

**DELETE** `/users/{id}`

**Response:** `204 No Content`

**Error Responses:**
- `404` - User not found
- `409` - User is already deleted

> **Note:** Soft deleted users are marked as deleted but data is retained in the database.

---

### 3.7 Verify Email

Verifies a user's email address using the verification token.

**POST** `/users/verify-email`

**Query Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| userId | UUID | ✅ | User ID |
| token | string | ✅ | Email verification token |

**Response:** `200 OK`

**Error Responses:**
- `400` - Invalid or expired verification token
- `404` - User not found

---

## 4. Role Endpoints

### 4.1 Create Role

Creates a new role.

**POST** `/roles`

**Request Body:**
```json
{
  "name": "ADMIN",
  "description": "Administrator with full system access"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| name | string | ✅ | 3-50 characters |
| description | string | ❌ | Max 255 characters |

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "name": "ADMIN",
  "description": "Administrator with full system access",
  "accessLevel": "ADMIN"
}
```

**Error Responses:**
- `400` - Invalid input data
- `409` - Role name already exists

---

### 4.2 Get Role by ID

**GET** `/roles/{id}`

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "name": "ADMIN",
  "description": "Administrator with full system access",
  "accessLevel": "ADMIN"
}
```

---

### 4.3 Get Role by Name

**GET** `/roles/name/{name}`

**Response:** Same as Get Role by ID

---

### 4.4 Get All Roles

**GET** `/roles`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "name": "ADMIN",
    "description": "Administrator with full system access",
    "accessLevel": "ADMIN"
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174002",
    "name": "USER",
    "description": "Standard user access",
    "accessLevel": "USER"
  }
]
```

---

### 4.5 Update Role

**PUT** `/roles/{id}`

**Request Body:**
```json
{
  "name": "SUPER_ADMIN",
  "description": "Super administrator with extended privileges"
}
```

**Response:** `200 OK`

**Error Responses:**
- `400` - Invalid input data
- `404` - Role not found
- `409` - Role name already in use

---

### 4.6 Delete Role (Soft Delete)

**DELETE** `/roles/{id}`

**Response:** `204 No Content`

**Error Responses:**
- `404` - Role not found
- `409` - Role is already deleted or is a system role

---

### 4.7 Assign Role to User

Assigns a role to a specific user.

**POST** `/roles/{roleId}/assign/{userId}`

**Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| roleId | UUID | ✅ | Role ID to assign |
| userId | UUID | ✅ | User ID to receive the role |

**Response:** `200 OK`

**Error Responses:**
- `404` - User or role not found
- `409` - User already has this role

---

### 4.8 Remove Role from User

Removes a role from a specific user.

**DELETE** `/roles/{roleId}/remove/{userId}`

**Response:** `200 OK`

**Error Response:** `404` - User, role, or association not found

---

## Error Response Format

All error responses follow this format:

```json
{
  "code": "ERROR_CODE",
  "message": "Human-readable error message",
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/users/register"
}
```

**Validation Error Format:**
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for 1 field(s)",
  "errors": [
    {
      "field": "username",
      "message": "Username must be between 3 and 50 characters"
    }
  ],
  "timestamp": "2024-12-13T10:30:00Z",
  "path": "/api/v1/users/register"
}
```

---

## Supported OAuth2 Providers

| Provider | Login URL |
|----------|-----------|
| Google | `/oauth2/authorization/google` |
| GitHub | `/oauth2/authorization/github` |
| Facebook | `/oauth2/authorization/facebook` |
| Microsoft | `/oauth2/authorization/microsoft` |

---

## Health Check

**GET** `/actuator/health`

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "discoveryComposite": { "status": "UP" }
  }
}
```
