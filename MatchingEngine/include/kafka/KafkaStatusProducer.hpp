#pragma once
#include "models/Status.hpp"

namespace kafka
{

    class KafkaStatusProducer
    {
    public:
        void publish(const models::Status &s);
    };

} // namespace kafka
