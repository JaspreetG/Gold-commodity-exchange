/**
 * @file OrderFactory.cpp
 * @brief Implementation of the OrderFactory class.
 */

#include "core/OrderFactory.hpp"

namespace core
{

    Order OrderFactory::create(const dto::OrderData &dto)
    {
        return Order(dto);
    }

} // namespace core
