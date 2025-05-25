#include "service/OrderMatchingService.hpp"
#include "models/LTP.hpp"
#include "models/OrderBookSnapshot.hpp"
#include <chrono>

namespace core
{

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
        models::OrderBookSnapshot snapshot(book_); // Pass the order book to the constructor
        obProd_.publish(snapshot);
    }

} // namespace core
