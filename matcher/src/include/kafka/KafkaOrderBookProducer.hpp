#pragma once
#include "models/OrderBookSnapshot.hpp"

namespace kafka {

class KafkaOrderBookProducer {
public:
    void publish(const models::OrderBookSnapshot& s);
};

} // namespace kafka
