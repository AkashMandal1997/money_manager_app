# Multi-stage build for Render deployment
# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src src

# Build the application using Maven (Maven is included in JDK image)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager.jar

# Render provides PORT environment variable
ENV PORT=8080
EXPOSE ${PORT}

# Run the application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar moneymanager.jar"]