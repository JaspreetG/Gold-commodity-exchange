#pragma once
#include "core/IMatchingStrategy.hpp"
#include <deque>

namespace core
{

    /**
     * @class BuyMarketStrategy
     * @brief Strategy for matching Buy Market orders.
     */
    class BuyMarketStrategy : public IMatchingStrategy
    {
    public:
        BuyMarketStrategy();

        /**
         * @brief Matches a Buy Market order against the order book.
         * @param incoming The incoming Buy Market order.
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
        ~BuyMarketStrategy();

    private:
        static std::deque<Order> buyMarketQueue;
    };

} // namespace core
