# Stage 1: Use Maven for building
FROM maven:3.9.10-eclipse-temurin-21 as build
WORKDIR /app
COPY ./discoverserver ./discoverserver
WORKDIR /app/discoverserver
RUN mvn clean package -DskipTests

# Stage 2: Use OpenJDK for running
FROM eclipse-temurin:21-jre
WORKDIR /app
# Ensure the correct file extension for the JAR file (e.g., 'discoverserver-1.0.jar')
COPY --from=build /app/discoverserver/target/*.jar discoverserver.jar
EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "discoverserver.jar"]
