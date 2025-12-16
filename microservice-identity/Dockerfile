# =============================================================================
# MICROSERVICE-IDENTITY DOCKERFILE
# OAuth2 Authorization Server / Identity Service
# =============================================================================

# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY microservice-identity/mvnw .
COPY microservice-identity/.mvn .mvn
COPY microservice-identity/pom.xml .
COPY pom.xml ../pom.xml

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B || true

COPY microservice-identity/src src
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Karibea Team"
LABEL service="microservice-identity"

RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 9000

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:9000/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
