#!/usr/bin/env sh

#run docker-exec
docker exec expense-manager-db mysql --user=root --password=password --database=expense_manager< src/main/resources/create-schema-users.sql | exit 0
