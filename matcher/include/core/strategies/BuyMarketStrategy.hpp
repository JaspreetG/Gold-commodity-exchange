#pragma once
#include "core/IMatchingStrategy.hpp"

namespace core {

class BuyMarketStrategy : public IMatchingStrategy {
public:
    std::vector<models::Trade>
    match(Order& incoming, OrderBook& book) override;
};

} // namespace core
