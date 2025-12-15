FROM eclipse-temurin:17-jdk-ubi9-minimal
WORKDIR /app
COPY microservice-card/target/*.jar app.jar
COPY microservice-shopcart/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
