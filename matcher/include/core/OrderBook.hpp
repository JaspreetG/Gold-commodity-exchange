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
        // friend class BuyMarketStrategy;
        // friend class SellMarketStrategy;
        // friend class BuyLimitStrategy;
        // friend class SellLimitStrategy;
        // friend class OrderMatchingService; // Allow access to private members

    private:
        // Private constructor to prevent instantiation
        OrderBook() = default;

        // Delete copy constructor and assignment operator
        OrderBook(const OrderBook &) = delete;
        OrderBook &operator=(const OrderBook &) = delete;

        // Delete move constructor and move assignment operator
        OrderBook(OrderBook &&) = delete;
        OrderBook &operator=(OrderBook &&) = delete;

        // Static instance
        static OrderBook *instance_;
        static std::once_flag initFlag_;

        // Private members
        std::map<double, std::list<Order>, std::greater<>> bids_;
        std::map<double, std::list<Order>> asks_;
        double lastTradedPrice_{0.0};

    public:
        // Static method to get the singleton instance
        static OrderBook &getInstance();

        void addOrder(const Order &o);
        void removeOrder(const Order &o);
        void updateLTP(double price);
        double getLTP() const;

        std::optional<std::reference_wrapper<Order>> getBestBid();
        std::optional<std::reference_wrapper<Order>> getBestAsk();
    };

} // namespace core
