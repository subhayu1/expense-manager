# Use a different base image
FROM ubuntu:20.04

# Install dependencies
RUN apt-get update && apt-get install -y wget gnupg2 software-properties-common

# Install Amazon Corretto JDK
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add -
RUN add-apt-repository 'deb https://apt.corretto.aws stable main'
RUN apt-get update && apt-get install -y java-17-amazon-corretto-jdk

# Install MySQL client
RUN apt-get install -y mysql-client

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper files to the container
COPY mvnw .
COPY .mvn .mvn

# Copy the project configuration files to the container
COPY pom.xml .

# Download the project dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the project source code to the container
COPY src ./src

# Build the project
RUN ./mvnw package -DskipTests

# Copy the entrypoint script to the container
COPY entrypoint.sh .

# Make the entrypoint script executable
RUN chmod +x entrypoint.sh

# Set the entry point for the container
ENTRYPOINT ["./entrypoint.sh"]