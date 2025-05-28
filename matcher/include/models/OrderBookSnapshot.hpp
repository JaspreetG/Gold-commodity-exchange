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

        const std::map<double, std::list<core::Order>, std::greater<>> &bids() const;
        const std::map<double, std::list<core::Order>> &asks() const;
        std::chrono::system_clock::time_point timestamp() const;
    };

} // namespace models
