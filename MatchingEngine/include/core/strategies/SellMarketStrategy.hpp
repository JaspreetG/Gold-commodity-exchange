#pragma once
#include "core/IMatchingStrategy.hpp"
#include <deque>

namespace core
{

    /**
     * @class SellMarketStrategy
     * @brief Strategy for matching Sell Market orders.
     */
    class SellMarketStrategy : public IMatchingStrategy
    {
    public:
        SellMarketStrategy();

        /**
         * @brief Matches a Sell Market order against the order book.
         * @param incoming The incoming Sell Market order.
         * @param book The order book.
         * @return A vector of executed trades.
         */
        std::vector<models::Trade> match(Order &incoming, OrderBook &book) override;

        /**
         * @brief Processes the queue of pending market orders.
         * @param book The order book.
         * @return A vector of executed trades from the queue.
         */
        static std::vector<models::Trade> processQueue(OrderBook &book);
        ~SellMarketStrategy();

    private:
        static std::deque<Order> sellMarketQueue;
    };

} // namespace core
