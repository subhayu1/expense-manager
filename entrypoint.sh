#!/bin/bash

 Wait for the MySQL service to be ready
until mysqladmin ping -h db --user=root --password=password --silent; do
    sleep 1
done

# Perform database initialization tasks using SQL script
#mysql -h db --user=root --password=password< src/main/resources/create-schema-users.sql


# Start your Java application
java -jar app/app.jar --spring.profiles.active=container
