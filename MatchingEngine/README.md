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
    %% ==== ENUMS ====
    class Side {
      <<enumeration>>
      BUY
      SELL
    }
    class OrderType {
      <<enumeration>>
      MARKET
      LIMIT
    }

    %% ==== DTO ====
    class OrderData {
      <<DTO>>
      +string id
      +double quantity
      +double price
      +Side side
      +OrderType type
    }

    %% ==== STRATEGY INTERFACE ====
    class IMatchingStrategy {
      <<interface>>
      +vector<Trade> match(Order&, OrderBook&)
    }

    %% ==== CONCRETE STRATEGIES ====
    class BuyMarketStrategy
    class BuyLimitStrategy
    class SellMarketStrategy
    class SellLimitStrategy

    BuyMarketStrategy ..|> IMatchingStrategy
    BuyLimitStrategy ..|> IMatchingStrategy
    SellMarketStrategy ..|> IMatchingStrategy
    SellLimitStrategy ..|> IMatchingStrategy

    %% ==== ORDER ====
    class Order {
      -string id
      -double quantity
      -double price
      -Side side
      -OrderType type
      -shared_ptr<IMatchingStrategy> strategy
      +Order(OrderData dto, shared_ptr<IMatchingStrategy>)
      +vector<Trade> match(OrderBook &book)
    }

    %% ==== FACTORY ====
    class OrderFactory {
      <<factory>>
      +static Order create(const OrderData &dto)
    }

    %% ==== ORDER BOOK ====
    class OrderBook {
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

    %% ==== SERVICE ====
    class OrderMatchingService {
      -OrderBook book
      -KafkaTradeProducer tradeProd
      -KafkaLTPProducer ltpProd
      -KafkaOrderBookProducer obProd
      +void handleOrder(const OrderData &dto)
    }

    %% ==== KAFKA I/O ====
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

    %% ==== STREAM MODELS ====
    class Trade {
      -string buyOrderId
      -string sellOrderId
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

    %% ==== RELATIONS ====
    KafkaOrderConsumer --> OrderMatchingService : calls handleOrder()
    OrderMatchingService --> KafkaTradeProducer
    OrderMatchingService --> KafkaLTPProducer
    OrderMatchingService --> KafkaOrderBookProducer
    OrderMatchingService --> OrderBook
    OrderMatchingService --> OrderFactory
    OrderFactory --> Order : injects strategy
    Order --> IMatchingStrategy : calls match()
    IMatchingStrategy --> OrderBook : uses public API
```

---

- For more details, see the source code in the `MatchingEngine` directory.
