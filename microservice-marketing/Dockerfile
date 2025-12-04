FROM eclipse-temurin:17-jdk-ubi9-minimal
WORKDIR /app
COPY microservice-marketing/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
