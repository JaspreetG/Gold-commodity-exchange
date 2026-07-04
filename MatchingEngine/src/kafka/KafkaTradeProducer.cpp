/**
 * @file KafkaTradeProducer.cpp
 * @brief Implementation of the KafkaTradeProducer class.
 */

#include "kafka/KafkaTradeProducer.hpp"
#include <iostream>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    /**
     * @brief Default constructor for KafkaTradeProducer.
     * 
     * Initializes the underlying cppkafka::Producer by fetching the broker address
     * from the KAFKA_BROKER environment variable (default: 127.0.0.1:29092).
     */
    KafkaTradeProducer::KafkaTradeProducer()
        : producer_([] {
            const char* broker_env = std::getenv("KAFKA_BROKER");
            std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
            return cppkafka::Configuration{{"metadata.broker.list", broker}};
        }())
    {
    }

    /**
     * @brief Serializes a Trade model into JSON and publishes it to the "trade" topic.
     * 
     * @param t The Trade object containing match details like IDs, price, quantity, and timestamp.
     */
    void KafkaTradeProducer::publish(const models::Trade &t)
    {
        auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(t.timestamp().time_since_epoch()).count();
        nlohmann::json j = {
            {"buyOrderId", t.buyOrderId()},
            {"sellOrderId", t.sellOrderId()},
            {"buyUserId", t.buyUserId()},
            {"sellUserId", t.sellUserId()},
            {"price", t.price()},
            {"quantity", t.quantity()},
            {"timestamp", ms}};
        std::string payload = j.dump();
        producer_.produce(cppkafka::MessageBuilder("trade").payload(payload));
        std::cout << "Published Trade to 'trade': " << payload << std::endl;
    }

} // namespace kafka
