#include "models/LTP.hpp"

namespace models {

LTP::LTP(double price, std::chrono::system_clock::time_point ts)
    : price_(price), ts_(ts) {}

double LTP::price() const { return price_; }
std::chrono::system_clock::time_point LTP::timestamp() const {
    return ts_;
}

} // namespace models
