FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY microservice-marketing/mvnw .
COPY microservice-marketing/.mvn .mvn
COPY microservice-marketing/pom.xml .
COPY pom.xml ../pom.xml

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B || true

COPY microservice-marketing/src ./src
RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache wget

RUN addgroup -g 1001 -S spring && adduser -u 1001 -S spring -G spring

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R spring:spring /app

USER spring

EXPOSE 8090

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8090/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -Dspring.profiles.active=docker"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
