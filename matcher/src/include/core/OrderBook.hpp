#pragma once
#include <map>
#include <list>
#include <optional>
#include "core/Order.hpp"

namespace core {

class OrderBook {
    friend class BuyMarketStrategy;
    friend class SellMarketStrategy;
    friend class BuyLimitStrategy;
    friend class SellLimitStrategy;

private:
    std::map<double, std::list<Order>, std::greater<>> bids_;
    std::map<double, std::list<Order>> asks_;
    double lastTradedPrice_{0.0};

public:
    void addOrder(const Order& o);
    void removeOrder(const Order& o);
    void updateLTP(double price);
    double getLTP() const;

    std::optional<std::reference_wrapper<Order>> getBestBid();
    std::optional<std::reference_wrapper<Order>> getBestAsk();

    friend class OrderMatchingService; // for snapshot access
};

} // namespace core
