# 💹 Real-Time Trading System

A **microservices-based trading platform** built for **scalability and high performance**.
It features a modern ReactJS frontend, a Spring Boot backend API, a high-speed C++ matching engine, and uses Apache Kafka for real-time event streaming. All components are containerized with Docker and orchestrated via Kubernetes.

---

## 🧭 Table of Contents

- [Architecture Overview](#-architecture-overview)
- [Technologies Used](#-technologies-used)
- [Repository Structure](#-repository-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Local Development](#-local-development-with-docker-compose)
- [Kubernetes Deployment](#️-kubernetes-deployment)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Monitoring & Logging](#-monitoring--logging)
- [Future Enhancements](#-future-enhancements)
- [License](#-license)
- [Author](#-author)

---

## 🧱 Architecture Overview

- 🖥️ **Frontend** (ReactJS)
  User interface for placing and tracking trades.

- 🔙 **Backend API** (Spring Boot)
  REST API server for order validation and coordination.

- ⚙️ **Matcher Engine** (C++20)
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
| ⚙️ Matching Engine | C++20             |
| 📬 Messaging       | Apache Kafka      |
| 🧠 Coordination    | Zookeeper         |
| 🐳 Containers      | Docker            |
| ☸️ Orchestration   | Kubernetes        |

---

## 📦 Repository Structure

```plaintext
trading-system/
├── frontend/             # ReactJS frontend app
│   ├── Dockerfile
│   ├── public/
│   └── src/
├── backend/              # Spring Boot backend API
│   ├── Dockerfile
│   └── src/main/java/
├── matcher/              # C++ matching engine
│   ├── Dockerfile
│   └── src/
├── kafka/                # (Optional) Kafka custom configs
├── k8s/                  # Kubernetes manifests
│   ├── frontend/
│   ├── backend/
│   ├── matcher/
│   └── kafka/
├── docker-compose.yml    # Local development config
├── LICENSE
└── README.md
```

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

(Optional) Copy and configure environment variables:

```bash
cp .env.example .env
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

## 📑 API Documentation

Swagger/OpenAPI will be available at:

```
http://localhost:8080/swagger-ui.html
```

(If enabled in the Spring Boot backend.)

---

## 🧪 Testing

Run tests for different components:

**Backend:**

```bash
cd backend
./mvnw test
```

**Frontend:**

```bash
cd frontend
npm install
npm test
```

**Matcher (if testable):**

```bash
cd matcher
make test
```

---

## 📊 Monitoring & Logging

- Logs are written to `stdout` and can be viewed using Docker or `kubectl logs`.
- Future support planned for Prometheus and Grafana dashboards.

---

## 🧠 Future Enhancements

- 📊 Real-time trade dashboard
- 🔐 Secure user authentication (JWT or OAuth2)
- 📈 Historical trade analytics and charting
- 🌍 Multi-region Kubernetes support
- 💾 Persistent order storage and replication
- 📡 WebSocket-based real-time UI updates

---

## 📄 License

Licensed under the [MIT License](./LICENSE).

---

## 👨‍💻 Author

Made with ❤️ by **{NAME}**
A software engineer passionate about real-time systems, high-performance backends, and clean architecture.
