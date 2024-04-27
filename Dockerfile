

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
COPY entrypoint.sh /app/


# Grant execution rights on the Gradle Wrapper
RUN chmod +x ./gradlew
# Copy the health-check.sh script into the Docker image
COPY health-check.sh /app/
RUN chmod +x /app/entrypoint.sh

# Use the RUN command to execute the script
RUN chmod +x /app/health-check.sh
RUN ./gradlew build
#RUN ./gradlew generateGitProperties
SHELL ["/bin/sh", "-c", "echo $(cat /app/build/resources/main/git.properties)"]
# check if git.properties file is generated anc echo the content

#RUN ./gradlew bootJar

# Start a new stage for the final image
FROM amazoncorretto:17-alpine-jdk

# Set the working directory in the container
WORKDIR /app

# Install the MySQL client
RUN apk add --no-cache mysql-client

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar ./app.jar


COPY src/main/resources/db/migration /app/db/migration
RUN ls -la /app >./app.txt
#SHELL ["/bin/sh", "-c", " /app/health-check.sh"]

# Run the web service on container startup
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=container"]
#CMD ["java", "-jar", "/app/app.jar"]
#ENTRYPOINT ["/app/entrypoint.sh"]
