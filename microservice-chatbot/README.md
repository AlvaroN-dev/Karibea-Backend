# Microservice Chatbot - RAG with Hexagonal Architecture

A production-ready RAG (Retrieval-Augmented Generation) chatbot microservice built with Spring Boot 3, implementing Hexagonal Architecture and DDD principles.

## 🏗️ Architecture

```
com.microservice.chatbot
├── application          # Use cases, DTOs, mappers, services, exceptions
│   ├── dto              # Request/Response objects
│   ├── exception        # Application-level exceptions
│   ├── mapper           # MapStruct mappers
│   ├── services         # Orchestration services
│   └── usecases         # Use case implementations
├── domain               # Business logic, ports, models
│   ├── events           # Domain events
│   ├── exceptions       # Domain exceptions
│   ├── models           # Domain entities & value objects
│   └── port             # Hexagonal ports (in/out)
└── infrastructure       # External adapters, controllers, config
    ├── adapters         # AI provider, retriever implementations
    ├── config           # Spring configuration
    ├── controller       # REST controllers
    ├── entities         # JPA entities
    ├── exceptions       # Exception handlers
    ├── kafka            # Event publishing
    └── repositories     # JPA repositories & adapters
```

## 🚀 Features

- **RAG Implementation**: Context retrieval from database → Prompt construction → AI response
- **Provider-Agnostic AI**: Swap between AI21 Studio, OpenAI, or any provider via configuration
- **Hexagonal Architecture**: Clean separation between domain, application, and infrastructure
- **Circuit Breaker**: Resilience4j for fault tolerance
- **Conversation Persistence**: Full message history with UUID primary keys
- **Human Escalation**: Escalate complex issues to human agents
- **Kafka Events**: Publish domain events for async processing

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chat/messages` | Send message and get AI response |
| GET | `/api/chat/conversations/{id}` | Get conversation with messages |
| POST | `/api/chat/conversations` | Create new conversation |
| GET | `/api/chat/sources` | Get available RAG sources |
| POST | `/api/chat/escalate` | Escalate to human agent |

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `AI_PROVIDER` | AI provider (`ai21` or `openai_stub`) | `ai21` |
| `CHATBOT_AI21_API_KEY` | AI21 Studio API key | - |
| `CHATBOT_AI21_MODEL` | AI21 model name | `jamba-mini-1.7` |
| `DB_URL` | PostgreSQL connection URL | - |
| `DB_USERNAME` | Database username | - |
| `DB_PASSWORD` | Database password | - |

### Profiles

- `dev`: Verbose logging, H2/local DB, stub provider available
- `prod`: Minimal logging, strict circuit breaker settings

## 🛠️ Running Locally

```bash
# With Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# With environment variables
export CHATBOT_AI21_API_KEY=your_key
export DB_URL=jdbc:postgresql://localhost:5432/chatbot_db
mvn spring-boot:run
```

## 📦 Dependencies

- Spring Boot 3.5.8
- Spring Data JPA (PostgreSQL)
- Spring Kafka
- Springdoc OpenAPI
- Resilience4j (Circuit Breaker)
- MapStruct
- Lombok

## 🔄 RAG Flow

```
User Message → ChatController → SendMessageUseCase → ChatOrchestrationService
    ↓
Sanitize Input → Retrieve Context → Build Prompt → AI Provider → Normalize Response
    ↓
Save Messages → Publish Events → Return Response
```

## 📖 API Documentation

Swagger UI available at: `http://localhost:8087/api/swagger-ui.html`

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## 📝 License

Apache 2.0
