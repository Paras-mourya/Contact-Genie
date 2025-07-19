# Use official lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory in container
WORKDIR /app

# Copy everything into container
COPY . .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build the project using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Run the generated JAR file
CMD ["java", "-jar", "target/smart-contract-0.0.1-SNAPSHOT.jar"]
