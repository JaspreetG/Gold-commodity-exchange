# 💹 Real-Time Trading System

A **microservices-based trading platform** built for **scalability and high performance**.
It features a modern ReactJS frontend, a Spring Boot backend API, a high-speed C++ matching engine, and uses Apache Kafka for real-time event streaming. All components are containerized with Docker and orchestrated via Kubernetes.

---

## 🧭 Table of Contents

- [Architecture Overview](#-architecture-overview)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Local Development](#-local-development-with-docker-compose)
- [Kubernetes Deployment](#️-kubernetes-deployment)
- [Matching Engine UML Diagrams](#-matching-engine-uml-diagrams)
- [License](#-license)

---

## 🧱 Architecture Overview

- 🖥️ **Frontend** (ReactJS)
  User interface for placing and tracking trades.

- 🔙 **Backend API** (Spring Boot)
  REST API server for order validation and coordination.

- ⚙️ **Matcher Engine** (C++17)
  High-performance engine to match buy/sell orders in real-time.

- 🔁 **Messaging** (Apache Kafka + Zookeeper)
  Decouples services and handles real-time order streaming.

- 🐳 **Containers** (Docker) & ☸️ **Orchestration** (Kubernetes)

---

## 🚀 Technologies Used

| Layer              | Technology        |
| ------------------ | ----------------- |
| 🖼️ Frontend        | ReactJS, NGINX    |
| 🔙 Backend         | Spring Boot, Java |
| ⚙️ Matching Engine | C++17             |
| 📬 Messaging       | Apache Kafka      |
| 🧠 Coordination    | Zookeeper         |
| 🐳 Containers      | Docker            |
| ☸️ Orchestration   | Kubernetes        |

---

## ⚙️ Prerequisites

- Docker ≥ 20.10
- Docker Compose ≥ 1.29
- Java 17+
- Node.js ≥ 16
- A C++20 compatible compiler (e.g., `g++-10` or later)
- kubectl ≥ 1.23
- A running Kubernetes cluster (Minikube, Kind, EKS, etc.)

---

## 🛠️ Getting Started

Clone the repository:

```bash
git clone https://github.com/jaspreetG/Gold-commodity-exchange.git
cd Gold-commodity-exchange
```

---

## 🧪 Local Development with Docker Compose

To build and start all services for local development:

```bash
docker-compose up --build
```

To stop the stack:

```bash
docker-compose down
```

---

## ☸️ Kubernetes Deployment

Deploy all services to your Kubernetes cluster:

```bash
kubectl apply -f k8s/
```

Ensure Kafka, Zookeeper, and other services are properly set up and the cluster is running.

---

## 📄 License

Licensed under the [MIT License](./LICENSE).

---

## 🔧Matching Engine UML Diagrams

This section provides visual representation of the matching engine internals using UML diagrams to help understand design structure, interactions, and order lifecycle.

### 📘 Class Diagram

Structure of core classes in the matching engine.

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
      +string orderId
      +string userId
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
      -string orderId
      -string userId
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

    %% ==== SERVICE ====
    class OrderMatchingService {
      -OrderBook book
      -KafkaTradeProducer tradeProd
      -KafkaLTPProducer ltpProd
      -KafkaOrderBookProducer obProd
      -KafkaStatusProducer statusProd
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
    class KafkaStatusProducer {
      +void publish(const string &status)
    }

    %% ==== STREAM MODELS ====
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

    %% ==== RELATIONS ====
    KafkaOrderConsumer --> OrderMatchingService : calls handleOrder()
    OrderMatchingService --> KafkaTradeProducer
    OrderMatchingService --> KafkaLTPProducer
    OrderMatchingService --> KafkaOrderBookProducer
    OrderMatchingService --> KafkaStatusProducer
    OrderMatchingService --> OrderBook
    OrderMatchingService --> OrderFactory
    OrderFactory --> Order : injects strategy
    Order --> IMatchingStrategy : calls match()
    IMatchingStrategy --> OrderBook : uses public API
```

---

### 🔁 Sequence Diagram

Flow of order processing from Kafka input to trade publication.

```mermaid
sequenceDiagram
    participant KafkaOrderConsumer
    participant OrderMatchingService
    participant OrderFactory
    participant Order
    participant OrderBook
    participant KafkaTradeProducer
    participant KafkaLTPProducer
    participant KafkaOrderBookProducer
    participant KafkaStatusProducer

    KafkaOrderConsumer->>OrderMatchingService: handleOrder(dto)
    OrderMatchingService->>OrderFactory: create(dto)
    OrderFactory-->>OrderMatchingService: Order
    OrderMatchingService->>Order: match(orderBook)
    Order->>OrderBook: getBestBid()/Ask(), addOrder(), removeOrder()
    Order->>OrderBook: updateLTP(price)
    Order-->>OrderMatchingService: vector<Trade>

    alt If trades matched
        OrderMatchingService->>KafkaTradeProducer: publish(trade)
        OrderMatchingService->>KafkaLTPProducer: publish(ltp)
    end

    OrderMatchingService->>KafkaOrderBookProducer: publish(snapshot)
    OrderMatchingService->>KafkaStatusProducer: publish("Order processed")
```

---

🔄 Activity Diagram

Visual flow of logic during order processing.

```mermaid
flowchart TD
    A["Receive order from Kafka"] --> B["Parse OrderData"]
    B --> C["Create Order via Factory"]
    C --> D["Inject Strategy"]
    D --> E["Match with OrderBook"]
    E --> F{"Match found?"}

    F -- Yes --> G["Create Trade"]
    G --> H["Update Book & LTP"]
    H --> J["Publish Trade"]
    H --> K["Publish LTP"]
    J --> M["Publish OrderBook"]
    K --> M

    F -- No --> L["Add to OrderBook"]
    L --> M

    M --> N["Publish Status"]
    N --> O["Done"]
```

---

🔀 State Transition Diagram

Lifecycle states of an order during processing.

```mermaid
stateDiagram-v2
    [*] --> Incoming : Received from Kafka
    Incoming --> Parsing : Parsing order Data
    Parsing --> Rejected : Invalid format
    Parsing --> Accepted : Successfully parsed

    Accepted --> Enriched : Strategy assigned via factory
    Enriched --> Matching : Matching started using OrderBook

    Matching --> FullyMatched : Complete match found
    Matching --> PartiallyMatched : Partial match, some qty remains
    Matching --> Unmatched : No match found

    FullyMatched --> Updated : OrderBook & LTP updated
    PartiallyMatched --> Updated
    Unmatched --> Queued : Order added to book

    Updated --> Published : Trade, LTP, Snapshot published
    Queued --> Published : Snapshot published only

    Published --> Acknowledged : KafkaStatusProducer emits final status
    Rejected --> Acknowledged
    Acknowledged --> [*]
```
