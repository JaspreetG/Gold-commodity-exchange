#include "service/OrderMatchingService.hpp"
#include "models/LTP.hpp"
#include "models/OrderBookSnapshot.hpp"
#include <chrono>

namespace core
{

    OrderMatchingService::OrderMatchingService()
        : tradeProd_(), ltpProd_(), obProd_() // Initialize producers
    {
    }

    void OrderMatchingService::handleOrder(const dto::OrderData &dto)
    {
        Order o = OrderFactory::create(dto);
        auto trades = o.match(book_);
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
        models::OrderBookSnapshot snapshot(book_.bids_, book_.asks_, std::chrono::system_clock::now()); // Pass the order book to the constructor
        obProd_.publish(snapshot);
    }

} // namespace core
