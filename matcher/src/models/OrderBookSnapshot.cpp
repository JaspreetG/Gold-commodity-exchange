#include "models/OrderBookSnapshot.hpp"

namespace models
{

    OrderBookSnapshot::OrderBookSnapshot(
        const std::map<double, std::list<core::Order>, std::greater<>> &bids,
        const std::map<double, std::list<core::Order>> &asks,
        std::chrono::system_clock::time_point ts)
        : bids_(bids), asks_(asks), ts_(ts) {}

    std::chrono::system_clock::time_point OrderBookSnapshot::timestamp() const
    {
        return ts_;
    }
    std::map<double, double, std::greater<>> OrderBookSnapshot::bidVolumes() const
    {
        std::map<double, double, std::greater<>> result;
        for (std::map<double, std::list<core::Order>, std::greater<>>::const_iterator it = bids_.begin(); it != bids_.end(); ++it)
        {
            double volume = 0.0;
            for (std::list<core::Order>::const_iterator orderIt = it->second.begin(); orderIt != it->second.end(); ++orderIt)
            {
                volume += orderIt->quantity();
            }
            result[it->first] = volume;
        }
        return result;
    }
    std::map<double, double> OrderBookSnapshot::askVolumes() const
    {
        std::map<double, double> result;
        for (std::map<double, std::list<core::Order>>::const_iterator it = asks_.begin(); it != asks_.end(); ++it)
        {
            double volume = 0.0;
            for (std::list<core::Order>::const_iterator orderIt = it->second.begin(); orderIt != it->second.end(); ++orderIt)
            {
                volume += orderIt->quantity();
            }
            result[it->first] = volume;
        }
        return result;
    }

} // namespace models
