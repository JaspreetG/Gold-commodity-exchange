#pragma once
#include "models/LTP.hpp"

namespace kafka {

class KafkaLTPProducer {
public:
    void publish(const models::LTP& l);
};

} // namespace kafka
