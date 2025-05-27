#include "core/Order.hpp"
#include "core/OrderBook.hpp"
#include <algorithm>
#include <stdexcept>
#include <mutex>
#include <thread>
#include <optional> // Include for std::optional

namespace core
{

    // Initialize static members
    OrderBook *OrderBook::instance_ = nullptr;
    std::once_flag OrderBook::initFlag_;

    void OrderBook::addOrder(const Order &o)
    {
        if (o.side() == dto::Side::BUY)
            bids_[o.price()].push_back(o);
        else
            asks_[o.price()].push_back(o);
    }

    void OrderBook::removeOrder(const Order &o)
    {
        if (o.side() == dto::Side::BUY)
        {
            auto it = bids_.find(o.price());
            if (it == bids_.end())
                return;
            it->second.remove_if([&](const Order &ord)
                                 { return ord.id() == o.id(); });
            if (it->second.empty())
                bids_.erase(it);
        }
        else
        {
            auto it = asks_.find(o.price());
            if (it == asks_.end())
                return;
            it->second.remove_if([&](const Order &ord)
                                 { return ord.id() == o.id(); });
            if (it->second.empty())
                asks_.erase(it);
        }
    }

    void OrderBook::updateLTP(double price)
    {
        lastTradedPrice_ = price;
    }

    double OrderBook::getLTP() const
    {
        return lastTradedPrice_;
    }

    OrderBook &OrderBook::getInstance()
    {
        std::call_once(initFlag_, []()
                       { instance_ = new OrderBook(); });
        return *instance_;
    }

    Order *OrderBook::getBestBid()
    {
        if (bids_.empty())
            return nullptr;

        auto &lst = bids_.begin()->second;

        return lst.empty() ? nullptr : &lst.front();
    }

    Order *OrderBook::getBestAsk(){
        if(asks_.empty()) return nullptr;

        auto &lst = asks_.begin()->second;

        return lst.empty() ? nullptr : &lst.front();
    }

    // const std::map<double, std::list<Order>, std::greater<>> &OrderBook::getBids() const
    // {
    //     return bids_;
    // }

    // const std::map<double, std::list<Order>> &OrderBook::getAsks() const
    // {
    //     return asks_;
    // }

    // std::optional<std::reference_wrapper<Order>> OrderBook::getBestBid()
    // {
    //     if (bids_.empty())
    //         return std::nullopt;
    //     auto &lst = bids_.begin()->second;
    //     return lst.empty() ? std::nullopt : std::optional<std::reference_wrapper<Order>>(std::ref(lst.front()));
    // }

    // std::optional<std::reference_wrapper<Order>> OrderBook::getBestAsk()
    // {
    //     if (asks_.empty())
    //         return std::nullopt;
    //     auto &lst = asks_.begin()->second;
    //     return lst.empty() ? std::nullopt : std::optional<std::reference_wrapper<Order>>(std::ref(lst.front()));
    // }

    

} // namespace core
