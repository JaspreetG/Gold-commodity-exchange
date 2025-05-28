#pragma once
#include "core/IMatchingStrategy.hpp"
#include <deque>

namespace core
{

    class BuyMarketStrategy : public IMatchingStrategy
    {
    public:
        BuyMarketStrategy();
        std::vector<models::Trade> match(Order &incoming, OrderBook &book) override;
        static std::vector<models::Trade> processQueue(OrderBook &book);
        ~BuyMarketStrategy();

    private:
        static std::deque<Order> buyMarketQueue;
    };

} // namespace core
