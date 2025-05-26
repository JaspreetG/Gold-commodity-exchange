# 🧩 Kafka Setup

This folder contains Kafka-specific setup scripts and environment config.

## 📦 Topics

The following Kafka topics are created:

- `orders` – Handles all incoming order data (buy/sell)
- `matches` – Used by matcher engine to publish matched orders

## Run this to start kafka

run
docker login
then

```
docker-compose -f docker-compose.kafka.yml up -d
```

## 🚀 Creating Topics

Run the topic creation script (after Kafka is up):
edit kafka env to add topics

## 🧪 Test produce/consume

Produce a message:

```
docker exec -it kafka-kafka-1 kafka-console-producer \
  --broker-list localhost:9092 \
  --topic orders
```

Then type messages like:

{"orderId": 1, "status": "created"}
{"orderId": 2, "status": "shipped"}

(Press Ctrl+C to stop)

Consume messages:

```
docker exec -it kafka-kafka-1 kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic orders \
  --from-beginning
```
