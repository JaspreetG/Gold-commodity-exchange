#include "core/strategies/BuyMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp" // Include for full definition of Order
#include <chrono>
#include <algorithm>
#include <deque>

namespace core
{
    std::deque<Order> BuyMarketStrategy::buyMarketQueue;
    BuyMarketStrategy::BuyMarketStrategy() {}

    std::vector<models::Trade> BuyMarketStrategy::match(Order &incoming, OrderBook &book)
    {
        buyMarketQueue.push_back(incoming);
        return processQueue(book);
    }

    std::vector<models::Trade> BuyMarketStrategy::processQueue(OrderBook &book)
    {
        std::vector<models::Trade> allTrades;
        while (!buyMarketQueue.empty())
        {
            Order &frontOrder = buyMarketQueue.front();
            double qty = frontOrder.quantity();
            std::vector<models::Trade> trades;
            while (qty > 0)
            {
                Order *bestAskPtr = book.getBestAsk();
                if (!bestAskPtr)
                    break;
                Order &bestAsk = *bestAskPtr;
                double tradeQty = std::min(qty, bestAsk.quantity());
                double price = bestAsk.price();
                trades.emplace_back(frontOrder.id(), bestAsk.id(),
                                    price, tradeQty,
                                    std::chrono::system_clock::now());
                qty -= tradeQty;
                bestAsk.setQuantity(bestAsk.quantity() - tradeQty);
                if (bestAsk.quantity() == 0)
                    book.removeOrder(bestAsk);
            }
            frontOrder.setQuantity(qty);
            if (!trades.empty())
                book.updateLTP(trades.back().price());
            allTrades.insert(allTrades.end(), trades.begin(), trades.end());
            if (qty > 0)
                // Not fully filled, keep at front and break
                break;
            // Fully filled, pop from queue
            buyMarketQueue.pop_front();
        }
        return allTrades;
    }

    BuyMarketStrategy::~BuyMarketStrategy() {}

} // namespace core
