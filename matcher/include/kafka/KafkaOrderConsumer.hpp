#pragma once
#include <cppkafka/consumer.h>
#include <memory>

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

    private:
        std::unique_ptr<cppkafka::Consumer> consumer_;
    };

} // namespace kafka
