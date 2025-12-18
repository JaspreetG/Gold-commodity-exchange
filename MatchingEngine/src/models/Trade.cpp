/**
 * @file Trade.cpp
 * @brief Implementation of the Trade class.
 */

#include "models/Trade.hpp"

namespace models
{

  Trade::Trade(std::string buyOrderId, std::string sellOrderId,
               std::string buyUserId, std::string sellUserId,
               double price, int qty,
               std::chrono::system_clock::time_point ts)
      : buyOrderId_(buyOrderId),
        sellOrderId_(sellOrderId),
        buyUserId_(buyUserId),
        sellUserId_(sellUserId),

        price_(price),
        quantity_(qty),
        ts_(ts)
  {
  }

  const std::string &Trade::buyOrderId() const { return buyOrderId_; }
  const std::string &Trade::sellOrderId() const { return sellOrderId_; }
  const std::string &Trade::buyUserId() const { return buyUserId_; }
  const std::string &Trade::sellUserId() const { return sellUserId_; }

  double Trade::price() const { return price_; }
  int Trade::quantity() const { return quantity_; }
  std::chrono::system_clock::time_point Trade::timestamp() const
  {
    return ts_;
  }

} // namespace models
