#pragma once
#include "core/OrderBook.hpp"
#include "core/OrderFactory.hpp"
#include "kafka/KafkaTradeProducer.hpp"
#include "kafka/KafkaLTPProducer.hpp"
#include "kafka/KafkaOrderBookProducer.hpp"

namespace kafka
{
    class KafkaOrderConsumer; // Forward declaration
}

namespace core
{

    class OrderMatchingService
    {
        OrderBook &book_ = OrderBook::getInstance(); // Reference to the singleton instance
        kafka::KafkaTradeProducer tradeProd_;
        kafka::KafkaLTPProducer ltpProd_;
        kafka::KafkaOrderBookProducer obProd_;

    public:
        OrderMatchingService(); // Explicitly declare the constructor
        void handleOrder(const dto::OrderData &dto);
    };

} // namespace core
