# Kubernetes Setup Guide for Gold Commodity Exchange

This guide explains how to deploy and manage the Kubernetes setup for the Gold Commodity Exchange project using Docker Desktop's built-in Kubernetes cluster.

---

## 🚀 Prerequisites

- Docker Desktop installed and running
- Kubernetes enabled in Docker Desktop settings
- kubectl CLI installed (comes with Docker Desktop)

---

## 📦 Directory Structure

The `k8s/` folder contains all deployment and service YAMLs for:

- `auth-service`
- `wallet-service`
- `trade-service`
- `frontend`
- `matching-engine`
- `postgres`
- `kafka` and `zookeeper`
- `namespace.yaml`

---

## 🛠️ Steps to Deploy

### 1. Enable Kubernetes

Open Docker Desktop → Preferences → Kubernetes → Enable Kubernetes → Apply & Restart

### 2. Verify Kubernetes is Running

```bash
kubectl cluster-info
kubectl get nodes
```

### 3. Apply All Kubernetes Resources

```bash
kubectl apply -f k8s/ --recursive
```

### 4. Check Status of All Resources

Use this

```bash
kubectl get pods -n gold-exchange
```

```bash
kubectl get all -n gold-exchange
```

---

## 📉 Delete All Resources

To delete everything within the `gold-exchange` namespace:

```bash
kubectl delete all --all -n gold-exchange
```

To delete the namespace itself:

```bash
kubectl delete namespace gold-exchange
```

---

## 🧹 Clean and Reset Kubernetes

You can also reset the Kubernetes cluster from Docker Desktop:

- Docker Desktop → Settings → Kubernetes → Reset Kubernetes Cluster

---

## 📍 Additional Tips

- Use `imagePullPolicy: Never` in deployment YAMLs to use locally built Docker images
- Use `kubectl logs <pod-name> -n gold-exchange` to debug
- Use `kubectl describe pod <pod-name> -n gold-exchange` to inspect events and reasons for errors

---

## 🌐 Accessing Frontend

`frontend` service is exposed via `LoadBalancer`, access it at:

```
http://localhost:80
```

Check with:

```bash
kubectl get svc frontend -n gold-exchange
```

---

Happy trading! 🪙
