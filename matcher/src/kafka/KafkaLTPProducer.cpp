#include "kafka/KafkaLTPProducer.hpp"
#include <iostream>
#include <cppkafka/producer.h>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    void KafkaLTPProducer::publish(const models::LTP &l)
    {
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:9092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker}};
        cppkafka::Producer producer(config);
        // Convert timestamp to milliseconds since epoch
        auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(l.timestamp().time_since_epoch()).count();
        nlohmann::json j = {
            {"price", l.price()},
            {"timestamp", ms}};
        std::string payload = j.dump();
        producer.produce(cppkafka::MessageBuilder("ltp").partition(0).payload(payload));
        producer.flush();
        std::cout << "Published LTP to 'ltp': " << payload << std::endl;
    }

} // namespace kafka
