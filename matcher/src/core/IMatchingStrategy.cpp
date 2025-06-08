#include "core/IMatchingStrategy.hpp"
namespace core
{
    IMatchingStrategy::~IMatchingStrategy() = default;
    kafka::KafkaStatusProducer IMatchingStrategy::statusProducer;

} // namespace core
