#pragma once
#include "core/IMatchingStrategy.hpp"

namespace core
{

    class BuyLimitStrategy : public IMatchingStrategy
    {
    public:
        BuyLimitStrategy();
        virtual ~BuyLimitStrategy();
        std::vector<models::Trade>
        match(Order &incoming, OrderBook &book) override;
    };

} // namespace core
