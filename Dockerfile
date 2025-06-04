
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src


# Package the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jdk-alpine AS runner

# Create non-root user (optional but more secure)
#RUN useradd -ms /bin/bash springuser

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Set permissions (optional)
#RUN chown -R springuser:springuser /app

# Use non-root user
#USER springuser

# Expose app port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
