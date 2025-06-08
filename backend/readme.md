# Gold Commodity Exchange - Backend

This backend is part of a larger gold commodity exchange platform. It consists of three Spring Boot microservices, Kafka for asynchronous communication, and WebSocket support for real-time data delivery to the frontend.

## 🧱 Architecture Overview

### Microservices:

- **auth-service**: Handles user authentication and registration
- **wallet-service**: Manages user gold and currency balances
- **trade-service**: Manages trade orders and status communication via Kafka and WebSocket

### Communication:

- Services communicate via **Kafka**
- **WebSockets** used for pushing real-time trade status updates to the frontend
- **Spring Security** with **JWT** and **TOTP (Google Authenticator)** is implemented in `auth-service` for secure authentication

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security + JWT + TOTP
- Apache Kafka
- WebSockets (Spring)
- Docker + Docker Compose

---

## 🔐 Authentication Flow (auth-service)

- User registers with phone number
- TOTP secret is generated and shown as a QR code (scannable using Google Authenticator)
- Login requires TOTP + JWT issuance for future authentication
- Stateless JWT-based access to other services

---

## 💰 Wallet Service (wallet-service)

- Handles deposit and withdrawal of money and gold
- Maintains current gold and cash balance for each user

---

## 📈 Trade Service (trade-service)

- Receives trade orders and positions via Kafka (Producer and Listener)
- Adheres to MVC architecture
- Sends live trade status updates via WebSocket to frontend

---

## ▶️ Running the Backend Using Docker Compose

### Prerequisites:

- Docker
- Docker Compose

### Steps:

1. Clone the repository:

```bash
git clone https://github.com/your-username/gold-commodity-exchange.git
cd gold-commodity-exchange/backend
```

2. Build and start the services using Docker Compose:

```bash
docker-compose up --build
```

This will:

- Build and run all 3 microservices
- Start Kafka and Zookeeper
- Expose the following ports:
  - `auth-service`: 8081
  - `wallet-service`: 8082
  - `trade-service`: 8083
  - `Kafka`: 9092 (internal use)

3. Access the services:

- Authentication: `http://localhost:8081`
- Wallet: `http://localhost:8082`
- Trade: `http://localhost:8083`

---

## 📡 Notes

- Ensure your frontend connects to WebSocket and APIs using the correct exposed ports.
- Kafka topics and group configurations should be set in each service's `application.yml`.

---

## 📋 TODO

- Add Swagger UI for API documentation
- Setup health-checks and service discovery (optional for scale)

---
