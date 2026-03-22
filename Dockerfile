# =============================================================================
# Consolidated Application Dockerfile
# Runs auth-service (8080), wallet-service (8081), trade-service (8082),
# matching-engine (C++), and frontend/nginx (80) in a single container
# managed by supervisord.
#
# External dependencies (postgres, zookeeper, kafka) remain as separate
# Railway services and are wired in via environment variables.
# =============================================================================

# -----------------------------------------------------------------------------
# Stage 1: Build auth-service
# -----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build-auth
WORKDIR /app
COPY backend/auth-service/pom.xml .
COPY backend/auth-service/src ./src
RUN mvn clean package -DskipTests

# -----------------------------------------------------------------------------
# Stage 2: Build wallet-service
# -----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build-wallet
WORKDIR /app
COPY backend/wallet-service/pom.xml .
COPY backend/wallet-service/src ./src
RUN mvn clean package -DskipTests

# -----------------------------------------------------------------------------
# Stage 3: Build trade-service
# -----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build-trade
WORKDIR /app
COPY backend/trade-service/pom.xml .
COPY backend/trade-service/src ./src
RUN mvn clean package -DskipTests

# -----------------------------------------------------------------------------
# Stage 4: Build matching-engine (C++ with cppkafka)
# -----------------------------------------------------------------------------
FROM ubuntu:22.04 AS build-matching-engine

ENV DEBIAN_FRONTEND=noninteractive

# Install build tools and all compile-time dependencies
RUN apt-get update --fix-missing && \
    apt-get install -y \
        build-essential \
        cmake \
        git \
        libboost-all-dev \
        librdkafka-dev \
        libssl-dev \
        pkg-config && \
    rm -rf /var/lib/apt/lists/*

# Build cppkafka from source
WORKDIR /tmp
RUN git clone --branch v0.4.1 https://github.com/mfontanini/cppkafka.git && \
    cd cppkafka && \
    mkdir build && cd build && \
    cmake .. && \
    make -j$(nproc) && \
    make install

# Copy built cppkafka source tree so CMake add_subdirectory() can find it
RUN mkdir -p /app && cp -r /tmp/cppkafka /app/cppkafka

WORKDIR /app
COPY MatchingEngine/ .

# Fetch nlohmann/json single-header
ADD https://raw.githubusercontent.com/nlohmann/json/v3.11.3/single_include/nlohmann/json.hpp \
    /usr/include/nlohmann/json.hpp

RUN cmake -Bbuild -H. && cmake --build build --target MatchingEngine -- -j$(nproc)

# -----------------------------------------------------------------------------
# Stage 5: Build frontend (React/Vite)
# -----------------------------------------------------------------------------
FROM node:18-slim AS build-frontend
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build

# -----------------------------------------------------------------------------
# Stage 6: Runtime image — all services + supervisord + nginx
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk

ENV DEBIAN_FRONTEND=noninteractive

# Install supervisord, nginx, and C++ runtime libraries
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        supervisor \
        nginx \
        libboost-system1.74.0 \
        librdkafka1 \
        libssl3 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# ── Java service JARs ────────────────────────────────────────────────────────
COPY --from=build-auth   /app/target/auth-service-0.0.1-SNAPSHOT.jar   /app/auth-service.jar
COPY --from=build-wallet /app/target/wallet-service-0.0.1-SNAPSHOT.jar /app/wallet-service.jar
COPY --from=build-trade  /app/target/trade-service-0.0.1-SNAPSHOT.jar  /app/trade-service.jar

# ── Matching engine binary + shared libraries ─────────────────────────────────
COPY --from=build-matching-engine /app/build/MatchingEngine           /app/MatchingEngine
COPY --from=build-matching-engine /usr/local/lib/libcppkafka.so.0.4.1 /usr/lib/
COPY --from=build-matching-engine /usr/local/lib/libcppkafka.so       /usr/lib/
RUN cd /usr/lib && \
    [ -e libcppkafka.so.0 ] || ln -s libcppkafka.so.0.4.1 libcppkafka.so.0 && \
    [ -e libcppkafka.so ]   || ln -s libcppkafka.so.0      libcppkafka.so && \
    ldconfig
ENV LD_LIBRARY_PATH=/usr/lib

# ── Frontend static assets ────────────────────────────────────────────────────
COPY --from=build-frontend /app/dist /usr/share/nginx/html

# ── nginx configuration (proxies API calls to localhost services) ─────────────
COPY nginx.conf /etc/nginx/conf.d/default.conf

# ── supervisord configuration ─────────────────────────────────────────────────
COPY supervisord.conf /etc/supervisor/conf.d/services.conf

# Create log directory for supervisord
RUN mkdir -p /var/log/supervisor

# Expose all service ports
EXPOSE 80 8080 8081 8082

# supervisord runs in the foreground and manages all processes
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/supervisord.conf"]
