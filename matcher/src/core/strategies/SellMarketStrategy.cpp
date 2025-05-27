#include "core/strategies/SellMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include <chrono>
#include <algorithm>

namespace core
{
    SellMarketStrategy::SellMarketStrategy() = default;

    std::vector<models::Trade> SellMarketStrategy::match(
        Order &incoming, OrderBook &book)
    {

        std::vector<models::Trade> trades;
        double qty = incoming.quantity();

        while (qty > 0)
        {
            auto bestBidPtr = book.getBestBid();
            if (!bestBidPtr)
                break;
            auto &bestBid = *bestBidPtr;
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
        if (!trades.empty())
            book.updateLTP(trades.back().price());

        return trades;
    }

    SellMarketStrategy::~SellMarketStrategy() {}
} // namespace core
