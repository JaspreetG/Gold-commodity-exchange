#pragma once
#include <vector>
#include "models/Trade.hpp"
#include "core/Order.hpp"
#include "core/OrderBook.hpp"

namespace core {

class IMatchingStrategy {
public:
    virtual ~IMatchingStrategy() = default;
    virtual std::vector<models::Trade>
    match(Order& incoming, OrderBook& book) = 0;
};

} // namespace core
