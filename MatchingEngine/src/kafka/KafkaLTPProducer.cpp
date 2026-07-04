/**
 * @file KafkaLTPProducer.cpp
 * @brief Implementation of the KafkaLTPProducer class.
 */

#include "kafka/KafkaLTPProducer.hpp"
#include <iostream>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    /**
     * @brief Default constructor for KafkaLTPProducer.
     * 
     * Initializes the underlying cppkafka::Producer by fetching the broker address
     * from the KAFKA_BROKER environment variable (default: 127.0.0.1:29092).
     */
    KafkaLTPProducer::KafkaLTPProducer()
        : producer_([] {
            const char* broker_env = std::getenv("KAFKA_BROKER");
            std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
            return cppkafka::Configuration{{"metadata.broker.list", broker}};
        }())
    {
    }

    /**
     * @brief Serializes an LTP model into JSON and publishes it to the "ltp" topic.
     * 
     * @param l The LTP object containing the latest price and timestamp.
     */
    void KafkaLTPProducer::publish(const models::LTP &l)
    {
        auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(l.timestamp().time_since_epoch()).count();
        nlohmann::json j = {
            {"price", l.price()},
            {"timestamp", ms}};
        std::string payload = j.dump();
        producer_.produce(cppkafka::MessageBuilder("ltp").payload(payload));
        std::cout << "Published LTP to 'ltp': " << payload << std::endl;
    }

} // namespace kafka
