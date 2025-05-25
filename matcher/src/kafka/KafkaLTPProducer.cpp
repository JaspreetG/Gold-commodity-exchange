#include "kafka/KafkaLTPProducer.hpp"
#include <iostream>

namespace kafka {

void KafkaLTPProducer::publish(const models::LTP& l) {
    std::cout << "Published LTP: " << l.price() << "\n";
}

} // namespace kafka
