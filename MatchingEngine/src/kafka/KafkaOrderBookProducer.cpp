/**
 * @file KafkaOrderBookProducer.cpp
 * @brief Implementation of the KafkaOrderBookProducer class.
 */

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
        std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker}};
        cppkafka::Producer producer(config);
        // Convert timestamp to milliseconds since epoch
        long long ms = std::chrono::duration_cast<std::chrono::milliseconds>(s.timestamp().time_since_epoch()).count();
        // Prepare bids and asks as arrays of {price, volume}
        nlohmann::json bids = nlohmann::json::array();
        // Use int for volume values
        std::map<double, int, std::greater<>> bid_vols = s.bidVolumes();
        for (std::map<double, int, std::greater<>>::const_iterator it = bid_vols.begin(); it != bid_vols.end(); ++it)
        {
            nlohmann::json entry;
            entry["price"] = it->first;
            entry["volume"] = it->second;
            bids.push_back(entry);
        }
        nlohmann::json asks = nlohmann::json::array();
        std::map<double, int> ask_vols = s.askVolumes();
        for (std::map<double, int>::const_iterator it = ask_vols.begin(); it != ask_vols.end(); ++it)
        {
            nlohmann::json entry;
            entry["price"] = it->first;
            entry["volume"] = it->second;
            asks.push_back(entry);
        }
        nlohmann::json j = {
            {"timestamp", ms},
            {"bids", bids},
            {"asks", asks}};
        std::string payload = j.dump();
        producer.produce(cppkafka::MessageBuilder("orderbook").partition(0).payload(payload));
        producer.flush();
        std::cout << "Published OrderBook Snapshot to 'orderbook': " << payload << std::endl;
    }

} // namespace kafka
