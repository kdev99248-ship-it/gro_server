#!/bin/bash

echo "Starting MySQL 5.7 container..."

# Check container exists

if [ "$(docker ps -a -q -f name=mysql57)" ]; then
docker start mysql57
else
echo "Container not found, creating new one..."
docker run -d
--name mysql57
-p 3306:3306
-e MYSQL_ROOT_PASSWORD=123456
-e MYSQL_DATABASE=mydb
-v /opt/mysql57:/var/lib/mysql
mysql:5.7
fi

echo "Done!"
