#pragma once
#include "models/OrderBookSnapshot.hpp"

namespace kafka {

/**
 * @class KafkaOrderBookProducer
 * @brief Producer for publishing Order Book snapshots to Kafka.
 */
class KafkaOrderBookProducer {
public:
    /**
     * @brief Publishes an Order Book snapshot.
     * @param s The snapshot of the order book.
     */
    void publish(const models::OrderBookSnapshot& s);
};

} // namespace kafka
