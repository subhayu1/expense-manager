#!/bin/bash
./app/health-check.sh
# Start your Java application
java -jar app/app.jar --spring.profiles.active=container
