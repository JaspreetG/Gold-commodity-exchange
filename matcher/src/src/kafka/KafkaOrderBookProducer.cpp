#include "kafka/KafkaOrderBookProducer.hpp"
#include <iostream>

namespace kafka {

void KafkaOrderBookProducer::publish(const models::OrderBookSnapshot& s) {
    std::cout << "Published OrderBook Snapshot at timestamp.\n";
}

} // namespace kafka
