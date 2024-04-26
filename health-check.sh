#!/bin/bash

# Directory where your git repository is located
REPO_DIR="./"
# JSON file to append the data
OUTPUT_FILE="./health-check.json"

# Navigate to the repository directory
cd "$REPO_DIR"

# Get the latest commit hash
commit_hash=$(git rev-parse HEAD)

# Get the current branch name
branch_name=$(git rev-parse --abbrev-ref HEAD)

# Get the commit message, replacing newlines with spaces
commit_message=$(git log -1 --pretty=%B | tr '\n' ' ')

# Get the commit date and time
commit_date=$(git log -1 --format=%cd)

# Manually create a JSON object
json_object="{\"commit_hash\": \"$commit_hash\", \"branch_name\": \"$branch_name\", \"commit_message\": \"$commit_message\", \"commit_date\": \"$commit_date\"}"

# Append the JSON object to the output file
echo $json_object >> $OUTPUT_FILE
