#pragma once
#include "service/OrderMatchingService.hpp"

namespace kafka {

class KafkaOrderConsumer {
public:
    void start(core::OrderMatchingService& svc);
};

} // namespace kafka
