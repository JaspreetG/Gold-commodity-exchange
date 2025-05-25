#include "core/OrderBook.hpp"
#include <algorithm>
#include <stdexcept>

namespace core {

void OrderBook::addOrder(const Order& o) {
    if (o.side() == dto::Side::BUY)
        bids_[o.price()].push_back(o);
    else
        asks_[o.price()].push_back(o);
}

void OrderBook::removeOrder(const Order& o) {
    auto& book = (o.side() == dto::Side::BUY ? bids_ : asks_);
    auto it = book.find(o.price());
    if (it == book.end()) return;
    it->second.remove_if([&](const Order& ord) { return ord.id() == o.id(); });
    if (it->second.empty()) book.erase(it);
}

void OrderBook::updateLTP(double price) {
    lastTradedPrice_ = price;
}

double OrderBook::getLTP() const {
    return lastTradedPrice_;
}

std::optional<std::reference_wrapper<Order>> OrderBook::getBestBid() {
    if (bids_.empty()) return std::nullopt;
    auto& lst = bids_.begin()->second;
    return lst.empty() ? std::nullopt : std::ref(lst.front());
}

std::optional<std::reference_wrapper<Order>> OrderBook::getBestAsk() {
    if (asks_.empty()) return std::nullopt;
    auto& lst = asks_.begin()->second;
    return lst.empty() ? std::nullopt : std::ref(lst.front());
}

} // namespace core
