/**
 * @file Order.cpp
 * @brief Implementation of the Order class.
 */

#include "core/Order.hpp"
#include "core/IMatchingStrategy.hpp"
#include <iostream>
namespace core
{

  Order::Order(const dto::OrderData &dto)
      : order_id_(dto.order_id),
        user_id_(dto.user_id),
        quantity_(dto.quantity),
        price_(dto.price),
        side_(dto.side),
        type_(dto.type) {}

  const std::string &Order::order_id() const { return order_id_; }
  const std::string &Order::user_id() const { return user_id_; }
  int Order::quantity() const { return quantity_; }
  double Order::price() const { return price_; }
  dto::Side Order::side() const { return side_; }
  dto::OrderType Order::type() const { return type_; }
  void Order::setQuantity(int q) { quantity_ = q; }
} // namespace core