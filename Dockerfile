# Etapa 1: Build cu Maven și JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiem toate fișierele în container
COPY . .

# Curățăm build-ul anterior și generăm JAR-ul "shaded"
RUN mvn clean verify

# Etapa 2: Imagine finală cu doar JAR-ul rezultat
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiem doar JAR-ul final din build-ul anterior
COPY --from=build /app/target/gps-server-render-1.0-SNAPSHOT-shaded.jar /app/app.jar

# Punctul de pornire al aplicației
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
