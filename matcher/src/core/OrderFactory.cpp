#include "core/OrderFactory.hpp"
#include "core/strategies/BuyMarketStrategy.hpp"
#include "core/strategies/BuyLimitStrategy.hpp"
#include "core/strategies/SellMarketStrategy.hpp"
#include "core/strategies/SellLimitStrategy.hpp"

namespace core
{

    Order OrderFactory::create(const dto::OrderData &dto)
    {
        std::shared_ptr<IMatchingStrategy> strat;
        using Side = dto::Side;
        using Type = dto::OrderType;

        if (dto.side == Side::BUY && dto.type == Type::MARKET)
            strat = std::make_shared<BuyMarketStrategy>();
        else if (dto.side == Side::BUY && dto.type == Type::LIMIT)
            strat = std::make_shared<BuyLimitStrategy>();
        else if (dto.side == Side::SELL && dto.type == Type::MARKET)
            strat = std::make_shared<SellMarketStrategy>();
        else
            strat = std::make_shared<SellLimitStrategy>();

        return Order(dto, strat);
    }

} // namespace core
