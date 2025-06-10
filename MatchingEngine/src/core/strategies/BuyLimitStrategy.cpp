#include "core/strategies/BuyLimitStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include "core/strategies/SellMarketStrategy.hpp"
#include <chrono>
#include <algorithm>

namespace core
{

    BuyLimitStrategy::BuyLimitStrategy() {}

    std::vector<models::Trade> BuyLimitStrategy::match(
        Order &incoming, OrderBook &book)
    {

        std::vector<models::Trade> trades;
        int qty = incoming.quantity();
        double limit = incoming.price();

        while (qty > 0)
        {
            auto bestAskPtr = book.getBestAsk();
            if (!bestAskPtr)
                break;
            auto &bestAsk = *bestAskPtr;
            if (bestAsk.price() > limit)
                break;

            int tradeQty = std::min(qty, bestAsk.quantity());
            double price = bestAsk.price();

            trades.emplace_back(models::Trade(incoming.order_id(), bestAsk.order_id(), incoming.user_id(), bestAsk.user_id(),
                                              price, tradeQty,
                                              std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(incoming.order_id(), incoming.user_id(),
                               "BUY",
                               tradeQty, std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(bestAsk.order_id(), bestAsk.user_id(),
                               "SELL",
                               tradeQty, std::chrono::system_clock::now()));
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
