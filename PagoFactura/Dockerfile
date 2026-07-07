# 1. Imagen base con Java 21 (vimos en tus logs que compila con release 21)
FROM openjdk:21-jdk-slim

# 2. Copia el archivo .jar recién generado en target dentro de la imagen
COPY target/*.jar app.jar

# 3. Instala curl para activar los Healthchecks que controlan el orden de tus etapas
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 4. Comando para arrancar el microservicio
ENTRYPOINT ["java", "-jar", "/app.jar"]