#!/bin/bash

CONTAINER_NAME="mysql57"
DB_NAME="goirong"
MYSQL_USER="root"
MYSQL_PASSWORD="123456"
SQL_FILE="./gro.sql"
START_SCRIPT="./start_db.sh"

echo "=== MySQL Import Script ==="

Check SQL file

if [ ! -f "$SQL_FILE" ]; then
echo "Error: File $SQL_FILE not found!"
exit 1
fi

Check docker container exists

if [ -z "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
echo "Container $CONTAINER_NAME not found. Starting via start_db.sh..."

if [ -f "$START_SCRIPT" ]; then
    bash $START_SCRIPT
    sleep 5
else
    echo "Error: start_db.sh not found!"
    exit 1
fi

fi

Check container running

if [ -z "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
echo "Container exists but not running. Starting container..."
docker start $CONTAINER_NAME
sleep 5
fi

Copy SQL file into container

echo "Copying SQL file into container..."
docker cp "$SQL_FILE" $CONTAINER_NAME:/tmp/gro.sql

if [ $? -ne 0 ]; then
echo "Error copying SQL file!"
exit 1
fi

Confirm before drop DB

read -p "Database '$DB_NAME' will be DROPPED if exists. Continue? (y/n): " confirm

if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then
echo "Aborted."
exit 0
fi

echo "Dropping and recreating database..."

docker exec -i $CONTAINER_NAME mysql -u$MYSQL_USER -p$MYSQL_PASSWORD <<EOF
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
EOF

if [ $? -ne 0 ]; then
echo "Error creating database!"
exit 1
fi

echo "Importing SQL..."

docker exec -i $CONTAINER_NAME bash -c "mysql -u$MYSQL_USER -p$MYSQL_PASSWORD $DB_NAME < /tmp/gro.sql"

if [ $? -ne 0 ]; then
echo "Import failed!"
exit 1
fi

echo "=== Import completed successfully ==="