#include "models/OrderBookSnapshot.hpp"

namespace models {

OrderBookSnapshot::OrderBookSnapshot(
    std::map<double, std::list<core::Order>> bids,
    std::map<double, std::list<core::Order>> asks,
    std::chrono::system_clock::time_point ts)
    : bids_(std::move(bids)), asks_(std::move(asks)), ts_(ts) {}

const auto& OrderBookSnapshot::bids() const { return bids_; }
const auto& OrderBookSnapshot::asks() const { return asks_; }
std::chrono::system_clock::time_point OrderBookSnapshot::timestamp() const {
    return ts_;
}

} // namespace models
