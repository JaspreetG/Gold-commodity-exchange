import { useState, useEffect } from "react";
import OrderBook from "./OrderBook";
import TradingForm from "./TradingForm";
import TradeHistory from "./TradeHistory";
import { OrderBook as OrderBookType, Trade } from "@/types/trading";

interface TradingInterfaceProps {
  userBalances: {
    usd: number;
    gold: number;
  };
  updateBalance: (type: "usd" | "gold", amount: number) => void;
}

const TradingInterface = ({
  userBalances,
  updateBalance,
}: TradingInterfaceProps) => {
  const [orderBook, setOrderBook] = useState<OrderBookType>({
    bids: [],
    asks: [],
  });
  const [trades, setTrades] = useState<Trade[]>([]);
  const [currentPrice, setCurrentPrice] = useState(2050);

  // Generate mock order book data
  useEffect(() => {
    const generateOrderBook = () => {
      const bids = [];
      const asks = [];

      // Generate bid orders (buy orders)
      for (let i = 0; i < 10; i++) {
        const price = currentPrice - (i + 1) * Math.random() * 5;
        const quantity = Math.random() * 2 + 0.1;
        bids.push({
          price: parseFloat(price.toFixed(2)),
          quantity: parseFloat(quantity.toFixed(3)),
          total: parseFloat((price * quantity).toFixed(2)),
        });
      }

      // Generate ask orders (sell orders)
      for (let i = 0; i < 10; i++) {
        const price = currentPrice + (i + 1) * Math.random() * 5;
        const quantity = Math.random() * 2 + 0.1;
        asks.push({
          price: parseFloat(price.toFixed(2)),
          quantity: parseFloat(quantity.toFixed(3)),
          total: parseFloat((price * quantity).toFixed(2)),
        });
      }

      setOrderBook({ bids, asks });
    };

    generateOrderBook();
    const interval = setInterval(generateOrderBook, 5000);
    return () => clearInterval(interval);
  }, [currentPrice]);

  // Generate mock trade history
  useEffect(() => {
    const mockTrades: Trade[] = [
      {
        id: "1",
        type: "buy",
        price: 2048.5,
        quantity: 0.5,
        total: 1024.25,
        timestamp: new Date(Date.now() - 3600000),
        fee: 2.05,
      },
      {
        id: "2",
        type: "sell",
        price: 2052.0,
        quantity: 0.25,
        total: 513.0,
        timestamp: new Date(Date.now() - 7200000),
        fee: 1.03,
      },
    ];
    setTrades(mockTrades);
  }, []);

  const addTrade = (trade: Omit<Trade, "id" | "timestamp">) => {
    const newTrade: Trade = {
      ...trade,
      id: Date.now().toString(),
      timestamp: new Date(),
    };
    setTrades((prev) => [newTrade, ...prev]);
  };

  return (
    <div className="grid grid-cols-1 xl:grid-cols-2 gap-8">
      <div>
        <OrderBook orderBook={orderBook} currentPrice={currentPrice} />
      </div>

      <div>
        <TradingForm
          userBalances={userBalances}
          updateBalance={updateBalance}
          currentPrice={currentPrice}
          onTrade={addTrade}
        />
      </div>

      <div className="xl:col-span-2">
        <TradeHistory trades={trades} />
      </div>
    </div>
  );
};

export default TradingInterface;
