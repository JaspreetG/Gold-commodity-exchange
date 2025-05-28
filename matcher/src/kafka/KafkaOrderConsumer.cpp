#include "kafka/KafkaOrderConsumer.hpp"
#include <cppkafka/configuration.h>
#include <cppkafka/message.h>
#include <cppkafka/consumer.h>
#include <nlohmann/json.hpp>
#include <iostream>
#include "service/OrderMatchingService.hpp"

namespace kafka
{

    void KafkaOrderConsumer::start(core::OrderMatchingService &svc)
    {
        // --- Kafka broker selection for local vs Docker ---
        // To use a local Kafka broker (running on your host):
        //   1. Uncomment the LOCAL_DEV block below.
        //   2. Comment out the DOCKER block.
        // To use Docker Compose Kafka (from inside a container):
        //   1. Uncomment the DOCKER block below.
        //   2. Comment out the LOCAL_DEV block.

        // --- LOCAL_DEV BLOCK: Use localhost for Kafka broker ---
        // Uncomment this block for local development (Kafka running on your host):
        /*
        cppkafka::Configuration config = {
            {"metadata.broker.list", "127.0.0.1:9092"}, // Change port if needed
            {"group.id", "matcher-group"},
            {"auto.offset.reset", "earliest"}
        };
        */

        // --- DOCKER BLOCK: Use KAFKA_BROKER env var (default to localhost if not set) ---
        // Uncomment this block when running inside Docker:
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:9092";
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker},
            {"group.id", "matcher-group"},
            {"auto.offset.reset", "earliest"}};

        // --- End of Kafka broker selection ---
        // Create the consumer with the configuration
        consumer_ = std::make_unique<cppkafka::Consumer>(config);
        consumer_->subscribe({"orders"});
        std::cout << "KafkaOrderConsumer started, listening for messages..." << std::endl;
        while (true)
        {
            auto msg = consumer_->poll();
            if (msg && !msg.get_error()) // Correct error check
            {
                try
                {
                    std::string payload = msg.get_payload(); // Get as string
                    if (!payload.empty())
                    {
                        nlohmann::json j = nlohmann::json::parse(payload);
                        std::string user_id = j.at("user_id");
                        double quantity = j.at("quantity");
                        double price = j.at("price");
                        std::string side = j.at("side");
                        std::string type = j.at("type");
                        dto::OrderData orderData = {
                            user_id,
                            quantity,
                            price,
                            (side == "BUY" ? dto::Side::BUY : dto::Side::SELL),
                            (type == "LIMIT" ? dto::OrderType::LIMIT : dto::OrderType::MARKET)};
                        std::cout << "Received order: " << user_id << std::endl;
                        svc.handleOrder(orderData);
                    }
                }
                catch (const std::exception &e)
                {
                    std::cerr << "Failed to parse JSON message: " << e.what() << std::endl;
                }
            }
        }
    }

} // namespace kafka
