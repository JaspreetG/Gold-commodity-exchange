import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useAuthStore } from "@/store/useAuthStore";
import { Clock, FileCheck, ShieldCheck } from "lucide-react";
import React, { useEffect } from "react";

// interface pastTradesHistoryProps {
//   pastTradess: pastTrades[];
// }

const TradeHistory = () => {
  const { getTradeHistory, isGettingTradeHistory, pastTrades } = useAuthStore();

  useEffect(() => {
    const intervalId = setInterval(() => {
      getTradeHistory();
    }, 3000); // 3000ms = 3 seconds

    // Optionally call immediately on mount
    getTradeHistory();

    return () => clearInterval(intervalId); // Cleanup on unmount
  }, [getTradeHistory]);

  return (
    <Card className="border border-gray-100 shadow-sm">
      <CardHeader className="pb-4">
        <CardTitle className="text-black font-light flex items-center text-lg">
          <ShieldCheck className="h-5 w-5 mr-2" />
          Successful Trades
        </CardTitle>
      </CardHeader>
      <CardContent className="p-0">
        <div className="space-y-2">
          {/* Header */}
          <div className="grid grid-cols-3 gap-2 px-6 pt-2 text-xs text-gray-500 font-light">
            <div>Side</div>

            {/* <div className="text-right">CreatedAt</div> */}
            <div className="text-right">Quantity</div>
            <div className="text-right">Price</div>
            {/* <div className="text-right">Total</div> */}
          </div>

          {/* pastTradess */}
          <div className="max-h-96 overflow-y-auto">
            {!pastTrades ? (
              <div className="px-6 py-8 text-center text-gray-500 font-light">
                No pastTradess yet
              </div>
            ) : (
              pastTrades?.map((pastTrade) => (
                <div
                  key={pastTrade.createdAt}
                  className="grid grid-cols-4 gap-2 px-6 py-3 text-sm hover:bg-gray-50 border-b border-gray-100 transition-colors"
                >
                  <div className="space-y-1">
                    <div
                      className={`font-normal ${
                        pastTrade.side === "BUY"
                          ? "text-green-600"
                          : "text-red-600"
                      }`}
                    >
                      {pastTrade.side?.toUpperCase()}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      {new Date(pastTrade.createdAt).toLocaleString()}
                    </div>
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    {pastTrade.quantity.toFixed(0)} g
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    ₹{pastTrade.price.toFixed(2)}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default TradeHistory;
