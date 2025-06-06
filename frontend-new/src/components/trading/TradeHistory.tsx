import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Trade } from "@/types/trading";
import { Clock } from "lucide-react";

interface TradeHistoryProps {
  trades: Trade[];
}

const TradeHistory = ({ trades }: TradeHistoryProps) => {
  return (
    <Card className="border border-gray-100 shadow-sm">
      <CardHeader className="pb-4">
        <CardTitle className="text-black font-light flex items-center text-lg">
          <Clock className="h-5 w-5 mr-2" />
          Trade History
        </CardTitle>
      </CardHeader>
      <CardContent className="p-0">
        <div className="space-y-2">
          {/* Header */}
          <div className="grid grid-cols-4 gap-2 px-6 pt-2 text-xs text-gray-500 font-light">
            <div>Type</div>
            <div className="text-right">Quantity</div>
            <div className="text-right">Price</div>
            <div className="text-right">Total</div>
          </div>

          {/* Trades */}
          <div className="max-h-96 overflow-y-auto">
            {trades.length === 0 ? (
              <div className="px-6 py-8 text-center text-gray-500 font-light">
                No trades yet
              </div>
            ) : (
              trades.map((trade) => (
                <div
                  key={trade.id}
                  className="grid grid-cols-4 gap-2 px-6 py-3 text-sm hover:bg-gray-50 border-b border-gray-100 transition-colors"
                >
                  <div className="space-y-1">
                    <div
                      className={`font-normal ${
                        trade.type === "BUY" ? "text-green-600" : "text-red-600"
                      }`}
                    >
                      {trade.type.toUpperCase()}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      {trade.timestamp.toLocaleTimeString()}
                    </div>
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    {trade.quantity.toFixed(3)} oz
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    ${trade.price.toFixed(2)}
                  </div>
                  <div className="text-right space-y-1">
                    <div className="text-black font-mono font-light">
                      ${trade.total.toFixed(2)}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      Fee: ${trade.fee.toFixed(2)}
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
