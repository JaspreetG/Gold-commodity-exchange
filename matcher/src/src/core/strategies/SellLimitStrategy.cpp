#include "core/strategies/SellLimitStrategy.hpp"
#include "core/OrderBook.hpp"
#include <chrono>

namespace core {

std::vector<models::Trade> SellLimitStrategy::match(
    Order& incoming, OrderBook& book) {

    std::vector<models::Trade> trades;
    double qty = incoming.quantity();
    double limit = incoming.price();

    while (qty > 0) {
        auto bestBidOpt = book.getBestBid();
        if (!bestBidOpt) break;
        auto& bestBid = bestBidOpt->get();
        if (bestBid.price() < limit) break;

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
    else if (incoming.quantity() > 0)
        book.addOrder(incoming);

    return trades;
}

} // namespace core
