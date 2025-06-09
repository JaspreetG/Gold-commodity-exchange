#include "core/Order.hpp"
#include "core/IMatchingStrategy.hpp"
#include <iostream>
namespace core
{

  Order::Order(const dto::OrderData &dto,
               std::shared_ptr<IMatchingStrategy> strat)
      : order_id_(dto.order_id),
        user_id_(dto.user_id),
        quantity_(dto.quantity),
        price_(dto.price),
        side_(dto.side),
        type_(dto.type),
        strategy_(std::move(strat)) {}

  const std::string &Order::order_id() const { return order_id_; }
  const std::string &Order::user_id() const { return user_id_; }
  int Order::quantity() const { return quantity_; }
  double Order::price() const { return price_; }
  dto::Side Order::side() const { return side_; }
  dto::OrderType Order::type() const { return type_; }
  void Order::setQuantity(int q) { quantity_ = q; }

  std::vector<models::Trade> Order::match(OrderBook &book)
  {
    return strategy_->match(*this, book);
  }

} // namespace core