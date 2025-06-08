# 🧩 Kafka Setup

This folder contains Kafka-specific setup scripts and environment config.

## 📦 Topics

The following Kafka topics are created:

- `order` – Handles all incoming order data (buy/sell)
- `trade` – Used by matcher engine to publish matched orders
- `orderbook` – Used to send orderbook to consumer
- `ltp` – Used to send Last traded price

## Run this to start kafka

run
docker login
then

```shell
docker-compose -f docker-compose.kafka.yml up -d
```

## 🚀 Checking Topics

```shell
docker exec -it kafka kafka-topics --bootstrap-server kafka:9092 --list
```

## 🧪 Test produce/consume

Produce a message:

```shell
docker exec -it kafka kafka-console-producer \
  --broker-list localhost:9092 \
  --topic order
```

Then type messages like (all in one line, copy-paste ready):

```json
{"user_id":"order-1","quantity":10,"price":1950.5,"side":"BUY","type":"LIMIT"}
{"user_id":"order-2","quantity":5,"price":1900.0,"side":"SELL","type":"LIMIT"}
{"user_id":"order-3","quantity":2,"price":0,"side":"BUY","type":"MARKET"}
{"user_id":"order-2","quantity":100,"price":1900.0,"side":"SELL","type":"LIMIT"}
```

(Press Ctrl+C to stop)

Consume messages:

```shell
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic order \
  --from-beginning
```

```shell
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic orderbook \
  --from-beginning
```

```shell
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic ltp \
  --from-beginning
```

```shell
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic trade \
  --from-beginning
```

```shell
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic status \
  --from-beginning
```

