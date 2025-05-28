#pragma once
#include "core/IMatchingStrategy.hpp"
#include <deque>

namespace core
{

    class SellMarketStrategy : public IMatchingStrategy
    {
    public:
        SellMarketStrategy();
        std::vector<models::Trade> match(Order &incoming, OrderBook &book) override;
        static std::vector<models::Trade> processQueue(OrderBook &book);
        ~SellMarketStrategy();

    private:
        static std::deque<Order> sellMarketQueue;
    };

} // namespace core
