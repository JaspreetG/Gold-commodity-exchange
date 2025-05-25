#include "models/Trade.hpp"

namespace models {

Trade::Trade(std::string buyId, std::string sellId,
             double price, double qty,
             std::chrono::system_clock::time_point ts)
    : buyOrderId_(std::move(buyId)),
      sellOrderId_(std::move(sellId)),
      price_(price),
      quantity_(qty),
      ts_(ts) {}

const std::string& Trade::buyOrderId() const { return buyOrderId_; }
const std::string& Trade::sellOrderId() const { return sellOrderId_; }
double Trade::price() const { return price_; }
double Trade::quantity() const { return quantity_; }
std::chrono::system_clock::time_point Trade::timestamp() const {
    return ts_;
}

} // namespace models
