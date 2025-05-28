#include "core/strategies/SellMarketStrategy.hpp"
#include "core/OrderBook.hpp"
#include "core/Order.hpp"
#include <chrono>
#include <algorithm>
#include <deque>

namespace core
{
    std::deque<Order> SellMarketStrategy::sellMarketQueue;

    SellMarketStrategy::SellMarketStrategy() {}

    std::vector<models::Trade> SellMarketStrategy::match(Order &incoming, OrderBook &book)
    {
        sellMarketQueue.push_back(incoming);
        return processQueue(book);
    }

    std::vector<models::Trade> SellMarketStrategy::processQueue(OrderBook &book)
    {
        std::vector<models::Trade> allTrades;
        while (!sellMarketQueue.empty())
        {
            Order &frontOrder = sellMarketQueue.front();
            double qty = frontOrder.quantity();
            std::vector<models::Trade> trades;
            while (qty > 0)
            {
                Order *bestBidPtr = book.getBestBid();
                if (!bestBidPtr)
                    break;
                Order &bestBid = *bestBidPtr;
                double tradeQty = std::min(qty, bestBid.quantity());
                double price = bestBid.price();
                trades.emplace_back(bestBid.id(), frontOrder.id(), price, tradeQty, std::chrono::system_clock::now());
                qty -= tradeQty;
                bestBid.setQuantity(bestBid.quantity() - tradeQty);
                if (bestBid.quantity() == 0)
                    book.removeOrder(bestBid);
            }
            frontOrder.setQuantity(qty);
            if (!trades.empty())
                book.updateLTP(trades.back().price());
            allTrades.insert(allTrades.end(), trades.begin(), trades.end());
            if (qty > 0)
                // Not fully filled, keep at front and break
                break;
            // Fully filled, pop from queue
            sellMarketQueue.pop_front();
        }
        return allTrades;
    }

    SellMarketStrategy::~SellMarketStrategy() {}
} // namespace core
