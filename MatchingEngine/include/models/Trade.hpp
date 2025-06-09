#pragma once
#include <string>
#include <chrono>

namespace models
{

    class Trade
    {
        std::string buyOrderId_;
        std::string sellOrderId_;
        std::string buyUserId_;
        std::string sellUserId_;
        double price_;
        int quantity_;
        std::chrono::system_clock::time_point ts_;

    public:
        Trade(std::string buyOrderId, std::string sellOrderId, std::string buyUserId, std::string sellUserId, double price, int qty, std::chrono::system_clock::time_point ts);

        const std::string &buyOrderId() const;
        const std::string &sellOrderId() const;
        const std::string &buyUserId() const;
        const std::string &sellUserId() const;
        double price() const;
        int quantity() const;
        std::chrono::system_clock::time_point timestamp() const;
    };

} // namespace models
