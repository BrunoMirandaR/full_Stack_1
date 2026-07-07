FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# AQUÍ INSTALAMOS NETCAT PARA EL SECUENCIADOR:
RUN apt-get update && apt-get install -y curl netcat-openbsd && rm -rf /var/lib/apt/lists/*
ENTRYPOINT ["java", "-jar", "app.jar"]