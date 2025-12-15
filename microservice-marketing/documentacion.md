# Documentación de API - Microservice Marketing

Este microservicio se encarga de la gestión de promociones, cupones y ventas flash.
Todos los identificadores (ID) utilizan el formato **UUID**.

## Endpoints

### Promociones (`/api/promotions`)

#### Crear Promoción
- **POST** `/api/promotions`
- **Request Body**: `Promotion`
- **Response**: `PromotionDTO`

**Ejemplo JSON Response**:
```json
{
  "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "name": "Summer Sale",
  "description": "Discount for summer items",
  "promotionType": "PERCENTAGE",
  "discountValue": 15.0,
  "startedAt": "2023-01-01T10:00:00",
  "endedAt": "2023-01-31T23:59:59",
  "isActive": true
}
```

#### Obtener Promoción por ID
- **GET** `/api/promotions/{id}`
- **Response**: `PromotionDTO`

### Cupones (`/api/coupons`)

#### Crear Cupón
- **POST** `/api/coupons`
- **Request Body**: `Coupon` (Se puede enviar `promotionId` o `promotion` object)
- **Response**: `CouponDTO` (Incluye objeto `promotion` completo)

**Ejemplo JSON Response**:
```json
{
  "id": "b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22",
  "promotion": {
    "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
    "name": "Summer Sale",
    "discountValue": 15.0,
    ...
  },
  "code": "SUMMER15",
  "usageLimit": 100,
  "usageCount": 5,
  "isActive": true
}
```

#### Usar Cupón
- **POST** `/api/coupons/use`
- **Params**: `code`, `userId`, `orderId`, `discountAmount`
- **Response**: `CouponUsageDTO`

### Ventas Flash (`/api/flash-sales`)

#### Crear Venta Flash
- **POST** `/api/flash-sales`
- **Response**: `FlashSaleDTO`

**Ejemplo JSON Response**:
```json
{
  "id": "c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33",
  "name": "Black Friday Flash Sale",
  "products": [
    {
      "id": "d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a44",
      "flashSaleId": "c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33",
      "externalProductId": "prod-123",
      "salePrice": 99.99
    }
  ]
}
```

## Configuración de UUID
El proyecto ha sido refactorizado para usar `java.util.UUID` en lugar de `Long` para todas las entidades.

## Swagger UI
La documentación interactiva está disponible en:
- `http://localhost:8080/swagger-ui/index.html` (o puerto configurado)
- `http://localhost:8080/v3/api-docs`
