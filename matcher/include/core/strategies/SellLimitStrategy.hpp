#pragma once
#include "core/IMatchingStrategy.hpp"

namespace core
{

    class SellLimitStrategy : public IMatchingStrategy
    {
    public:
        virtual ~SellLimitStrategy();
        std::vector<models::Trade>
        match(Order &incoming, OrderBook &book) override;
    };

} // namespace core
