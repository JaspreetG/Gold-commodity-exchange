/**
 * @file Status.cpp
 * @brief Implementation of the Status class.
 */

#include "models/Status.hpp"

namespace models
{

    Status::Status(std::string orderId, std::string userId, std::string side, int quantity, std::chrono::system_clock::time_point ts)
    {
        orderId_ = orderId;
        userId_ = userId;
        side_ = side;
        quantity_ = quantity;
        ts_ = ts;
    }
    const std::string &Status::orderId() const { return orderId_; }
    const std::string &Status::userId() const { return userId_; }
    const std::string &Status::side() const { return side_; }
    int Status::quantity() const { return quantity_; }
    std::chrono::system_clock::time_point Status::timestamp() const { return ts_; }

} // namespace models
