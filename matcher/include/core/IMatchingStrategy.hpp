#pragma once
#include <vector>
#include "models/Trade.hpp"
#include "kafka/KafkaStatusProducer.hpp"

namespace core
{
    class Order;     // Forward declaration
    class OrderBook; // Forward declaration

    class IMatchingStrategy
    {
    public:
        IMatchingStrategy() = default;
        virtual ~IMatchingStrategy();
        virtual std::vector<models::Trade> match(Order &incoming, OrderBook &book) = 0;
        static kafka::KafkaStatusProducer statusProducer;
    };

} // namespace core
