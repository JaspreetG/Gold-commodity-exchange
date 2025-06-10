#include "core/strategies/BuyMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include <chrono>
#include <algorithm>
#include <deque>

namespace core
{
    BuyMarketStrategy::BuyMarketStrategy() {}

    std::vector<models::Trade> BuyMarketStrategy::match(Order &incoming, OrderBook &book)
    {
        int qty = incoming.quantity();
        std::vector<models::Trade> trades;
        while (qty > 0)
        {
            Order *bestAskPtr = book.getBestAsk();
            if (!bestAskPtr)
            {
                IMatchingStrategy::statusProducer.publish(
                    models::Status(incoming.order_id(), incoming.user_id(), "BUY",
                                   0, std::chrono::system_clock::now()));
                break;
            }
            Order &bestAsk = *bestAskPtr;
            int tradeQty = std::min(qty, bestAsk.quantity());
            double price = bestAsk.price();
            trades.emplace_back(models::Trade(incoming.order_id(), bestAsk.order_id(), incoming.user_id(), bestAsk.user_id(),
                                              price, tradeQty,
                                              std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(incoming.order_id(), incoming.user_id(), "BUY",
                               tradeQty, std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(bestAsk.order_id(), bestAsk.user_id(),
                               "SELL", tradeQty, std::chrono::system_clock::now()));
            qty -= tradeQty;
            bestAsk.setQuantity(bestAsk.quantity() - tradeQty);
            if (bestAsk.quantity() == 0)
                book.removeOrder(bestAsk);
        }
        if (!trades.empty())
            book.updateLTP(trades.back().price());
        return trades;
    }

    BuyMarketStrategy::~BuyMarketStrategy() {}

} // namespace core
