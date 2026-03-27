FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", \
  "-Dspring.data.mongodb.uri=mongodb+srv://mendoza_db_user:iS4BXTZmC4z9nKXX@franchise-cluster.udhr2xv.mongodb.net/franchisedb?retryWrites=true&w=majority&appName=franchise-cluster", \
  "-jar", "app.jar"]