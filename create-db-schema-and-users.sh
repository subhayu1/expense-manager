#!/usr/bin/env sh

docker exec expense-manager-db mysql --user=root --password=password < src/main/resources/create-schema-users.sql
docker exec expense-manager-db mysql --user=root --password=password -e "CALL CreateDatabaseAndUsers()"