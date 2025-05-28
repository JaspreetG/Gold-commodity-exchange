# ---- Build stage ----
FROM ubuntu:22.04 AS build

ENV DEBIAN_FRONTEND=noninteractive

# Install build tools and dependencies (including Boost for cppkafka)
RUN apt-get update --fix-missing && \
    apt-get install -y \
        build-essential \
        cmake \
        git \
        libboost-all-dev \
        librdkafka-dev \
        libssl-dev \
        pkg-config \
        wget \
        unzip && \
    rm -rf /var/lib/apt/lists/*

# Build cppkafka from source inside the container
WORKDIR /tmp
RUN git clone --branch v0.4.1 https://github.com/mfontanini/cppkafka.git && \
    cd cppkafka && \
    mkdir build && cd build && \
    cmake .. && \
    make -j$(nproc) && \
    make install && \
    ls -l /usr/local/lib/ && \
    mkdir -p /cppkafka-lib && \
    cp -L /usr/local/lib/libcppkafka.so.0.4.1 /cppkafka-lib/ && \
    # Only copy .so.0.4.1 and .so, skip .so.0 which does not exist
    cp -L /usr/local/lib/libcppkafka.so /cppkafka-lib/

WORKDIR /app
COPY . .
# Ensure json.hpp is present before building
ADD https://raw.githubusercontent.com/nlohmann/json/v3.11.3/single_include/nlohmann/json.hpp /usr/include/nlohmann/json.hpp

RUN cmake -Bbuild -H. && cmake --build build --target MatchingEngine -- -j$(nproc)

# Debug: List all possible locations for libcppkafka.so* after build
RUN echo '--- /usr/local/lib/ ---' && ls -l /usr/local/lib/ || true \
 && echo '--- /usr/lib/ ---' && ls -l /usr/lib/ || true \
 && echo '--- /app/build/cppkafka/src/ ---' && ls -l /app/build/cppkafka/src/ || true \
 && echo '--- /app/build/lib/ ---' && ls -l /app/build/lib/ || true \
 && echo '--- /app/build/ ---' && ls -l /app/build/ || true \
 && echo '--- /app/build/cppkafka/ ---' && ls -l /app/build/cppkafka/ || true

# ---- Runtime stage ----
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install runtime dependencies
RUN apt-get update --fix-missing && \
    apt-get install -y \
        libboost-system1.74.0 \
        librdkafka1 \
        libssl3 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/build/MatchingEngine /app/MatchingEngine
COPY --from=build /cppkafka-lib/libcppkafka.so.0.4.1 /usr/lib/
COPY --from=build /cppkafka-lib/libcppkafka.so /usr/lib/
# Ensure symlinks for libcppkafka are present and run ldconfig in a single RUN block
RUN set -eux; \
  cd /usr/lib; \
  if [ ! -e libcppkafka.so.0.4.1 ]; then \
    echo 'libcppkafka.so.0.4.1 missing!'; exit 1; \
  fi; \
  [ -e libcppkafka.so.0 ] || ln -s libcppkafka.so.0.4.1 libcppkafka.so.0; \
  [ -e libcppkafka.so ] || ln -s libcppkafka.so.0 libcppkafka.so; \
  ldconfig
ENV LD_LIBRARY_PATH=/usr/lib
CMD ["./MatchingEngine"]
