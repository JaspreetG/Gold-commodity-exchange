#include "models/OrderBookSnapshot.hpp"

namespace models
{

    OrderBookSnapshot::OrderBookSnapshot(
        const std::map<double, std::list<core::Order>, std::greater<>> &bids,
        const std::map<double, std::list<core::Order>> &asks,
        std::chrono::system_clock::time_point ts)
        : bids_(bids), asks_(asks), ts_(ts) {}

    const auto &OrderBookSnapshot::bids() const { return bids_; }
    const auto &OrderBookSnapshot::asks() const { return asks_; }
    std::chrono::system_clock::time_point OrderBookSnapshot::timestamp() const
    {
        return ts_;
    }

} // namespace models
