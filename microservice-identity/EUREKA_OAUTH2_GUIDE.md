# Configuración de Eureka y OAuth2

## Resumen

Se ha agregado configuración completa para:

1. **Eureka Client**: Registro del microservicio en Eureka Server
2. **OAuth2 Multi-Platform**: Autenticación con Google, GitHub y Facebook
3. **Integración con JWT**: OAuth2 y JWT coexisten para diferentes casos de uso

---

## Configuración de Eureka Client

### En `application.yaml`

```yaml
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
    prefer-ip-address: true
    metadata-map:
      version: 1.0.0
      description: Identity and Authentication Service
```

### Variables de Entorno

Puedes configurar la URL del servidor Eureka con:
```bash
export EUREKA_SERVER_URL=http://eureka-server:8761/eureka/
```

O en tu `.env.dev`:
```
EUREKA_SERVER_URL=http://localhost:8761/eureka/
```

---

## Configuración de OAuth2

### Proveedores Soportados

1. **Google OAuth2**
2. **GitHub OAuth2**
3. **Facebook OAuth2**

### Variables de Entorno Requeridas

Debes configurar las credenciales de OAuth2 para cada proveedor:

```bash
# Google
export GOOGLE_CLIENT_ID=tu-google-client-id
export GOOGLE_CLIENT_SECRET=tu-google-client-secret

# GitHub
export GITHUB_CLIENT_ID=tu-github-client-id
export GITHUB_CLIENT_SECRET=tu-github-client-secret

# Facebook
export FACEBOOK_CLIENT_ID=tu-facebook-client-id
export FACEBOOK_CLIENT_SECRET=tu-facebook-client-secret
```

### Cómo Obtener Credenciales

#### Google OAuth2
1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Habilita "Google+ API"
4. Ve a "Credenciales" → "Crear credenciales" → "ID de cliente de OAuth 2.0"
5. Configura las URIs de redirección autorizadas:
   - `http://localhost:8080/login/oauth2/code/google`
6. Copia el Client ID y Client Secret

#### GitHub OAuth2
1. Ve a [GitHub Developer Settings](https://github.com/settings/developers)
2. Click en "New OAuth App"
3. Configura:
   - Application name: Tu nombre de app
   - Homepage URL: `http://localhost:8080`
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copia el Client ID y genera un Client Secret

#### Facebook OAuth2
1. Ve a [Facebook Developers](https://developers.facebook.com/)
2. Crea una nueva app
3. Agrega "Facebook Login" como producto
4. Configura las URIs de redirección válidas:
   - `http://localhost:8080/login/oauth2/code/facebook`
5. Copia el App ID (Client ID) y App Secret (Client Secret)

---

## Arquitectura de la Solución

### Capa de Dominio
- **Sin cambios**: El dominio permanece independiente de OAuth2
- Los modelos de dominio (`User`, `Role`) no conocen OAuth2

### Capa de Aplicación
- **Pendiente**: Crear servicio para procesar usuarios OAuth2
- Mapear información de OAuth2 a modelos de dominio
- Generar JWT para usuarios autenticados con OAuth2

### Capa de Infraestructura
- **`SecurityConfig`**: Configuración de seguridad con OAuth2 y JWT
- **`OAuth2Controller`**: Maneja callbacks de OAuth2 (success/failure)
- **`application.yaml`**: Configuración de proveedores OAuth2

---

## Flujos de Autenticación

### Flujo 1: Autenticación JWT (API)
```
Cliente → POST /api/v1/auth/login → Servidor
Servidor → Valida credenciales → Genera JWT
Servidor → Retorna JWT al cliente
Cliente → Usa JWT en header Authorization para requests
```

### Flujo 2: Autenticación OAuth2 (Web)
```
Usuario → Click "Login con Google"
Navegador → Redirige a Google
Google → Usuario autoriza
Google → Redirige a /login/oauth2/code/google
Servidor → Procesa callback OAuth2
Servidor → Crea/actualiza usuario
Servidor → Redirige a /api/v1/auth/oauth2/success
Servidor → Retorna información del usuario + JWT
```

---

## Endpoints

### Endpoints Públicos

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Login tradicional con email/password |
| `/api/v1/auth/register` | POST | Registro de nuevo usuario |
| `/login/oauth2/code/google` | GET | Callback de Google OAuth2 |
| `/login/oauth2/code/github` | GET | Callback de GitHub OAuth2 |
| `/login/oauth2/code/facebook` | GET | Callback de Facebook OAuth2 |
| `/api/v1/auth/oauth2/success` | GET | Callback exitoso de OAuth2 |
| `/api/v1/auth/oauth2/failure` | GET | Callback fallido de OAuth2 |

### Endpoints Protegidos

Todos los demás endpoints requieren autenticación (JWT o sesión OAuth2).

---

## Uso desde el Frontend

### Login con Google (Ejemplo React)

```javascript
// Botón de login con Google
<button onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}>
  Login con Google
</button>
```

### Login con GitHub

```javascript
<button onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/github'}>
  Login con GitHub
</button>
```

### Login con Facebook

```javascript
<button onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/facebook'}>
  Login con Facebook
</button>
```

---

## Seguridad y Mejores Prácticas

### CORS
- Configurado para permitir orígenes específicos
- Actualiza `SecurityConfig.corsConfigurationSource()` con tus URLs de frontend

### Sesiones
- **API endpoints**: Stateless (JWT)
- **OAuth2 login**: Puede usar sesiones temporales durante el flujo OAuth2

### Tokens
- **JWT**: Para autenticación API
- **OAuth2 Access Token**: Usado internamente durante el flujo OAuth2

---

## Próximos Pasos (TODO)

1. **Implementar servicio OAuth2 en capa de aplicación**:
   - Crear `OAuth2UserService` para procesar usuarios OAuth2
   - Mapear información de OAuth2 a modelo de dominio `User`
   - Generar JWT después de autenticación OAuth2 exitosa

2. **Persistencia de usuarios OAuth2**:
   - Crear o actualizar usuario en base de datos
   - Vincular cuenta OAuth2 con cuenta existente (opcional)

3. **Testing**:
   - Probar flujo OAuth2 con cada proveedor
   - Verificar generación de JWT
   - Probar integración con frontend

4. **Producción**:
   - Configurar HTTPS (requerido por OAuth2)
   - Actualizar redirect URIs en proveedores OAuth2
   - Configurar variables de entorno en servidor

---

## Verificación

### 1. Verificar Registro en Eureka

```bash
# Inicia el servidor Eureka (si no está corriendo)
# Luego inicia este microservicio

# Verifica en http://localhost:8761
# Deberías ver "MICROSERVICE-IDENTITY" registrado
```

### 2. Verificar OAuth2

```bash
# Configura las variables de entorno
export GOOGLE_CLIENT_ID=tu-client-id
export GOOGLE_CLIENT_SECRET=tu-client-secret

# Inicia el microservicio
mvn spring-boot:run

# Navega a: http://localhost:8080/oauth2/authorization/google
# Deberías ser redirigido a Google para autenticación
```

---

## Troubleshooting

### Error: "redirect_uri_mismatch"
- Verifica que las URIs de redirección en el proveedor OAuth2 coincidan exactamente
- Formato correcto: `http://localhost:8080/login/oauth2/code/{provider}`

### Error: "Eureka server not found"
- Verifica que el servidor Eureka esté corriendo en `http://localhost:8761`
- Puedes deshabilitar Eureka temporalmente con: `eureka.client.enabled=false`

### Error: "OAuth2 authentication failed"
- Verifica que las credenciales (Client ID y Secret) sean correctas
- Revisa los logs para más detalles del error

---

## Principios SOLID y Arquitectura Hexagonal

### ✅ Cumplimiento

- **Domain Layer**: Sin dependencias de OAuth2 o Eureka
- **Application Layer**: Lógica de negocio para procesar usuarios OAuth2
- **Infrastructure Layer**: Adaptadores para OAuth2 y Eureka

### Separación de Responsabilidades

- **SecurityConfig**: Solo configuración de seguridad
- **OAuth2Controller**: Solo manejo de callbacks HTTP
- **OAuth2UserService** (pendiente): Solo lógica de procesamiento de usuarios

---

## Referencias

- [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [Spring Cloud Netflix Eureka](https://spring.io/projects/spring-cloud-netflix)
- [Google OAuth2](https://developers.google.com/identity/protocols/oauth2)
- [GitHub OAuth2](https://docs.github.com/en/developers/apps/building-oauth-apps)
- [Facebook Login](https://developers.facebook.com/docs/facebook-login)
