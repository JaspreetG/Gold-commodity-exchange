#pragma once
#include "models/Trade.hpp"

namespace kafka {

class KafkaTradeProducer {
public:
    void publish(const models::Trade& t);
};

} // namespace kafka
