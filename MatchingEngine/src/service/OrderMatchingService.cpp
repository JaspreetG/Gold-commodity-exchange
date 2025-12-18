/**
 * @file OrderMatchingService.cpp
 * @brief Implementation of the OrderMatchingService class.
 */

#include "service/OrderMatchingService.hpp"
#include "models/LTP.hpp"
#include "models/OrderBookSnapshot.hpp"
#include "core/StrategyFactory.hpp"
#include <chrono>
#include <iostream>

namespace core
{
    OrderMatchingService::OrderMatchingService()
        : book_(OrderBook::getInstance()), tradeProd_(), ltpProd_(), obProd_() // Initialize all members
    {
    }

    void OrderMatchingService::handleOrder(const dto::OrderData &dto)
    {

        Order order = OrderFactory::create(dto);
        std::shared_ptr<IMatchingStrategy> strategy = StrategyFactory::create(dto);
        auto trades = strategy->match(order, book_);
        for (auto &t : trades)
        {
            tradeProd_.publish(t);
        }
        if (!trades.empty())
        {
            models::LTP ltp(book_.getLTP(),
                            std::chrono::system_clock::now());
            ltpProd_.publish(ltp);
        }

        models::OrderBookSnapshot snapshot(book_.getBids(), book_.getAsks(), std::chrono::system_clock::now()); // Pass the order book to the constructor
        obProd_.publish(snapshot);
    }

} // namespace core
