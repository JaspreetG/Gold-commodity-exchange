#include "kafka/KafkaOrderConsumer.hpp"
#include "dto/order.pb.h" // Include the generated Protobuf header
#include <rdkafka.h>      // Include librdkafka header
#include <iostream>
#include <cstring>
#include "dto/OrderData.hpp"                // Include the OrderData DTO
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
                    // Deserialize Protobuf message
                    dto::Order order;
                    if (order.ParseFromArray(msg->payload, msg->len))
                    {
                        std::cout << "Received order: " << order.order_id() << std::endl;

                        // Process the order
                        dto::OrderData orderData = {
                            order.order_id(),
                            order.quantity(),
                            order.price(),
                            order.side() == "BUY" ? dto::Side::BUY : dto::Side::SELL,
                            order.type() == "LIMIT" ? dto::OrderType::LIMIT : dto::OrderType::MARKET};
                        svc.handleOrder(orderData);
                    }
                    else
                    {
                        std::cerr << "Failed to parse Protobuf message" << std::endl;
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
