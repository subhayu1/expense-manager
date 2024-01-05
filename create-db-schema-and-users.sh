#!/usr/bin/env bash

#run docker-exec
docker exec -it expense-manager-db mysql --user=root --password=password --database=expense_manager < src/main/resources/create-schema-users.sql | exit 0
