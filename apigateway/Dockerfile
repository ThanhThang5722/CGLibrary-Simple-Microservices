# Stage 1: Use Maven for building
FROM maven:3.9.10-eclipse-temurin-21 as build
WORKDIR /app

COPY ./apigateway ./apigateway

WORKDIR /app/apigateway
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/apigateway/target/*.jar apigateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","apigateway.jar"]