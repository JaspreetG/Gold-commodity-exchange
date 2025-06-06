#pragma once
#include <string>
#include <chrono>

namespace models
{

    class Trade
    {
        std::string buyOrderId_;
        std::string sellOrderId_;
        double price_;
        int quantity_;
        std::chrono::system_clock::time_point ts_;

    public:
        Trade(std::string buyId, std::string sellId,
              double price, int qty,
              std::chrono::system_clock::time_point ts);

        const std::string &buyOrderId() const;
        const std::string &sellOrderId() const;
        double price() const;
        int quantity() const;
        std::chrono::system_clock::time_point timestamp() const;
    };

} // namespace models
