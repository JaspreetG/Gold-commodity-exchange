#pragma once
#include <memory>
#include <vector>
#include "dto/OrderData.hpp"
#include "core/IMatchingStrategy.hpp"
#include "models/Trade.hpp"

namespace core
{

    class OrderBook;

    class Order
    {
    private:
        std::string order_id_;
        std::string user_id_;
        int quantity_;
        double price_;
        dto::Side side_;
        dto::OrderType type_;

    public:
        Order(const dto::OrderData &dto);

        const std::string &order_id() const;
        const std::string &user_id() const;
        int quantity() const;
        double price() const;
        dto::Side side() const;
        dto::OrderType type() const;
        void setQuantity(int q);
    };

} // namespace core
