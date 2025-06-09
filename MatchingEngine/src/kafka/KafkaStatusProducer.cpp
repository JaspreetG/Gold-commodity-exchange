#include "kafka/KafkaStatusProducer.hpp"
#include <iostream>
#include <cppkafka/producer.h>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    void KafkaStatusProducer::publish(const models::Status &s)
    {
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker}};
        cppkafka::Producer producer(config);
        nlohmann::json j = {
            {"orderId", s.orderId()},
            {"userId", s.userId()},
            {"side", s.side()},
            {"quantity", s.quantity()}};
        std::string payload = j.dump();
        producer.produce(cppkafka::MessageBuilder("status").partition(0).payload(payload));
        producer.flush();
        std::cout << "Published Status to 'status': " << payload << std::endl;
    }

} // namespace kafka
