#include "core/strategies/SellMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp"
#include <chrono>
#include <algorithm>
#include <deque>

namespace core
{
    SellMarketStrategy::SellMarketStrategy() {}

    std::vector<models::Trade> SellMarketStrategy::match(Order &incoming, OrderBook &book)
    {
        int qty = incoming.quantity();
        std::vector<models::Trade> trades;
        while (qty > 0)
        {
            Order *bestBidPtr = book.getBestBid();
            if (!bestBidPtr)
            {
                IMatchingStrategy::statusProducer.publish(
                    models::Status(incoming.order_id(), incoming.user_id(),
                                   "SELL",
                                   0, std::chrono::system_clock::now()));
                break;
            }
            Order &bestBid = *bestBidPtr;
            int tradeQty = std::min(qty, bestBid.quantity());
            double price = bestBid.price();
            trades.emplace_back(models::Trade(incoming.order_id(), bestBid.order_id(), incoming.user_id(), bestBid.user_id(),
                                              price, tradeQty,
                                              std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(incoming.order_id(), incoming.user_id(),
                               "SELL",
                               tradeQty, std::chrono::system_clock::now()));
            IMatchingStrategy::statusProducer.publish(
                models::Status(bestBid.order_id(), bestBid.user_id(),
                               "BUY",
                               tradeQty, std::chrono::system_clock::now()));
            qty -= tradeQty;
            bestBid.setQuantity(bestBid.quantity() - tradeQty);
            if (bestBid.quantity() == 0)
                book.removeOrder(bestBid);
        }

        if (!trades.empty())
            book.updateLTP(trades.back().price());
        return trades;
    }

    SellMarketStrategy::~SellMarketStrategy() {}
} // namespace core
