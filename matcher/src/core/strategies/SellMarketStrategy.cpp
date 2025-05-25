#include "core/strategies/SellMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include <chrono>

namespace core {

std::vector<models::Trade> SellMarketStrategy::match(
    Order& incoming, OrderBook& book) {

    std::vector<models::Trade] trades;
    double qty = incoming.quantity();

    while (qty > 0) {
        auto bestBidOpt = book.getBestBid();
        if (!bestBidOpt) break;
        auto& bestBid = bestBidOpt->get();
        double tradeQty = std::min(qty, bestBid.quantity());
        double price = bestBid.price();

        trades.emplace_back(bestBid.id(), incoming.id(),
                            price, tradeQty,
                            std::chrono::system_clock::now());

        qty -= tradeQty;
        bestBid.setQuantity(bestBid.quantity() - tradeQty);

        if (bestBid.quantity() == 0)
            book.removeOrder(bestBid);
    }

    incoming.setQuantity(qty);
    if (! trades.empty())
        book.updateLTP(trades.back().price());

    return trades;
}

} // namespace core
