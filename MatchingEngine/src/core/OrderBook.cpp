#include "core/Order.hpp"
#include "core/OrderBook.hpp"
#include <algorithm>
#include <stdexcept>
#include <mutex>
#include <thread>
#include <optional>

namespace core
{

    // Initialize static members
    OrderBook *OrderBook::instance_ = nullptr;
    std::once_flag OrderBook::initFlag_;

    /**
     * @brief Adds an order to the order book.
     *
     * Adds the order to the appropriate side (BUY/SELL) based on the order details.
     * Orders are stored in a map where the key is the price, and the value is a list of orders.
     *
     * @param order The order to add.
     */
    void OrderBook::addOrder(const Order &order)
    {
        if (order.side() == dto::Side::BUY)
            bids_[order.price()].push_back(order);
        else
            asks_[order.price()].push_back(order);
    }

    /**
     * @brief Removes an order from the order book.
     *
     * Searches for the order by price and then removes it from the list of orders at that price.
     * If the list becomes empty after removal, the price level is removed from the map.
     *
     * @param order The order to remove.
     */
    void OrderBook::removeOrder(const Order &order)
    {
        if (order.side() == dto::Side::BUY)
        {
            auto it = bids_.find(order.price());
            if (it == bids_.end())
                return;

            // Remove order with matching ID
            it->second.remove_if([&](const Order &ord)
                                 { return ord.order_id() == order.order_id(); });

            // Clean up price level if empty
            if (it->second.empty())
                bids_.erase(it);
        }
        else
        {
            auto it = asks_.find(order.price());
            if (it == asks_.end())
                return;

             // Remove order with matching ID
            it->second.remove_if([&](const Order &ord)
                                 { return ord.order_id() == order.order_id(); });

            // Clean up price level if empty
            if (it->second.empty())
                asks_.erase(it);
        }
    }

    /**
     * @brief Updates the Last Traded Price (LTP).
     *
     * @param price The new last traded price.
     */
    void OrderBook::updateLTP(double price)
    {
        lastTradedPrice_ = price;
    }

    /**
     * @brief Gets the Last Traded Price (LTP).
     *
     * @return double The last traded price.
     */
    double OrderBook::getLTP() const
    {
        return lastTradedPrice_;
    }

    /**
     * @brief Retrieves the singleton instance of the OrderBook.
     *
     * Uses double-checked locking (via std::call_once) to ensure thread safety.
     *
     * @return OrderBook& The singleton instance.
     */
    OrderBook &OrderBook::getInstance()
    {
        std::call_once(initFlag_, []()
                       { instance_ = new OrderBook(); });
        return *instance_;
    }

    /**
     * @brief Gets the best (highest) bid order.
     *
     * @return Order* Pointer to the best bid order, or nullptr if no bids exist.
     */
    Order *OrderBook::getBestBid()
    {
        if (bids_.empty())
            return nullptr;

        // Bids are sorted descending (std::greater), so begin() is the highest price.
        auto &orderList = bids_.begin()->second;

        return orderList.empty() ? nullptr : &orderList.front();
    }

    /**
     * @brief Gets the best (lowest) ask order.
     *
     * @return Order* Pointer to the best ask order, or nullptr if no asks exist.
     */
    Order *OrderBook::getBestAsk()
    {
        if (asks_.empty())
            return nullptr;

        // Asks are sorted ascending (default), so begin() is the lowest price.
        auto &orderList = asks_.begin()->second;

        return orderList.empty() ? nullptr : &orderList.front();
    }

    /**
     * @brief Retrieves all current buy orders (bids).
     *
     * @return std::map<double, std::list<Order>, std::greater<>> Map of bids.
     */
    std::map<double, std::list<Order>, std::greater<>> OrderBook::getBids()
    {
        return bids_;
    }

    /**
     * @brief Retrieves all current sell orders (asks).
     *
     * @return std::map<double, std::list<Order>> Map of asks.
     */
    std::map<double, std::list<Order>> OrderBook::getAsks()
    {
        return asks_;
    }
} // namespace core
