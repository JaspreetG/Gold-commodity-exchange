package io.goldexchange.trade_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for sending orders to the matching engine.
 */
@Component
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructs the OrderProducer.
     *
     * @param kafkaTemplate The Kafka template for sending messages.
     */
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends an order to the "order" Kafka topic.
     *
     * @param orderJson The order details in JSON format.
     */
    public void sendOrder(String orderJson) {

        kafkaTemplate.send("order", orderJson);
    }
}
