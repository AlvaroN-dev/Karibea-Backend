FROM eclipse-temurin:17-jdk-ubi9-minimal
WORKDIR /app
COPY microservice-review/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
