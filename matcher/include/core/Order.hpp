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
        std::string id_;
        double quantity_;
        double price_;
        dto::Side side_;
        dto::OrderType type_;
        std::shared_ptr<core::IMatchingStrategy> strategy_;

    public:
        Order(const dto::OrderData &dto,
              std::shared_ptr<IMatchingStrategy> strat);

        const std::string &id() const;
        double quantity() const;
        double price() const;
        dto::Side side() const;
        dto::OrderType type() const;
        void setQuantity(double q);

        std::vector<models::Trade> match(OrderBook &book);
    };

} // namespace core
