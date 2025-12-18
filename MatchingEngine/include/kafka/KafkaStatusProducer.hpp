#pragma once
#include "models/Status.hpp"

namespace kafka
{

    /**
     * @class KafkaStatusProducer
     * @brief Producer for publishing order status updates to Kafka.
     */
    class KafkaStatusProducer
    {
    public:
        /**
         * @brief Publishes a status update.
         * @param s The status data object.
         */
        void publish(const models::Status &s);
    };

} // namespace kafka
