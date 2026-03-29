#!/bin/bash

# --- Cấu hình ---
JAR_PATH="run/Nro.jar"
LOG_FILE="nohup.out"
DB_CONTAINER="mysql57"
START_DB_SCRIPT="$HOME/gro_server/database/start_db.sh"

# --- 1. Kiểm tra process Nro.jar ---
PID=$(pgrep -f "$JAR_PATH")
if [ -n "$PID" ]; then
    echo "Found running Nro.jar process (PID=$PID). Killing it..."
    kill -9 $PID
    sleep 2
else
    echo "No running Nro.jar process found."
fi

# --- 2. Kiểm tra Docker MySQL ---
if [ -z "$(docker ps -q -f name=$DB_CONTAINER)" ]; then
    echo "Database container '$DB_CONTAINER' not running. Starting..."
    if [ -f "$START_DB_SCRIPT" ]; then
        bash "$START_DB_SCRIPT"
        # Chờ container MySQL ready
        echo "Waiting 10s for MySQL to initialize..."
        sleep 10
    else
        echo "Error: start_db.sh not found at $START_DB_SCRIPT"
        exit 1
    fi
else
    echo "Database container '$DB_CONTAINER' is already running."
fi

# --- 3. Chạy Nro.jar bằng nohup ---
echo "Starting Nro.jar with nohup..."
nohup java -jar "$JAR_PATH" > "$LOG_FILE" 2>&1 &

echo "Nro.jar started. Logs: $LOG_FILE"