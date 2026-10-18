
# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

COPY . .

# Make the Gradle wrapper executable on Linux
RUN chmod +x gradlew

# Build the executable Spring Boot JAR
RUN ./gradlew clean bootJar -x test --no-daemon

# Select the executable JAR, excluding the plain JAR
RUN set -eux; \
    JAR="$(find build/libs -maxdepth 1 -type f -name '*.jar' ! -name '*-plain.jar' | head -n 1)"; \
    test -n "$JAR"; \
    cp "$JAR" app.jar

# Stage 2: Run the application with Java 17
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /workspace/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
