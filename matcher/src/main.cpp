#include "kafka/KafkaOrderConsumer.hpp"
#include "service/OrderMatchingService.hpp"

int main()
{
    core::OrderMatchingService service;
    kafka::KafkaOrderConsumer consumer;
    consumer.start(service);
    return 0;
}
