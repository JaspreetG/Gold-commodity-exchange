#pragma once

#include <nlohmann/json.hpp> // For JSON parsing
#include <rdkafka.h>         // Include librdkafka header

namespace core
{
    class OrderMatchingService; // Forward declaration
}

namespace kafka
{

    class KafkaOrderConsumer
    {
    public:
        void start(core::OrderMatchingService &svc);
    };

} // namespace kafka
