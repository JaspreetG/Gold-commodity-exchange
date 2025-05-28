#!/bin/bash

# Load variables from kafka.env
source "$(dirname "$0")/kafka.env"

# Check if any topic was provided
if [ -z "$TOPICS" ]; then
  echo "⚠️  No topics specified in kafka.env. Nothing to create."
  exit 0
fi

echo "Waiting for Kafka to be ready..."

until docker exec "$KAFKA_CONTAINER" kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --list >/dev/null 2>&1; do
  echo "Kafka not ready yet... waiting 5 seconds"
  sleep 5
done

echo "Kafka is ready! Creating topics..."

for topic in $TOPICS; do
  echo "Creating topic: $topic"
  docker exec "$KAFKA_CONTAINER" kafka-topics \
    --create --if-not-exists \
    --bootstrap-server "$BOOTSTRAP_SERVER" \
    --replication-factor "$REPLICATION_FACTOR" \
    --partitions "$PARTITIONS" \
    --topic "$topic" || echo "Failed to create topic $topic"
done

echo -e "\nCurrent topics in Kafka:"
docker exec "$KAFKA_CONTAINER" kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --list
