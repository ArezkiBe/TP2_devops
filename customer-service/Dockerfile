# Build stage — uses root Gradle wrapper via docker-compose context (.)
FROM amazoncorretto:17 AS builder
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle
COPY customer-service/ customer-service/
RUN chmod +x ./gradlew
RUN ./gradlew :customer-service:clean :customer-service:build -x test

# Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/customer-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
