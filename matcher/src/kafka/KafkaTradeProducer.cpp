#include "kafka/KafkaTradeProducer.hpp"
#include <iostream>
#include <cppkafka/producer.h>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    void KafkaTradeProducer::publish(const models::Trade &t)
    {
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:9092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker}};
        cppkafka::Producer producer(config);
        nlohmann::json j = {
            {"buyOrderId", t.buyOrderId()},
            {"sellOrderId", t.sellOrderId()},
            {"price", t.price()},
            {"quantity", t.quantity()}};
        std::string payload = j.dump();
        producer.produce(cppkafka::MessageBuilder("trade").partition(0).payload(payload));
        producer.flush();
        std::cout << "Published Trade to 'trade': " << payload << "\n";
    }

} // namespace kafka
