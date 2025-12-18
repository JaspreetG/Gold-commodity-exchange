#pragma once
#include "models/LTP.hpp"

namespace kafka {

/**
 * @class KafkaLTPProducer
 * @brief Producer for publishing Last Traded Price (LTP) updates to Kafka.
 */
class KafkaLTPProducer {
public:
    /**
     * @brief Publishes an LTP update.
     * @param l The LTP data object.
     */
    void publish(const models::LTP& l);
};

} // namespace kafka
