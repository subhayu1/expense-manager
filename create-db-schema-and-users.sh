#!/usr/bin/env sh

#run docker-exec
docker exec expense-manager-db mysql --user=root --password=password< src/main/resources/create-schema-users.sql
