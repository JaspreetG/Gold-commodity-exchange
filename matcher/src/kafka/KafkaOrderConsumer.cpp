#include "kafka/KafkaOrderConsumer.hpp"
#include <rdkafka.h> // Include librdkafka header
#include <nlohmann/json.hpp>
#include <iostream>
#include <cstring>
#include "service/OrderMatchingService.hpp" // Include the full definition of OrderMatchingService

namespace kafka
{

    void KafkaOrderConsumer::start(core::OrderMatchingService &svc)
    {
        // Kafka configuration
        rd_kafka_conf_t *conf = rd_kafka_conf_new();
        rd_kafka_conf_set(conf, "bootstrap.servers", "localhost:9092", nullptr, 0);
        rd_kafka_conf_set(conf, "group.id", "order_consumer_group", nullptr, 0);

        // Create Kafka consumer
        char errstr[512];
        rd_kafka_t *consumer = rd_kafka_new(RD_KAFKA_CONSUMER, conf, errstr, sizeof(errstr));
        if (!consumer)
        {
            std::cerr << "Failed to create Kafka consumer: " << errstr << std::endl;
            return;
        }

        // Subscribe to the topic
        rd_kafka_topic_partition_list_t *topics = rd_kafka_topic_partition_list_new(1);
        rd_kafka_topic_partition_list_add(topics, "orders", RD_KAFKA_PARTITION_UA);
        if (rd_kafka_subscribe(consumer, topics))
        {
            std::cerr << "Failed to subscribe to topic" << std::endl;
            rd_kafka_topic_partition_list_destroy(topics);
            rd_kafka_destroy(consumer);
            return;
        }
        rd_kafka_topic_partition_list_destroy(topics);

        std::cout << "KafkaOrderConsumer started. Listening for messages..." << std::endl;

        // Consume messages
        while (true)
        {
            rd_kafka_message_t *msg = rd_kafka_consumer_poll(consumer, 1000);
            if (msg)
            {
                if (msg->err)
                {
                    if (msg->err != RD_KAFKA_RESP_ERR__PARTITION_EOF)
                    {
                        std::cerr << "Error: " << rd_kafka_message_errstr(msg) << std::endl;
                    }
                }
                else
                {
                    // Parse JSON message
                    try
                    {
                        std::string payload(static_cast<const char *>(msg->payload), msg->len);
                        auto j = nlohmann::json::parse(payload);
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
                        svc.handleOrder(orderData);
                        std::cout << "Received order: " << user_id << std::endl;
                    }
                    catch (const std::exception &e)
                    {
                        std::cerr << "Failed to parse JSON message: " << e.what() << std::endl;
                    }
                }
                rd_kafka_message_destroy(msg);
            }
        }

        // Cleanup
        rd_kafka_consumer_close(consumer);
        rd_kafka_destroy(consumer);
    }

} // namespace kafka
