FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .env.docker /app/.env
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/conexa-scheduling-0.0.1-SNAPSHOT.jar app.jar
COPY --from=builder /app/.env /app/.env

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
