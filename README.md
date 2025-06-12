# 💹 Real-Time Trading System

A **microservices-based trading platform** built for **scalability and high performance**.  
It features a modern ReactJS frontend, a Spring Boot backend API, a high-speed C++ matching engine, and uses Apache Kafka for real-time event streaming. All components are containerized with Docker and orchestrated via Kubernetes.

## Gold Commodity Exchange 🪙

Live demo in GitHub Codespaces:

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://github.com/codespaces/new?hide_repo_select=true&repo=JaspreetG/Gold-commodity-exchange&ref=main)

> 💡 When the Codespace opens, it auto-runs all services via Docker Compose. The frontend runs on port **3000**. Use the “Ports” tab to open the live site.
> ![alt text](image.png) > ![alt text](image-1.png) > ![alt text](image-2.png) > ![alt text](image-3.png) > ![alt text](image-6.png) > ![alt text](image-7.png) > ![alt text](image-4.png) > ![alt text](image-5.png) > ![alt text](image-8.png) > ![alt text](image-10.png)

## Table of Contents

- [🧱 Architecture Overview](#architecture-overview)
- [🚀 Technologies Used](#technologies-used)
- [⚙️ Prerequisites](#prerequisites)
- [🛠️ Getting Started](#getting-started)
- [🧪 Local Development with Docker Compose](#local-development-with-docker-compose)
- [☸️ Kubernetes Deployment](#kubernetes-deployment)
- [🖥️ Deployment Diagram](#deployment-diagram)
- [🧩 Spring Boot UML Diagrams](#spring-boot-uml-diagrams)
- [🔧 Matching Engine UML Diagrams](#matching-engine-uml-diagrams)
- [📄 License](#license)

---

## Architecture Overview

- 🖥️ **Frontend** (ReactJS)  
  User interface for placing and tracking trades.

- 🔙 **Backend API** (Spring Boot)  
  REST API server for order validation and coordination.

- 🔌 **WebSocket Communication** (Spring WebSocket)  
  Enables real-time updates like order book, LTP, and trades to clients.

- ⚙️ **Matcher Engine** (C++17)  
  High-performance engine to match buy/sell orders in real-time.

- 📬 **Messaging** (Apache Kafka + Zookeeper)  
  Decouples services and handles real-time order streaming.

- 🗄️ **Database** (PostgreSQL)  
  Stores user data, wallet balances, and trade history.

- 🐳 **Containers** (Docker) & ☸️ **Orchestration** (Kubernetes)

---

## Technologies Used

| Layer              | Technology        |
| ------------------ | ----------------- |
| 🖼️ Frontend        | ReactJS, NGINX    |
| 🔙 Backend         | Spring Boot, Java |
| 🔌 WebSocket       | Spring WebSocket  |
| ⚙️ Matching Engine | C++17             |
| 📬 Messaging       | Apache Kafka      |
| 🧠 Coordination    | Zookeeper         |
| 🗄️ Database        | PostgreSQL        |
| 🐳 Containers      | Docker            |
| ☸️ Orchestration   | Kubernetes        |

---

## Prerequisites

- Docker ≥ 20.10
- Docker Compose ≥ 1.29
- Java 17+
- Node.js ≥ 16
- A C++20 compatible compiler (e.g., `g++-10` or later)
- kubectl ≥ 1.23
- A running Kubernetes cluster (Minikube, Kind, EKS, etc.)

---

## Getting Started

Clone the repository:

```bash
git clone https://github.com/jaspreetG/Gold-commodity-exchange.git
cd Gold-commodity-exchange
```

---

## Local Development with Docker Compose

To build and start all services for local development:

```bash
docker-compose up --build
```

To stop the stack:

```bash
docker-compose down
```

---

## Kubernetes Deployment

Deploy all services to your Kubernetes cluster:

```bash
kubectl apply -f k8s/
```

Ensure Kafka, Zookeeper, and other services are properly set up and the cluster is running.

---

## Deployment Diagram

This section shows how the system is deployed across containers, services, and infrastructure — highlighting interactions between Kafka, backend services, and other components.

```mermaid
flowchart LR
    %% Clients
    subgraph Clients
      UD["User Device (Mobile/Web)"]
    end

    %% Frontend Exposure
    subgraph Frontend["Frontend (K8s LoadBalancer)"]
      FE["React App Exposed via Load balancer in k8"]
    end

    %% Application Layer (Spring Boot Services)
    subgraph AppLayer["Application Layer (K8s)"]
      AuthSvc["Auth Service (Spring Boot)"]
      WalletSvc["Wallet Service (Spring Boot)"]
      TradeSvc["Trade Service (Spring Boot)"]
    end

    %% Matcher Service (C++)
    subgraph Matcher["Matching Engine Service"]
      EngineSvc["Matching Engine (C++)"]
    end

    %% Messaging Layer
    subgraph Messaging["Messaging Layer (K8s)"]
      Kafka["Kafka Broker"]
      ZK["Zookeeper"]
    end

    %% Data Layer
    subgraph Data["Data Layer"]
      UserDB[(User DB)]
      WalletDB[(Wallet DB)]
      TradeDB[(Trade DB)]
    end

    %% Connections
    UD -->|HTTPS| FE
    FE --> AuthSvc
    FE --> WalletSvc
    FE --> TradeSvc

    AuthSvc --> UserDB
    WalletSvc --> WalletDB
    TradeSvc --> TradeDB

    TradeSvc -->|Publish Order| Kafka
    Kafka --> EngineSvc
    EngineSvc -->|Publish LTP| Kafka
    Kafka --> TradeSvc

    Kafka --> ZK

    %% Styling
    style Clients      fill:#E6F7FF,stroke:#006D75,stroke-width:1px
    style Frontend     fill:#FFF0F6,stroke:#C41D7F,stroke-width:1px
    style AppLayer     fill:#E6FFFB,stroke:#08979C,stroke-width:1px
    style Matcher      fill:#FFFBE6,stroke:#D4B106,stroke-width:1px
    style Messaging    fill:#F0F5FF,stroke:#2F54EB,stroke-width:1px
    style Data         fill:#F9F0FF,stroke:#722ED1,stroke-width:1px
```

---

## Spring Boot UML Diagrams

This section visualizes the internal architecture and flow of the Spring Boot services using various UML diagrams.

---

### 🎯 Use Case Diagram

High-level representation of how different users (like Admin, Trader) interact with the system.

```mermaid
flowchart TD
    %% Actor
    User((User))

    %% System Boundary
    subgraph GCESystem[Gold Commodity Exchange System]

        subgraph Authentication
            UC1([Register with Phone Number])
            UC2([Setup TOTP])
            UC3([Login via TOTP])
            UC7([Logout])
        end

        subgraph Trading
            UC4([Access Dashboard])
            UC5([Place Order])
            UC6([View Order Book])
            UC8([See Past Trades])
        end

        subgraph Wallet
            UC9([Add Money])
            UC10([Withdraw Money])
        end

    end

    %% Actor Use Case Links
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
    User --> UC10
```

---

### 📘 Class Diagram

Structure of key Spring Boot components: controllers, services, repositories, models.

```mermaid
classDiagram

%% Authentication Service
class AuthController {
  +Response register(PhoneDTO)
  +Response verifyTOTP(TOTPDTO)
  +Response loginTOTP(TOTPDTO)
}

class AuthService {
  -UserRepository userRepo
  -AuthenticationManager authManager
  -AuthenticationProvider authProvider
  +register(PhoneDTO): User
  +verifyTOTP(phone, code): bool
  +sendTOTP(phone): void
}

class UserRepository {
  +findByPhone(phone): User
  +save(user): User
}

class User {
  -String userId
  -String phone
  -String totpSecret
}

AuthController --> AuthService
AuthService --> UserRepository
AuthService --> AuthenticationManager
AuthService --> AuthenticationProvider
UserRepository ..> User

%% Wallet Service
class WalletController {
  +Response addMoney(AmountDTO)
  +Response withdrawMoney(AmountDTO)
}

class WalletService {
  -WalletRepository walletRepo
  +addFunds(userId, amount): Wallet
  +withdrawFunds(userId, amount): Wallet
}

class WalletRepository {
  +findByUserId(id): Wallet
  +save(wallet): Wallet
}

class Wallet {
  -String walletId
  -String userId
  -BigDecimal balance
  -double gold
}

WalletController --> WalletService
WalletService --> WalletRepository
WalletRepository ..> Wallet

%% Trading Service
class TradeController {
  +Response placeOrder(OrderDTO)
  +List<Trade> getPastTrades(userId)
}

class TradeService {
  -TradeRepository tradeRepo
  -KafkaTemplate kafka
  +placeOrder(OrderDTO): Trade
  +getPastTrades(userId): List<Trade>
}

class TradeRepository {
  +save(trade): Trade
  +findByUserId(userId): List<Trade>
}

class Trade {
  -String tradeId
  -String userId
  -String orderId
  -BigDecimal price
  -BigDecimal quantity
  -LocalDateTime timestamp
}

TradeController --> TradeService
TradeService --> TradeRepository
TradeService --> KafkaTemplate
TradeRepository ..> Trade
```

---

### 🔁 Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant AuthService
    participant WalletService
    participant TradingService
    participant Kafka
    participant MatchingEngine
    participant Database

    %% --- Registration Flow ---
    User->>Frontend: Open app & enter phone
    Frontend->>AuthService: Check if phone exists
    AuthService-->>Frontend: Phone doesn't exist
    Frontend->>AuthService: Register user
    AuthService->>Database: Save user & TOTP secret
    AuthService-->>Frontend: Show TOTP setup (QR code)
    User->>Frontend: Scans QR / enters TOTP
    Frontend->>AuthService: Verify TOTP
    AuthService->>Database: Validate TOTP
    AuthService-->>Frontend: Registration complete

    %% --- Login Flow ---
    User->>Frontend: Enter phone & TOTP
    Frontend->>AuthService: Authenticate
    AuthService->>Database: Fetch user & TOTP secret
    AuthService-->>Frontend: Auth success / failure

    %% --- Dashboard Access ---
    User->>Frontend: Access dashboard

    %% --- Wallet Operations ---
    User->>Frontend: Add money
    Frontend->>WalletService: Add funds
    WalletService->>Database: Update balance
    WalletService-->>Frontend: Funds added

    User->>Frontend: Withdraw money
    Frontend->>WalletService: Withdraw request
    WalletService->>Database: Deduct balance
    WalletService-->>Frontend: Withdrawal success

    %% --- Trading Flow (Corrected) ---
    User->>Frontend: Place order
    Frontend->>TradingService: Submit order
    TradingService->>Database: Validate balance
    TradingService->>Kafka: Publish order to "order" topic

    Kafka->>MatchingEngine: Deliver order
    MatchingEngine->>Kafka: Publish Trade to "trade" topic
    Kafka->>Database: Store Trade in DB

    TradingService-->>Frontend: Order placed

    %% --- Fetch Past Trades ---
    User->>Frontend: View trade history
    Frontend->>TradingService: Request trade history
    TradingService->>Database: Fetch trade data
    TradingService-->>Frontend: Return trade list
```

---

### 🔄 Activity Diagram

Visualizes logic flow (e.g., registration, order handling, etc.).

```mermaid
flowchart TD
    A([User opens app / frontend])
    B[Enter phone number]
    C{Phone number exists?}
    D[Register user with phone]
    E[Generate and store TOTP secret]
    F[Prompt user to set up TOTP in authenticator app]
    G[Enter TOTP code]
    H{TOTP code valid?}
    I[Show error: invalid TOTP]
    J[Complete registration and authenticate user]
    K[Send OTP to phone]
    L[Enter OTP]
    M{OTP valid?}
    N[Show error: invalid OTP]
    O[User authenticated]
    P[Access dashboard]
    Q[Place order]
    R[View order book]
    S[Add money to wallet]
    T[Withdraw money]
    U[See past trades]
    V[Logout]

    %% Registration Flow
    A --> B --> C
    C -- No --> D --> E --> F --> G --> H
    H -- No --> I --> G
    H -- Yes --> J --> P

    %% Login Flow
    C -- Yes --> K --> L --> M
    M -- No --> N --> L
    M -- Yes --> O --> P

    %% Main App Functions
    P --> Q
    P --> R
    P --> S
    P --> T
    P --> U
    P --> V
```

---

## Matching Engine UML Diagrams

This section provides visual representation of the matching engine internals using UML diagrams to help understand design structure, interactions, and order lifecycle.

### Class Diagram

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

### Sequence Diagram

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

### Activity Diagram

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

### State Transition Diagram

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

## License

Licensed under the [MIT License](./LICENSE).

---
