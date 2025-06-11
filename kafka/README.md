# 🧩 Kafka Setup

This folder contains Kafka-specific setup scripts.

## 📦 Topics

The following Kafka topics are created:

- `order` – Handles all incoming order data (buy/sell)
- `trade` – Publishes matched orders from the matching engine
- `orderbook` – Broadcasts the current state of the order book to consumers
- `ltp` – Sends the last traded price
- `status` – Used for publishing live status of order

## Run this to start kafka

run
docker login
then

```shell
docker-compose -f docker-compose.kafka.yml up -d
```

## 🚀 Checking Topics

List all Kafka topics:

```bash
docker exec -it zookeeper-kafka kafka-topics --bootstrap-server kafka:9092 --list
```

## 🧪 Test Produce/Consume

### Produce messages to the `order` topic:

```bash
docker exec -it zookeeper-kafka kafka-console-producer \
  --broker-list localhost:9092 \
  --topic order
```

Then paste the following (all on one line per message):

```json
{"user_id":"order-1","quantity":10,"price":1950.5,"side":"BUY","type":"LIMIT"}
{"user_id":"order-2","quantity":5,"price":1900.0,"side":"SELL","type":"LIMIT"}
{"user_id":"order-3","quantity":2,"price":0,"side":"BUY","type":"MARKET"}
{"user_id":"order-2","quantity":100,"price":1900.0,"side":"SELL","type":"LIMIT"}
```

(Press `Ctrl+C` to stop the producer)

### Consume messages from each topic:

**Order:**

```bash
docker exec -it zookeeper-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic order \
  --from-beginning
```

**Orderbook:**

```bash
docker exec -it zookeeper-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic orderbook \
  --from-beginning
```

**LTP:**

```bash
docker exec -it zookeeper-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic ltp \
  --from-beginning
```

**Trade:**

```bash
docker exec -it zookeeper-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic trade \
  --from-beginning
```

**Status:**

```bash
docker exec -it zookeeper-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic status \
  --from-beginning
```
