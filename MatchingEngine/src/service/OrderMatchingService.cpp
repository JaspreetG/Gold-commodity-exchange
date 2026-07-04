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
    /**
     * @brief Constructor for OrderMatchingService.
     * 
     * Initializes the service by acquiring the singleton instance of the OrderBook.
     * It also implicitly initializes the Kafka producers for trades, LTP, and orderbook snapshots.
     */
    OrderMatchingService::OrderMatchingService()
        : book_(OrderBook::getInstance())
    {
    }

    /**
     * @brief Core handler for processing an incoming order.
     * 
     * Takes an OrderData DTO, creates an Order object, and determines the appropriate 
     * matching strategy via the StrategyFactory. It then executes the strategy against 
     * the order book.
     * Finally, it publishes any resulting trades, the new Last Traded Price (LTP),
     * and the updated OrderBook snapshot to Kafka.
     * 
     * @param dto The data transfer object containing the incoming order's parameters.
     */
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
