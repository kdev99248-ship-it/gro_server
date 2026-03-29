#!/bin/bash

CONTAINER_NAME="mysql57"

echo "Starting MySQL 5.7 container..."

# Check docker installed

if ! command -v docker &> /dev/null
then
echo "Docker not installed!"
exit 1
fi

# Start docker service if not running

if ! systemctl is-active --quiet docker; then
echo "Starting Docker service..."
systemctl start docker
fi

# Check container exists

if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then

```
# Check running
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Container already running."
else
    echo "Starting existing container..."
    docker start $CONTAINER_NAME
fi
```

else
echo "Container not found, creating new one..."
docker run -d \
--name mysql57 \
--restart unless-stopped \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_DATABASE=mydb \
-v /opt/mysql57:/var/lib/mysql \
mysql:5.7
fi

echo "Waiting for MySQL to initialize..."
sleep 10

echo "Done!"
