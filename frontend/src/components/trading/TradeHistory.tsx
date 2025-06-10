import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useAuthStore } from "@/store/useAuthStore";
import { Clock } from "lucide-react";
import React, { useEffect } from "react";

// interface pastTradesHistoryProps {
//   pastTradess: pastTrades[];
// }

const TradeHistory = () => {
  const { getTradeHistory, isGettingTradeHistory, pastTrades } = useAuthStore();

  useEffect(() => {
    const intervalId = setInterval(() => {
      getTradeHistory();
    }, 5000); // 5000ms = 5 seconds

    // Optionally call immediately on mount
    getTradeHistory();

    return () => clearInterval(intervalId); // Cleanup on unmount
  }, [getTradeHistory]);

  return (
    <Card className="border border-gray-100 shadow-sm">
      <CardHeader className="pb-4">
        <CardTitle className="text-black font-light flex items-center text-lg">
          <Clock className="h-5 w-5 mr-2" />
          pastTrades History
        </CardTitle>
      </CardHeader>
      <CardContent className="p-0">
        <div className="space-y-2">
          {/* Header */}
          <div className="grid grid-cols-4 gap-2 px-6 pt-2 text-xs text-gray-500 font-light">
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
                      {new Date(pastTrade.createdAt).toLocaleDateString()}
                    </div>
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    {pastTrade.quantity.toFixed(3)} gram
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    ₹{pastTrade.price.toFixed(2)}
                  </div>
                  <div className="text-right space-y-1">
                    <div className="text-black font-mono font-light">
                      {/* ${pastTrades.total.toFixed(2)} */}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      {/* Fee: ${pastpastTradess.fee.toFixed(2)} */}
                    </div>
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
