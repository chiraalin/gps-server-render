# Etapa 1: Build cu Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagine finală cu .jar-ul normal
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/gps-server-render-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
