# Gold Commodity Exchange - Backend

This backend is part of a larger gold commodity exchange platform. It consists of three Spring Boot microservices, Kafka for asynchronous communication, and WebSocket support for real-time data delivery to the frontend.

## 🧱 Architecture Overview

### Microservices

- **auth-service**: Handles user authentication and registration
- **wallet-service**: Manages user gold and currency balances
- **trade-service**: Manages trade orders and status communication via Kafka and WebSocket

### Communication

- Services communicate via **Kafka**
- **WebSockets** used for pushing real-time trade status updates to the frontend
- **Spring Security** with **JWT** and **TOTP (Google Authenticator)** is implemented in `auth-service` for secure authentication

---

## 🛠️ Tech Stack

- Java 21
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

### Prerequisites

- Docker
- Docker Compose

### Only one step

1. Build and start the services using Docker Compose:

```bash
docker-compose up --build
```

This will:

- Build and run all 3 microservices
- Start Kafka and Zookeeper
- Expose the following ports:
  - `auth-service`: 8080
  - `wallet-service`: 8081
  - `trade-service`: 8082

2. Access the services

- Authentication: `http://localhost:8080`
- Wallet: `http://localhost:8081`
- Trade: `http://localhost:8082`
