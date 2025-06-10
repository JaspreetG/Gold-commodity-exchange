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
        int qty = incoming.quantity();
        double limit = incoming.price();

        while (qty > 0)
        {
            auto bestBidPtr = book.getBestBid();
            if (!bestBidPtr)
                break;
            auto &bestBid = *bestBidPtr;
            if (bestBid.price() < limit)
                break;

            int tradeQty = std::min(qty, bestBid.quantity());
            double price = bestBid.price();

            trades.push_back(models::Trade(incoming.order_id(), bestBid.order_id(), incoming.user_id(), bestBid.user_id(),
                                           price, tradeQty,
                                           std::chrono::system_clock::now()));
            ;
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

        incoming.setQuantity(qty);
        if (!trades.empty())
            book.updateLTP(trades.back().price());
        if (incoming.quantity() > 0)
            book.addOrder(incoming);

        return trades;
    }

    SellLimitStrategy::~SellLimitStrategy() {}

} // namespace core
