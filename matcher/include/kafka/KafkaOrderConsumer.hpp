#pragma once

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
