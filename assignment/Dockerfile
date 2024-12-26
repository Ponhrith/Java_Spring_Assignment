# Use an OpenJDK image as the base image
FROM openjdk:23-slim AS base

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY target/assignment-0.0.1-SNAPSHOT.jar assignment.jar

EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "assignment.jar"]
