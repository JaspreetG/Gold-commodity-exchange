#pragma once

#include "dto/order.pb.h" // Include the generated Protobuf header
#include <rdkafka.h>      // Include librdkafka header

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
