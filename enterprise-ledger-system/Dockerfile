# STAGE 1: Build the application
# We use a heavy image with Maven to compile the code
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy only the POM first to cache dependencies (Massive speed boost for rebuilds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the JAR, skipping tests for deployment speed
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run the application
# We use a tiny, secure JRE image that only knows how to run Java
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Extract ONLY the compiled JAR from Stage 1
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]