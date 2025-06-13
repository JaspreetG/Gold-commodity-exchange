#pragma once

#include "core/IMatchingStrategy.hpp"
#include "dto/OrderData.hpp"
#include <memory>

namespace core
{

    class StrategyFactory
    {
    public:
        static std::shared_ptr<IMatchingStrategy> create(const dto::OrderData &dto);
    };

} // namespace core