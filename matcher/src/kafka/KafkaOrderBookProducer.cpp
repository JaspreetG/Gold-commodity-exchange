#include "kafka/KafkaOrderBookProducer.hpp"
#include <iostream>
#include <cppkafka/producer.h>
#include <nlohmann/json.hpp>
#include <cstdlib>

namespace kafka
{

    void KafkaOrderBookProducer::publish(const models::OrderBookSnapshot &s)
    {
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:9092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker}};
        cppkafka::Producer producer(config);
        // Convert timestamp to milliseconds since epoch
        long long ms = std::chrono::duration_cast<std::chrono::milliseconds>(s.timestamp().time_since_epoch()).count();
        // Prepare bids and asks as arrays of {price, size}
        nlohmann::json bids = nlohmann::json::array();
        for (std::map<double, std::list<core::Order>, std::greater<>>::const_iterator it = s.bids().begin(); it != s.bids().end(); ++it)
        {
            nlohmann::json entry;
            entry["price"] = it->first;
            entry["size"] = static_cast<int>(it->second.size());
            bids.push_back(entry);
        }
        nlohmann::json asks = nlohmann::json::array();
        for (std::map<double, std::list<core::Order>>::const_iterator it = s.asks().begin(); it != s.asks().end(); ++it)
        {
            nlohmann::json entry;
            entry["price"] = it->first;
            entry["size"] = static_cast<int>(it->second.size());
            asks.push_back(entry);
        }
        nlohmann::json j = {
            {"timestamp", ms},
            {"bids", bids},
            {"asks", asks}};
        std::string payload = j.dump();
        producer.produce(cppkafka::MessageBuilder("orderbook").partition(0).payload(payload));
        producer.flush();
        std::cout << "Published OrderBook Snapshot to 'orderbook': " << payload << "\n";
    }

} // namespace kafka
