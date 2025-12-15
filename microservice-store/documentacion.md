# Documentación de Microservicio Store

## Endpoints

### 1. Crear Tienda (Store)
**POST** `/stores`

**Request Body:**
```json
{
  "externalOwnerUserId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Mi Tienda",
  "description": "La mejor tienda del mundo",
  "email": "contacto@mitienda.com",
  "phone": "+1234567890",
  "logoUrl": "http://example.com/logo.png",
  "bannerUrl": "http://example.com/banner.png"
}
```

**Response (201 Created):**
```json
{
  "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "externalOwnerUserId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Mi Tienda",
  "description": "La mejor tienda del mundo",
  "email": "contacto@mitienda.com",
  "phone": "+1234567890",
  "logoUrl": "http://example.com/logo.png",
  "bannerUrl": "http://example.com/banner.png",
  "status": {
      "id": "b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22",
      "name": "PENDING",
      "verificationStatus": "UNVERIFIED",
      "description": "Store is pending verification"
  },
  "addresses": [],
  "settings": null
}
```

### 2. Obtener Tienda por ID
**GET** `/stores/{id}`

**Response (200 OK):**
```json
{
  "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "externalOwnerUserId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Mi Tienda",
  "description": "La mejor tienda del mundo",
  "email": "contacto@mitienda.com",
  "phone": "+1234567890",
  "logoUrl": "http://example.com/logo.png",
  "bannerUrl": "http://example.com/banner.png",
  "status": {
      "id": "b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22",
      "name": "ACTIVE",
      "verificationStatus": "VERIFIED",
      "description": "Store is active"
  },
  "addresses": [
      {
          "id": "c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33",
          "addressType": "BILLING",
          "streetAddress": "123 Calle Principal",
          "city": "Ciudad",
          "state": "Estado",
          "postalCode": "12345",
          "country": "Pais",
          "primary": true
      }
  ],
  "settings": {
      "id": "d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a44",
      "returnPolicy": "30 days return",
      "shippingPolicy": "Free shipping",
      "minOrderAmount": 10.00,
      "acceptsReturns": true,
      "returnWindowDays": 30
  }
}
```

### 3. Listar Tiendas
**GET** `/stores`

**Response (200 OK):**
```json
[
  {
    "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
    "name": "Mi Tienda",
    "email": "contacto@mitienda.com",
    "addresses": [...],
    "settings": {...}
    ...
  }
]
```

### 4. Actualizar Tienda
**PUT** `/stores/{id}`

**Request Body:**
```json
{
  "name": "Mi Tienda Actualizada",
  "description": "Nueva descripcion",
  "email": "nuevo@mitienda.com",
  "phone": "+0987654321",
  "logoUrl": "http://example.com/new_logo.png",
  "bannerUrl": "http://example.com/new_banner.png"
}
```

### 5. Eliminar Tienda
**DELETE** `/stores/{id}`

**Response (204 No Content)**
