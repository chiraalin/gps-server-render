# Imagine de bază cu Maven + JDK 21
FROM maven:3.9.6-eclipse-temurin-21

# Setează directorul de lucru în container
WORKDIR /app

# Copiază toate fișierele în container
COPY . .

# Compilează și creează JAR-ul shaded
RUN mvn clean package -e

# Rulează aplicația folosind JAR-ul generat de shade plugin
CMD ["java", "-jar", "target/gps-server-render-1.0-SNAPSHOT-shaded.jar"]
