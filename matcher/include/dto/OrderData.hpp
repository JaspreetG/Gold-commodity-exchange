#pragma once
#include <string>
#include "core/IMatchingStrategy.hpp"

namespace dto
{

    enum class Side
    {
        BUY,
        SELL
    };
    enum class OrderType
    {
        MARKET,
        LIMIT
    };

    struct OrderData
    {
        std::string order_id;
        std::string user_id; // user ID
        int quantity;        // integer quantity
        double price;        // ignored for MARKET orders
        dto::Side side;
        dto::OrderType type;
    };

} // namespace dto
