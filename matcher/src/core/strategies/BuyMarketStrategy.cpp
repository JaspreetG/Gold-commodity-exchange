#include "core/strategies/BuyMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include <chrono>
#include <algorithm>

namespace core
{

    std::vector<models::Trade> BuyMarketStrategy::match(
        Order &incoming, OrderBook &book)
    {

        std::vector<models::Trade> trades;
        double qty = incoming.quantity();

        while (qty > 0)
        {
            auto bestAskOpt = book.getBestAsk();
            if (!bestAskOpt)
                break;
            auto &bestAsk = bestAskOpt->get();
            double tradeQty = std::min(qty, bestAsk.quantity());
            double price = bestAsk.price();

            trades.emplace_back(incoming.id(), bestAsk.id(),
                                price, tradeQty,
                                std::chrono::system_clock::now());

            qty -= tradeQty;
            bestAsk.setQuantity(bestAsk.quantity() - tradeQty);

            if (bestAsk.quantity() == 0)
                book.removeOrder(bestAsk);
        }

        incoming.setQuantity(qty);
        if (!trades.empty())
            book.updateLTP(trades.back().price());

        return trades;
    }

} // namespace core
