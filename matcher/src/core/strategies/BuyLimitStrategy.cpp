#include "core/strategies/BuyLimitStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include <chrono>
#include <algorithm>

namespace core
{

    BuyLimitStrategy::BuyLimitStrategy() = default;

    std::vector<models::Trade> BuyLimitStrategy::match(
        Order &incoming, OrderBook &book)
    {

        std::vector<models::Trade> trades;
        double qty = incoming.quantity();
        double limit = incoming.price();

        while (qty > 0)
        {
            auto bestAskPtr = book.getBestAsk();
            if (!bestAskPtr)
                break;
            auto &bestAsk = *bestAskPtr;
            if (bestAsk.price() > limit)
                break;

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
        if (incoming.quantity() > 0)
            book.addOrder(incoming);

        return trades;
    }

    BuyLimitStrategy::~BuyLimitStrategy() {}

} // namespace core
