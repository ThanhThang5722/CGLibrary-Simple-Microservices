# Use Maven for building
FROM maven:3.9.10-eclipse-temurin-21 as build

# Set working directory
WORKDIR /app

# Copy commonservice and bookservice
COPY ./commonservice ./commonservice
COPY ./bookservice ./bookservice

# Build commonservice first
WORKDIR /app/commonservice
RUN mvn clean install -DskipTests

# Build bookservice
WORKDIR /app/bookservice
RUN mvn clean package -DskipTests

# Use OpenJDK for running
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/bookservice/target/*.jar bookservice.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "bookservice.jar"]