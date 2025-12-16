# =============================================================================
# MICROSERVICE-GATEWAY DOCKERFILE
# API Gateway with Spring Cloud Gateway
# =============================================================================

# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY microservice-gateway/mvnw .
COPY microservice-gateway/.mvn .mvn
COPY microservice-gateway/pom.xml .
COPY pom.xml ../pom.xml

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B || true

# Copy source code
COPY microservice-gateway/src src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Add labels for container management
LABEL maintainer="Karibea Team"
LABEL service="microservice-gateway"
LABEL description="API Gateway - Central Entry Point"

# Create non-root user for security
RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring

WORKDIR /app

# Copy the built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose Gateway port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers - Gateway needs more memory for routing
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=docker \
    -Dreactor.netty.http.server.accessLogEnabled=true"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
