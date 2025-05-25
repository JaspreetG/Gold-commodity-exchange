#pragma once
#include <memory>
#include "dto/OrderData.hpp"
#include "core/Order.hpp"

namespace core {

class OrderFactory {
public:
    static Order create(const dto::OrderData& dto);
};

} // namespace core
