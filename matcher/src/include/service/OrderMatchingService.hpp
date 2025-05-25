#pragma once
#include "core/OrderBook.hpp"
#include "core/OrderFactory.hpp"
#include "kafka/KafkaOrderConsumer.hpp"
#include "kafka/KafkaTradeProducer.hpp"
#include "kafka/KafkaLTPProducer.hpp"
#include "kafka/KafkaOrderBookProducer.hpp"

namespace core {

class OrderMatchingService {
    OrderBook book_;
    kafka::KafkaTradeProducer tradeProd_;
    kafka::KafkaLTPProducer ltpProd_;
    kafka::KafkaOrderBookProducer obProd_;

public:
    void handleOrder(const dto::OrderData& dto);
};

} // namespace core
