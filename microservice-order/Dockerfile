FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY microservice-order/mvnw .
COPY microservice-order/.mvn .mvn
COPY microservice-order/pom.xml .
COPY pom.xml ../pom.xml

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B || true

COPY microservice-order/src ./src
RUN ./mvnw clean package -DskipTests -B -U

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R spring:spring /app

USER spring

EXPOSE 8084

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health >/dev/null 2>&1 || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -Dspring.profiles.active=docker"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
