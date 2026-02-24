# Use lightweight Java image
FROM eclipse-temurin:21-jdk-alpine

# Create app directory
WORKDIR /opt

# Copy project files
COPY . .

# Build the jar using Maven wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","target/Foodies-0.0.1-SNAPSHOT.jar"]
