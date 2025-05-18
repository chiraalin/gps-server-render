FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/gps-server-render-1.0-SNAPSHOT-shaded.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
