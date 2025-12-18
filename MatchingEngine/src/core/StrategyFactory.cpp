/**
 * @file StrategyFactory.cpp
 * @brief Implementation of the StrategyFactory class.
 */

#include "core/StrategyFactory.hpp"
#include "core/strategies/BuyLimitStrategy.hpp"
#include "core/strategies/SellLimitStrategy.hpp"
#include "core/strategies/BuyMarketStrategy.hpp"
#include "core/strategies/SellMarketStrategy.hpp"

#include <stdexcept>

namespace core
{

    std::shared_ptr<IMatchingStrategy> StrategyFactory::create(const dto::OrderData &dto)
    {
        using namespace dto;

        if (dto.side == Side::BUY && dto.type == OrderType::LIMIT)
            return std::make_shared<BuyLimitStrategy>();
        else if (dto.side == Side::SELL && dto.type == OrderType::LIMIT)
            return std::make_shared<SellLimitStrategy>();
        else if (dto.side == Side::BUY && dto.type == OrderType::MARKET)
            return std::make_shared<BuyMarketStrategy>();
        else if (dto.side == Side::SELL && dto.type == OrderType::MARKET)
            return std::make_shared<SellMarketStrategy>();

        throw std::invalid_argument("Unsupported order type or side.");
    }

} // namespace core