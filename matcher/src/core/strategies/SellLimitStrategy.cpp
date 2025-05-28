#include "core/strategies/SellLimitStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include "core/strategies/BuyMarketStrategy.hpp"
#include <chrono>
#include <algorithm>

namespace core
{

    SellLimitStrategy::SellLimitStrategy() {}

    std::vector<models::Trade> SellLimitStrategy::match(
        Order &incoming, OrderBook &book)
    {
        std::vector<models::Trade> trades;
        double qty = incoming.quantity();
        double limit = incoming.price();

        while (qty > 0)
        {
            auto bestBidPtr = book.getBestBid();
            if (!bestBidPtr)
                break;
            auto &bestBid = *bestBidPtr;
            if (bestBid.price() < limit)
                break;

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
        if (incoming.quantity() > 0)
            book.addOrder(incoming);

        // After processing sell limit, process any pending buy market orders
        std::vector<models::Trade> marketTrades = BuyMarketStrategy::processQueue(book);
        trades.insert(trades.end(), marketTrades.begin(), marketTrades.end());

        return trades;
    }

    SellLimitStrategy::~SellLimitStrategy() {}

} // namespace core
