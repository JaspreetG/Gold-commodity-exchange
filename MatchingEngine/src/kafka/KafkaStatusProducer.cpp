/**
 * @file KafkaStatusProducer.cpp
 * @brief Implementation of the KafkaStatusProducer class.
 */

#include "kafka/KafkaStatusProducer.hpp"
#include <iostream>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    /**
     * @brief Default constructor for KafkaStatusProducer.
     * 
     * Initializes the underlying cppkafka::Producer by fetching the broker address
     * from the KAFKA_BROKER environment variable (default: 127.0.0.1:29092).
     */
    KafkaStatusProducer::KafkaStatusProducer()
        : producer_([] {
            const char* broker_env = std::getenv("KAFKA_BROKER");
            std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
            return cppkafka::Configuration{{"metadata.broker.list", broker}};
        }())
    {
    }

    /**
     * @brief Serializes a Status model into JSON and publishes it to the "status" topic.
     * 
     * @param s The Status object containing the updated order details (quantity filled, etc.).
     */
    void KafkaStatusProducer::publish(const models::Status &s)
    {
        auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(s.timestamp().time_since_epoch()).count();
        nlohmann::json j = {
            {"orderId", s.orderId()},
            {"userId", s.userId()},
            {"side", s.side()},
            {"quantity", s.quantity()},
            {"timestamp", ms}};
        std::string payload = j.dump();
        producer_.produce(cppkafka::MessageBuilder("status").payload(payload));
        std::cout << "Published Status to 'status': " << payload << std::endl;
    }

} // namespace kafka
