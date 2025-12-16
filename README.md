# Karibea Backend

> 🛍️ Plataforma de comercio electrónico para ropa y cosméticos con búsqueda y filtrado más rápido e inteligente que los marketplaces tradicionales.

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-brightgreen?style=flat-square&logo=spring)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat-square&logo=docker)](https://www.docker.com/)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-4.0.1-black?style=flat-square&logo=apachekafka)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura](#-arquitectura)
- [Microservicios](#-microservicios)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Configuración](#-configuración)
- [Despliegue](#-despliegue)
- [API Documentation](#-api-documentation)
- [Monitoreo](#-monitoreo)
- [Contribución](#-contribución)

---

## 📖 Descripción

Karibea es una plataforma de e-commerce moderna construida con arquitectura de microservicios. Ofrece:

- ⚡ **Búsqueda Inteligente**: Filtrado rápido y preciso de productos
- 🎯 **Selección por Género**: Navegación organizada e intuitiva
- 🤖 **Chatbot IA**: Recomendaciones personalizadas basadas en estilo, preferencias y presupuesto
- 🔒 **Seguridad Robusta**: Autenticación OAuth2/JWT
- 📊 **Escalabilidad**: Arquitectura distribuida con comunicación asíncrona

---

## 🏗️ Arquitectura

```
                                    ┌─────────────────┐
                                    │   Cliente Web   │
                                    │   / Mobile      │
                                    └────────┬────────┘
                                             │
                                             ▼
                              ┌──────────────────────────┐
                              │     API Gateway          │
                              │     (Port 8080)          │
                              │  ┌────────────────────┐  │
                              │  │ - Load Balancing   │  │
                              │  │ - Rate Limiting    │  │
                              │  │ - Circuit Breaker  │  │
                              │  │ - JWT Validation   │  │
                              │  │ - Swagger Agg.     │  │
                              │  └────────────────────┘  │
                              └───────────┬──────────────┘
                                          │
              ┌───────────────────────────┼───────────────────────────┐
              │                           │                           │
              ▼                           ▼                           ▼
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│   Eureka Server     │    │   Config Server     │    │   Kafka Cluster     │
│   (Port 8761)       │    │   (Port 8888)       │    │   (3 Brokers)       │
│                     │    │                     │    │                     │
│ Service Discovery   │    │ Centralized Config  │    │ Event Streaming     │
└─────────────────────┘    └─────────────────────┘    └─────────────────────┘
              │                           │                           │
              └───────────────────────────┴───────────────────────────┘
                                          │
    ┌─────────────────────────────────────┼─────────────────────────────────────┐
    │         │         │         │       │       │         │         │         │
    ▼         ▼         ▼         ▼       ▼       ▼         ▼         ▼         ▼
┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐
│Catalog│ │Identity│ │ User │ │ Order │ │Payment│ │Inventory│ │Shopcart│ │Shipping│ │ More │
│ 8081  │ │ 8082  │ │ 8083 │ │ 8084  │ │ 8085  │ │  8086  │ │  8087  │ │  8088  │ │ ...  │
└───┬───┘ └───┬───┘ └───┬───┘ └───┬───┘ └───┬───┘ └────┬───┘ └───┬────┘ └───┬────┘ └──────┘
    │         │         │         │         │          │         │          │
    └─────────┴─────────┴─────────┴─────────┴──────────┴─────────┴──────────┘
                                          │
                              ┌───────────┴───────────┐
                              │     PostgreSQL        │
                              │    (14 Databases)     │
                              └───────────────────────┘
```

---

## 🔧 Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **Gateway** | 8080 | Punto de entrada único, enrutamiento, seguridad |
| **Eureka** | 8761 | Descubrimiento y registro de servicios |
| **Config** | 8888 | Configuración centralizada |
| **Catalog** | 8081 | Gestión de productos y categorías |
| **Identity** | 8082 | Autenticación OAuth2/JWT |
| **User** | 8083 | Gestión de usuarios y perfiles |
| **Order** | 8084 | Procesamiento de pedidos |
| **Payment** | 8085 | Integración de pagos (Stripe/PayPal) |
| **Inventory** | 8086 | Control de inventario |
| **Shopcart** | 8087 | Carrito de compras |
| **Shipping** | 8088 | Gestión de envíos |
| **Notification** | 8089 | Notificaciones (Email/SMS/Push) |
| **Marketing** | 8090 | Promociones y campañas |
| **Review** | 8091 | Reseñas y calificaciones |
| **Search** | 8092 | Búsqueda con Elasticsearch |
| **Store** | 8093 | Gestión de tiendas |
| **Chatbot** | 8094 | Asistente IA con OpenAI |

---

## 🛠️ Tecnologías

### Core
- **Java 17** - Lenguaje principal
- **Spring Boot 3.5.8** - Framework base
- **Spring Cloud 2025.0.0** - Microservicios

### Infraestructura
- **Spring Cloud Gateway** - API Gateway reactivo
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - Configuración centralizada
- **Apache Kafka 4.0.1** - Mensajería asíncrona (KRaft mode)

### Datos
- **PostgreSQL 15** - Base de datos relacional
- **Spring Data JPA** - Persistencia
- **Elasticsearch** - Motor de búsqueda

### Seguridad
- **Spring Security 6** - Framework de seguridad
- **OAuth2 Resource Server** - Validación de tokens
- **JWT** - Tokens de acceso

### Documentación
- **SpringDoc OpenAPI 2.8.8** - Generación de API docs
- **Swagger UI** - Interfaz de documentación

### DevOps
- **Docker** - Contenedorización
- **Docker Compose** - Orquestación local

---

## 📦 Requisitos Previos

- **Java 17** o superior
- **Maven 3.8+**
- **Docker** y **Docker Compose**
- **Git**

### Verificar instalación

```bash
java -version    # Java 17+
mvn -version     # Maven 3.8+
docker --version # Docker 20+
docker-compose --version # Docker Compose 2+
```

---

## ⚙️ Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/karibea-backend.git
cd karibea-backend
```

### 2. Configurar variables de entorno

```bash
# Copiar template de variables
cp .env.example .env

# Editar con tus valores
nano .env  # o tu editor preferido
```

### 3. Variables importantes a configurar

```env
# Base de datos
POSTGRES_PASSWORD=tu_password_seguro

# JWT
JWT_SECRET=tu_secret_muy_largo_min_256_bits

# OAuth2 (opcional)
GOOGLE_CLIENT_ID=tu_client_id
GOOGLE_CLIENT_SECRET=tu_client_secret

# Servicios externos (opcional)
STRIPE_API_KEY=sk_test_xxx
OPENAI_API_KEY=sk-xxx
```

---

## 🚀 Despliegue

### Desarrollo Local

```bash
# Iniciar todos los servicios
python run_compose.py dev

# O directamente con docker-compose
docker-compose -f docker-compose.dev.yml up -d
```

### Producción

```bash
# Iniciar en modo producción
python run_compose.py prod

# O directamente
docker-compose -f docker-compose.prod.yml up -d
```

### Verificar servicios

```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs de un servicio
docker-compose logs -f gateway
```

### Orden de inicio

Los servicios se inician automáticamente en el orden correcto:

1. **PostgreSQL** y **Kafka** (infraestructura)
2. **Config Server** (configuración)
3. **Eureka Server** (descubrimiento)
4. **Gateway** y demás microservicios

---

## 📚 API Documentation

### Swagger UI (Agregado)

Una vez iniciados los servicios, accede a la documentación completa:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints de documentación por servicio

| Servicio | Swagger UI | OpenAPI JSON |
|----------|------------|--------------|
| Gateway (Todos) | http://localhost:8080/swagger-ui.html | http://localhost:8080/v3/api-docs |
| Catalog | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| Identity | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |

---

## 📊 Monitoreo

### Endpoints de Health Check

```bash
# Gateway
curl http://localhost:8080/actuator/health

# Eureka Dashboard
http://localhost:8761

# Servicios individuales
curl http://localhost:808X/actuator/health
```

### Métricas Prometheus (si está configurado)

```
http://localhost:8080/actuator/prometheus
```

---

## 📁 Estructura del Proyecto

```
karibea-backend/
├── 📄 docker-compose.dev.yml    # Compose para desarrollo
├── 📄 docker-compose.prod.yml   # Compose para producción
├── 📄 .env.example              # Template de variables
├── 📄 init.sql                  # Inicialización de BD
├── 📄 pom.xml                   # POM padre
│
├── 📁 docs/                     # Documentación
│   └── ARCHITECTURE.md
│
├── 📁 microservice-gateway/     # API Gateway
├── 📁 microservice-eureka/      # Service Discovery
├── 📁 microservice-config/      # Config Server
│   └── src/main/resources/config/  # Configs centralizadas
│
├── 📁 microservice-catalog/     # Productos
├── 📁 microservice-identity/    # Auth/OAuth2
├── 📁 microservice-user/        # Usuarios
├── 📁 microservice-order/       # Pedidos
├── 📁 microservice-payment/     # Pagos
├── 📁 microservice-inventory/   # Inventario
├── 📁 microservice-shopcart/    # Carrito
├── 📁 microservice-shipping/    # Envíos
├── 📁 microservice-notification/# Notificaciones
├── 📁 microservice-marketing/   # Marketing
├── 📁 microservice-review/      # Reseñas
├── 📁 microservice-search/      # Búsqueda
├── 📁 microservice-store/       # Tiendas
└── 📁 microservice-chatbot/     # Chatbot IA
```

---

## 🔐 Seguridad

### Autenticación

```bash
# Obtener token
POST http://localhost:8080/api/identity/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Usar token en requests

```bash
curl -H "Authorization: Bearer {token}" \
     http://localhost:8080/api/catalog/products
```

---

## 🧪 Testing

### Ejecutar tests unitarios

```bash
# Todos los servicios
mvn test

# Servicio específico
cd microservice-catalog
mvn test
```

### Tests de integración

```bash
mvn verify -Pintegration-tests
```

---

## 📝 Contribución

1. Fork el repositorio
2. Crea una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Add: nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 👥 Equipo

Desarrollado con ❤️ por el equipo de Karibea.

---

## 📞 Soporte

- 📧 Email: soporte@karibea.com
- 📖 Documentación: [docs/](docs/)
- 🐛 Issues: [GitHub Issues](https://github.com/tu-usuario/karibea-backend/issues)
