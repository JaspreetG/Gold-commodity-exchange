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

        // --- DOCKER BLOCK: Use KAFKA_BROKER env var (default to localhost if not set) ---
        const char *broker_env = std::getenv("KAFKA_BROKER");
        std::string broker = broker_env ? broker_env : "127.0.0.1:29092";
        std::cout << "Using Kafka broker: " << broker << std::endl;
        cppkafka::Configuration config = {
            {"metadata.broker.list", broker},
            {"group.id", "MatchingEngine-group"},
            {"auto.offset.reset", "earliest"}};

        // --- End of Kafka broker selection ---
        // Create the consumer with the configuration
        consumer_ = std::make_unique<cppkafka::Consumer>(config);
        consumer_->subscribe({"order"});
        std::cout << "KafkaOrderConsumer started, listening for messages..." << std::endl;
        while (true)
        {
            auto msg = consumer_->poll(std::chrono::seconds(1));
            if (msg && !msg.get_error()) // Correct error check
            {
                try
                {
                    std::string payload = msg.get_payload(); // Get as string
                    if (!payload.empty())
                    {
                        nlohmann::json j = nlohmann::json::parse(payload);
                        std::string order_id = j.at("orderId").get<std::string>();
                        std::string user_id = j.at("userId").get<std::string>();
                        int quantity = j.at("quantity").get<int>();
                        double price = j.at("price").get<double>();
                        std::string side = j.at("side").get<std::string>();
                        std::string type = j.at("type").get<std::string>();
                        dto::OrderData orderData = {
                            order_id,
                            user_id,
                            quantity,
                            price,
                            (side == "BUY" ? dto::Side::BUY : dto::Side::SELL),
                            (type == "LIMIT" ? dto::OrderType::LIMIT : dto::OrderType::MARKET)};
                        std::cout << "Received order: " << user_id << std::endl;
                        svc.handleOrder(orderData);
                        // std::cout << "Order handled successfully." << std::endl;
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
