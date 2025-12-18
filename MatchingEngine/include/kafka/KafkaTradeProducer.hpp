#pragma once
#include "models/Trade.hpp"

namespace kafka {

/**
 * @class KafkaTradeProducer
 * @brief Producer for publishing executed trades to Kafka.
 */
class KafkaTradeProducer {
public:
    /**
     * @brief Publishes a trade event.
     * @param t The trade details.
     */
    void publish(const models::Trade& t);
};

} // namespace kafka
