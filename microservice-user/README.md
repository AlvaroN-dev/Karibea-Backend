# Microservice User

Microservicio para la gestión de perfiles de usuario, direcciones, preferencias y catálogos.

## Arquitectura

- **Arquitectura Hexagonal** (Ports & Adapters)
- **Domain-Driven Design** (DDD)
- **Event-Driven** con Apache Kafka
- **OAuth2/JWT** para autenticación

## Estructura del Proyecto

```
com.microservice.user
├── application/
│   ├── dto/
│   │   ├── request/     # DTOs de entrada
│   │   └── response/    # DTOs de salida
│   ├── exception/       # Excepciones de aplicación
│   ├── mapper/          # Mappers DTO <-> Domain
│   ├── services/        # Servicios de aplicación
│   └── usecases/        # Implementación de casos de uso
├── domain/
│   ├── events/          # Eventos de dominio
│   ├── exceptions/      # Excepciones de dominio
│   ├── models/          # Modelos de dominio
│   ├── port/
│   │   ├── in/          # Puertos de entrada (use cases)
│   │   └── out/         # Puertos de salida (repositories, services)
│   └── service/         # Servicios de dominio
└── infrastructure/
    ├── adapters/
    │   ├── external/    # Adaptadores externos
    │   └── persistence/ # Adaptadores de persistencia
    ├── config/          # Configuraciones
    ├── controller/      # Controladores REST
    ├── entities/        # Entidades JPA
    ├── exceptions/      # Manejo de excepciones
    ├── kafka/
    │   ├── config/      # Configuración Kafka
    │   ├── consumer/    # Consumidores de eventos
    │   ├── events/      # DTOs de eventos Kafka
    │   └── producer/    # Productores de eventos
    └── repositories/    # Repositorios JPA
```

## Base de Datos

### Tablas Principales

| Tabla | Descripción |
|-------|-------------|
| `user_profiles` | Perfiles de usuario |
| `address` | Direcciones de usuarios |
| `user_preferences` | Preferencias de usuario |
| `genders` | Catálogo de géneros |
| `currency` | Catálogo de monedas |
| `lenguaje` | Catálogo de idiomas |

## API Endpoints

### User Profiles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/profiles` | Crear perfil |
| `GET` | `/api/v1/profiles/{id}` | Obtener por ID |
| `GET` | `/api/v1/profiles/user/{externalUserId}` | Obtener por user externo |
| `GET` | `/api/v1/profiles` | Listar con paginación |
| `PUT` | `/api/v1/profiles/{id}` | Actualizar perfil |
| `DELETE` | `/api/v1/profiles/{id}` | Eliminar perfil |

#### Crear Perfil
```bash
POST /api/v1/profiles
Authorization: Bearer <token>
Content-Type: application/json

{
  "externalUserId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "middleName": "Michael",
  "secondLastname": "García",
  "phone": "+1-555-123-4567",
  "genderId": "550e8400-e29b-41d4-a716-446655440001",
  "avatarUrl": "https://example.com/avatars/johndoe.jpg",
  "dateOfBirth": "1990-05-15"
}
```

#### Respuesta
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "externalUserId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "middleName": "Michael",
  "secondLastname": "García",
  "fullName": "John Michael Doe García",
  "phone": "+1-555-123-4567",
  "gender": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Male"
  },
  "avatarUrl": "https://example.com/avatars/johndoe.jpg",
  "dateOfBirth": "1990-05-15",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Addresses

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/addresses` | Crear dirección |
| `GET` | `/api/v1/addresses/{id}` | Obtener por ID |
| `GET` | `/api/v1/addresses/user/{externalUserId}` | Listar por usuario |
| `PUT` | `/api/v1/addresses/{id}` | Actualizar dirección |
| `PATCH` | `/api/v1/addresses/{id}/default` | Establecer como default |
| `DELETE` | `/api/v1/addresses/{id}` | Eliminar dirección |

#### Crear Dirección
```bash
POST /api/v1/addresses
Authorization: Bearer <token>
Content-Type: application/json

{
  "externalUserId": "550e8400-e29b-41d4-a716-446655440000",
  "label": "Home",
  "streetAddress": "123 Main Street, Apt 4B",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "United States",
  "isDefault": true
}
```

### Preferences

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/preferences/{externalUserId}` | Crear preferencias |
| `GET` | `/api/v1/preferences/{externalUserId}` | Obtener preferencias |
| `PUT` | `/api/v1/preferences` | Actualizar preferencias |
| `DELETE` | `/api/v1/preferences/{externalUserId}` | Eliminar preferencias |

#### Actualizar Preferencias
```bash
PUT /api/v1/preferences
Authorization: Bearer <token>
Content-Type: application/json

{
  "externalUserId": "550e8400-e29b-41d4-a716-446655440000",
  "languageId": "550e8400-e29b-41d4-a716-446655440002",
  "currencyId": "550e8400-e29b-41d4-a716-446655440003",
  "notificationEmail": true,
  "notificationPush": true
}
```

### Catalogs (Públicos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/catalogs/genders` | Listar géneros |
| `GET` | `/api/v1/catalogs/currencies` | Listar monedas |
| `GET` | `/api/v1/catalogs/languages` | Listar idiomas |

## Eventos Kafka

### Eventos Consumidos (desde microservice-identity)

**Topic:** `identity-events`

| Evento | Acción |
|--------|--------|
| `UserCreated` | Actualiza cache local de usuarios |
| `UserUpdated` | Actualiza cache local de usuarios |
| `UserDeleted` | Elimina perfil y datos asociados |

### Eventos Publicados

**Topic:** `user.profile.events`

| Evento | Descripción |
|--------|-------------|
| `UserProfileCreated` | Perfil de usuario creado |
| `UserProfileUpdated` | Perfil de usuario actualizado |
| `UserProfileDeleted` | Perfil de usuario eliminado |
| `PreferencesUpdated` | Preferencias actualizadas |

## Configuración

### Variables de Entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/karibea_user` | URL de base de datos |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de BD |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Contraseña de BD |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `kafka-0:9092` | Servidores Kafka |
| `OAUTH2_ISSUER_URI` | `http://microservice-identity:8080` | URI del emisor JWT |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://microservice-eureka:8761/eureka` | URL de Eureka |
| `CONFIG_SERVER_URI` | `http://microservice-config:8888` | URL del Config Server |

## Desarrollo

### Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 14+ (o H2 para desarrollo)
- Apache Kafka

### Compilar
```bash
./mvnw clean compile
```

### Ejecutar Tests
```bash
./mvnw test
```

### Ejecutar Localmente
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Documentación API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Seguridad

- Todos los endpoints excepto `/api/v1/catalogs/**` requieren autenticación JWT
- El token JWT se obtiene desde `microservice-identity`
- Los claims del token incluyen roles para autorización

## Decisiones Arquitectónicas

### Event-Driven vs REST

| Escenario | Decisión |
|-----------|----------|
| Verificación de usuario | Event-Driven (cache local via Kafka) |
| Comunicación con Identity | Eventos Kafka (no WebClient) |
| API pública | REST con JWT |

### Justificación

1. **Eliminación de WebClient para Identity**: El JWT validado por Spring Security ya confirma la identidad del usuario. No es necesario verificar síncronamente con microservice-identity.

2. **Cache local de usuarios**: Los eventos de Kafka mantienen un cache actualizado de usuarios conocidos, eliminando la necesidad de llamadas HTTP síncronas.

3. **Consistencia eventual aceptable**: En perfiles de usuario, la consistencia eventual es aceptable. Los cambios se propagan vía eventos.

## Changelog

### v1.0.0
- Arquitectura hexagonal completa
- Event-driven con Kafka
- Eliminación de WebClient/WebFlux
- Documentación Swagger/OpenAPI
- DTOs con @Schema annotations
