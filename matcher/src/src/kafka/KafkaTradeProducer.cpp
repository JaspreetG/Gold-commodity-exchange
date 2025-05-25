#include "kafka/KafkaTradeProducer.hpp"
#include <iostream>

namespace kafka {

void KafkaTradeProducer::publish(const models::Trade& t) {
    std::cout << "Published Trade: " << t.buyOrderId()
              << " vs " << t.sellOrderId()
              << " @ " << t.price()
              << " qty " << t.quantity() << "\n";
}

} // namespace kafka
