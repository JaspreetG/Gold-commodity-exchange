#pragma once
#include <vector>
#include "models/Trade.hpp"

namespace core
{
    class Order;     // Forward declaration
    class OrderBook; // Forward declaration

    class IMatchingStrategy
    {
    public:
        virtual ~IMatchingStrategy() = default;
        virtual std::vector<models::Trade> match(Order &incoming, OrderBook &book) = 0;
    };

} // namespace core
