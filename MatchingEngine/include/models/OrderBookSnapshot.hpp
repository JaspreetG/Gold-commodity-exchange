#pragma once
#include <map>
#include <list>
#include <chrono>
#include "core/Order.hpp"

namespace models
{

    class OrderBookSnapshot
    {
    private:
        std::map<double, std::list<core::Order>, std::greater<>> bids_;
        std::map<double, std::list<core::Order>> asks_;
        std::chrono::system_clock::time_point ts_;

    public:
        OrderBookSnapshot(const std::map<double, std::list<core::Order>, std::greater<>> &bids,
                          const std::map<double, std::list<core::Order>> &asks,
                          std::chrono::system_clock::time_point ts);

        std::chrono::system_clock::time_point timestamp() const;

        // Returns a map of price to total volume (sum of quantities) for bids
        std::map<double, int, std::greater<>> bidVolumes() const;
        // Returns a map of price to total volume (sum of quantities) for asks
        std::map<double, int> askVolumes() const;
    };

} // namespace models
