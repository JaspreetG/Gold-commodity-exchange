import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Trade } from "@/types/trading";
import { Clock } from "lucide-react";

interface TradePositionProps {
  positions: Trade[];
}

const TradePosition = ({ positions }: TradePositionProps) => {
  return (
    <Card className="border border-gray-100 shadow-sm">
      <CardHeader className="pb-4">
        <CardTitle className="text-black font-light flex items-center text-lg">
          <Clock className="h-5 w-5 mr-2" />
          Current Position
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

          {/* Positions */}
          <div className="max-h-96 overflow-y-auto">
            {positions.length === 0 ? (
              <div className="px-6 py-8 text-center text-gray-500 font-light">
                No open positions
              </div>
            ) : (
              positions.map((position) => (
                <div
                  key={position.id}
                  className="grid grid-cols-4 gap-2 px-6 py-3 text-sm hover:bg-gray-50 border-b border-gray-100 transition-colors"
                >
                  <div className="space-y-1">
                    <div
                      className={`font-normal ${
                        position.type === "BUY"
                          ? "text-green-600"
                          : "text-red-600"
                      }`}
                    >
                      {position.type.toUpperCase()}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      {position.timestamp.toLocaleTimeString()}
                    </div>
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    {position.quantity.toFixed(3)} oz
                  </div>
                  <div className="text-right text-black font-mono font-light">
                    ${position.price.toFixed(2)}
                  </div>
                  <div className="text-right space-y-1">
                    <div className="text-black font-mono font-light">
                      ${position.total.toFixed(2)}
                    </div>
                    <div className="text-gray-500 text-xs font-light">
                      Fee: ${position.fee.toFixed(2)}
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

export default TradePosition;
