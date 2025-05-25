#pragma once
#include <chrono>

namespace models {

class LTP {
    double price_;
    std::chrono::system_clock::time_point ts_;

public:
    LTP(double price, std::chrono::system_clock::time_point ts);
    double price() const;
    std::chrono::system_clock::time_point timestamp() const;
};

} // namespace models
