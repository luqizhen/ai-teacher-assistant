# Use Maven image for building
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK runtime image
FROM eclipse-temurin:21-jre-alpine

# Install curl for health check
RUN apk add --no-cache curl

WORKDIR /app

# Copy the built jar file
COPY --from=build /app/target/piano-teacher-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
