#pragma once
#include <map>
#include <list>
#include <optional>
#include <functional>

namespace core
{

    class Order; // Forward declaration

    class OrderBook
    {
        friend class BuyMarketStrategy;
        friend class SellMarketStrategy;
        friend class BuyLimitStrategy;
        friend class SellLimitStrategy;
        friend class OrderMatchingService; // Allow access to private members

        std::map<double, std::list<Order>, std::greater<>> bids_;
        std::map<double, std::list<Order>> asks_;
        double lastTradedPrice_{0.0};

    public:
        void addOrder(const Order &o);
        void removeOrder(const Order &o);
        void updateLTP(double price);
        double getLTP() const;

        // std::optional<std::reference_wrapper<Order>> getBestBid();
        // std::optional<std::reference_wrapper<Order>> getBestAsk();
    };

} // namespace core
