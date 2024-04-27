

FROM amazoncorretto:17-alpine-jdk AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle Wrapper files
COPY gradlew /app/
COPY gradle /app/gradle
COPY build.gradle.kts /app/
COPY settings.gradle.kts /app/

# Copy the source code
COPY src /app/src

# Grant execution rights on the Gradle Wrapper
RUN chmod +x ./gradlew

# Build the application using the Gradle Wrapper
RUN ./gradlew clean build 
RUN ./gradlew bootJar

# Start a new stage for the final image
FROM amazoncorretto:17-alpine-jdk

# Set the working directory in the container
WORKDIR /app

# Install the MySQL client
RUN apk add --no-cache mysql-client

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar ./app.jar

# List the contents of the /app directory

# Copy the entrypoint script
COPY entrypoint.sh /app/
#COPY flyway-container.conf /app/

COPY src/main/resources/db/migration /app/db/migration
RUN ls -la /app >./app.txt

RUN chmod +x /app/entrypoint.sh

# Run the web service on container startup
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=container"]
#ENTRYPOINT ["/app/entrypoint.sh"]
