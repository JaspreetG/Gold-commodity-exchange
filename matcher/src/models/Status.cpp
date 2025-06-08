#include "models/Status.hpp"

namespace models
{

    Status::Status(std::string orderId, std::string userId, int quantity, std::chrono::system_clock::time_point ts)
    {
        orderId_ = orderId;
        userId_ = userId;
        quantity_ = quantity;
        ts_ = ts;
    }
    const std::string &Status::orderId() const { return orderId_; }
    const std::string &Status::userId() const { return userId_; }
    int Status::quantity() const { return quantity_; }

} // namespace models
