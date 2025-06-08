#pragma once
#include <string>
#include <chrono>

namespace models
{

    class Status
    {
        std::string orderId_;

        std::string userId_;
        int quantity_;
        std::chrono::system_clock::time_point ts_;

    public:
        Status(std::string orderId, std::string userId,
               int quantity, std::chrono::system_clock::time_point ts);

        const std::string &orderId() const;
        const std::string &userId() const;
        int quantity() const;
        std::chrono::system_clock::time_point timestamp() const;
    };

} // namespace models
