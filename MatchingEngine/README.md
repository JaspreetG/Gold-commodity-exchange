# Matcher Engine

## Build and Run with Docker Compose

1. **Ensure Docker and Docker Compose are installed.**
2. From the MatchingEngineroot, run:

```sh
docker-compose up --build
```

- This will build and start MatchingEngineas defined in the `docker-compose.yml` files.
- To stop the services:

```sh
docker-compose down
```

## Local Development (macOS/Linux)

1. **Install dependencies:**

   - C++ compiler (e.g., clang++)
   - CMake
   - [librdkafka](https://github.com/edenhill/librdkafka)
   - [cppkafka](https://github.com/mfontanini/cppkafka)
   - nlohmann/json (header-only)

2. **Build the matchingEngine:**

```sh
cd MatchingEngine
make
```

- The executable `MatchingEngine` will be created in the `MatchingEngine` directory.

- **Run Kafka and Zookeeper (if not using Docker Compose):**

  - Start Zookeeper and Kafka brokers as required.

- **Run the matchingEngine:**

```sh
./MatchingEngine
```

## Class Diagram (Mermaid)

```mermaid
classDiagram

%% ================= ENUMS =================
class Side {
  <<enumeration>>
  +BUY
  +SELL
}

class OrderType {
  <<enumeration>>
  +MARKET
  +LIMIT
}

%% ================= DTO =================
class OrderData {
  <<DTO>>
  +string orderId
  +string userId
  +double quantity
  +double price
  +Side side
  +OrderType type
}

%% ================= ORDER =================
class Order {
  -string orderId
  -string userId
  -double quantity
  -double price
  -Side side
  -OrderType type
  +Order(OrderData dto)
}

class OrderFactory {
  <<factory>>
  +static Order create(const OrderData &dto)
}

%% ================= ORDER BOOK =================
class OrderBook {
  <<singleton>>
  -map<double, list<Order>, greater<double>> bids
  -map<double, list<Order>> asks
  -double lastTradedPrice
  +void addOrder(const Order &o)
  +void removeOrder(const Order &o)
  +void updateLTP(double price)
  +double getLTP() const
  +optional<Order> getBestBid() const
  +optional<Order> getBestAsk() const
}

%% ================= STRATEGY =================
class IMatchingStrategy {
  <<interface>>
  +vector<Trade> match(Order&, OrderBook&)
}

class StrategyFactory {
  <<factory>>
  +static shared_ptr<IMatchingStrategy> create(const OrderData &dto)
}

class BuyMarketStrategy {
  +vector<Trade> match(Order&, OrderBook&)
}
class BuyLimitStrategy {
  +vector<Trade> match(Order&, OrderBook&)
}
class SellMarketStrategy {
  +vector<Trade> match(Order&, OrderBook&)
}
class SellLimitStrategy {
  +vector<Trade> match(Order&, OrderBook&)
}

%% ================= STREAM MODELS =================
class Trade {
  -string buyerOrderId
  -string buyerUserId
  -string sellerOrderId
  -string sellerUserId
  -double price
  -double quantity
  -timestamp ts
}

class LTP {
  -double price
  -timestamp ts
}

class OrderBookSnapshot {
  -map<double, list<Order>> bids
  -map<double, list<Order>> asks
  -timestamp ts
}

%% ================= KAFKA =================
class KafkaOrderConsumer {
  +void start(OrderMatchingService &svc)
}

class KafkaTradeProducer {
  +void publish(const Trade &t)
}

class KafkaLTPProducer {
  +void publish(const LTP &l)
}

class KafkaOrderBookProducer {
  +void publish(const OrderBookSnapshot &s)
}

class KafkaStatusProducer {
  +void publish(const string &status)
}

%% ================= SERVICE =================
class OrderMatchingService {
  -OrderBook book
  -KafkaTradeProducer tradeProd
  -KafkaLTPProducer ltpProd
  -KafkaOrderBookProducer obProd
  -KafkaStatusProducer statusProd
  +void handleOrder(const OrderData &dto)
}

%% ================= RELATIONSHIPS =================

%% Kafka interactions
KafkaOrderConsumer ..> OrderMatchingService : uses
KafkaOrderConsumer ..> OrderData : receives

KafkaTradeProducer ..> Trade : takes
KafkaLTPProducer ..> LTP : takes
KafkaOrderBookProducer ..> OrderBookSnapshot : takes

%% Service internal flow
OrderMatchingService ..> OrderFactory : creates
OrderMatchingService ..> StrategyFactory : selects strategy
OrderMatchingService ..> IMatchingStrategy : uses for matching
OrderMatchingService ..> OrderBook : accesses state
OrderMatchingService ..> LTP : sends
OrderMatchingService ..> Trade : sends
OrderMatchingService ..> OrderBookSnapshot : sends

OrderMatchingService --> KafkaTradeProducer : composition
OrderMatchingService --> KafkaLTPProducer : composition
OrderMatchingService --> KafkaOrderBookProducer : composition

%% Strategy logic
StrategyFactory ..> OrderData : analyzes
StrategyFactory ..> IMatchingStrategy : returns

BuyMarketStrategy ..|> IMatchingStrategy
BuyLimitStrategy ..|> IMatchingStrategy
SellMarketStrategy ..|> IMatchingStrategy
SellLimitStrategy ..|> IMatchingStrategy

IMatchingStrategy ..> OrderBook : reads
IMatchingStrategy ..> Trade : returns
IMatchingStrategy ..> KafkaStatusProducer : reports status

%% Order creation
OrderFactory ..> Order : returns
OrderFactory ..> OrderData : uses
```

---

- For more details, see the source code in the `MatchingEngine` directory.
