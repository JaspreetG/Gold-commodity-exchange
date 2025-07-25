import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Trade } from "@/types/trading";
import { TrendingUp } from "lucide-react";
import { useAuthStore } from "@/store/useAuthStore";
import { useLtp } from "../../hooks/useLtp";

interface TradingFormProps {
  userBalances: {
    inr: number;
    gold: number;
  };
  updateBalance: (type: "inr" | "gold", amount: number) => void;
  currentPrice: number;
  onTrade: (trade: Omit<Trade, "id" | "timestamp">) => void;
}

const TradingForm = ({
  userBalances,
  updateBalance,
  onTrade,
}: TradingFormProps) => {
  const { createOrder, isCreatingOrder, addToast } = useAuthStore();

  const ltp = useLtp();
  const currentPrice = ltp?.price || 1;

  // const currentPrice = 4000;

  const [buyQuantity, setBuyQuantity] = useState("");
  const [sellQuantity, setSellQuantity] = useState("");
  const [buyOrderType, setBuyOrderType] = useState("market");
  const [sellOrderType, setSellOrderType] = useState("market");
  const [buyPrice, setBuyPrice] = useState("");
  const [sellPrice, setSellPrice] = useState("");

  const handleBuy = async () => {
    const quantity = parseInt(buyQuantity);
    if (!quantity || quantity <= 0) {
      addToast({
        title: "Order Failed",
        description: "Quantity must be greater than 0",
      });
      setBuyQuantity("");
      setBuyPrice("");
      return;
    }

    const price =
      buyOrderType === "limit" ? parseFloat(buyPrice) : currentPrice;
    if (buyOrderType === "limit" && (!price || price <= 0)) {
      addToast({
        title: "Order Failed",
        description: "Price must be greater than 0 for limit orders",
      });
      setBuyQuantity("");
      setBuyPrice("");
      return;
    }

    const total = quantity * price;

    if (total > userBalances.inr) {
      addToast({
        title: "Order Failed",
        description: "Insufficient funds",
      });
      setBuyQuantity("");
      setBuyPrice("");
      return;
    }

    const side = "BUY";
    const type = buyOrderType === "limit" ? "LIMIT" : "MARKET";

    createOrder(quantity, price, side, type);

    // updateBalance();
    // onTrade({
    //   type: "BUY",
    //   price,
    //   quantity,
    //   total,
    //   fee: 0,
    // });

    setBuyQuantity("");
    setBuyPrice("");
  };

  const handleSell = async () => {
    console.log("in handle sell");

    const quantity = parseInt(sellQuantity);
    if (!quantity || quantity <= 0) {
      addToast({
        title: "Order Failed",
        description: "Quantity must be greater than 0",
      });
      setSellQuantity("");
      setSellPrice("");
      return;
    }

    if (quantity > userBalances.gold) {
      addToast({
        title: "Order Failed",
        description: "Insufficient gold",
      });
      setSellQuantity("");
      setSellPrice("");
      return;
    }

    const price =
      sellOrderType === "limit" ? parseFloat(sellPrice) : currentPrice;
    if (sellOrderType === "limit" && (!price || price <= 0)) {
      addToast({
        title: "Order Failed",
        description: "Price must be greater than 0 for limit orders",
      });
      setSellQuantity("");
      setSellPrice("");
      return;
    }

    // const total = quantity * price;

    const side = "SELL";
    const type = sellOrderType === "limit" ? "LIMIT" : "MARKET";

    createOrder(quantity, price, side, type);

    // updateBalance("gold", -quantity);
    // updateBalance("inr", total);

    // onTrade({
    //   type: "SELL",
    //   price,
    //   quantity,
    //   total,
    //   fee: 0,
    // });

    setSellQuantity("");
    setSellPrice("");
  };

  return (
    <Card className="border border-gray-100 shadow-sm">
      <CardHeader className="pb-4">
        <CardTitle className="text-black font-light flex items-center text-lg">
          <TrendingUp className="h-5 w-5 mr-2" />
          Place Order
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Tabs defaultValue="BUY" className="w-full">
          <TabsList className="grid w-full grid-cols-2 bg-gray-100">
            <TabsTrigger
              value="BUY"
              className="text-gray-700 data-[state=active]:bg-green-600 data-[state=active]:text-white"
            >
              Buy Gold
            </TabsTrigger>
            <TabsTrigger
              value="SELL"
              className="text-gray-700 data-[state=active]:bg-red-600 data-[state=active]:text-white"
            >
              Sell Gold
            </TabsTrigger>
          </TabsList>

          <TabsContent value="BUY" className="space-y-5 mt-6">
            <div className="space-y-3">
              <Label className="text-gray-700 font-light text-sm">
                Order Type
              </Label>
              <RadioGroup
                value={buyOrderType}
                onValueChange={setBuyOrderType}
                className="flex space-x-6"
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="market"
                    id="buy-market"
                    className="border-gray-300"
                  />
                  <Label
                    htmlFor="buy-market"
                    className="text-sm font-light cursor-pointer"
                  >
                    Market Order
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="limit"
                    id="buy-limit"
                    className="border-gray-300"
                  />
                  <Label
                    htmlFor="buy-limit"
                    className="text-sm font-light cursor-pointer"
                  >
                    Limit Order
                  </Label>
                </div>
              </RadioGroup>
            </div>

            {buyOrderType === "limit" && (
              <div className="space-y-2">
                <Label
                  htmlFor="buy-price"
                  className="text-gray-700 font-light text-sm"
                >
                  Price (INR)
                </Label>
                <Input
                  id="buy-price"
                  type="number"
                  value={buyPrice}
                  onChange={(e) => setBuyPrice(e.target.value)}
                  className="bg-white border-gray-300 text-black focus:border-green-500 focus:ring-green-500"
                  placeholder={currentPrice.toFixed(2)}
                  min="0"
                  step="0.01"
                />
              </div>
            )}

            <div className="space-y-2">
              <Label
                htmlFor="buy-quantity"
                className="text-gray-700 font-light text-sm"
              >
                Quantity (gram)
              </Label>
              <Input
                id="buy-quantity"
                type="number"
                value={buyQuantity}
                // onChange={(e) => setBuyQuantity(e.target.value)}
                onChange={(e) => {
                  const value = e.target.value;
                  if (/^\d*$/.test(value)) {
                    setBuyQuantity(value);
                  }
                }}
                className="bg-white border-gray-300 text-black focus:border-green-500 focus:ring-green-500"
                placeholder="0"
                min="0"
                step="1"
              />
            </div>

            <div className="bg-gray-50 p-4 rounded-lg space-y-3 text-sm">
              <div className="flex justify-between text-gray-600">
                <span>Price:</span>
                <span className="text-black font-mono">
                  ₹
                  {buyOrderType === "limit"
                    ? buyPrice || currentPrice.toFixed(2)
                    : currentPrice.toFixed(2)}
                </span>
              </div>
              <div className="flex justify-between text-gray-600 border-t border-gray-200 pt-3">
                <span>Total:</span>
                <span className="text-black font-mono font-medium">
                  ₹
                  {buyQuantity
                    ? (
                        parseFloat(buyQuantity) *
                        (buyOrderType === "limit"
                          ? parseFloat(buyPrice) || currentPrice
                          : currentPrice)
                      ).toFixed(2)
                    : "0.00"}
                </span>
              </div>
              <div className="text-xs text-gray-500">
                Available:₹{(userBalances?.inr ?? 0).toLocaleString()}
              </div>
            </div>

            <Button
              onClick={handleBuy}
              disabled={
                isCreatingOrder ||
                !buyQuantity ||
                (buyOrderType === "limit" && !buyPrice)
              }
              className="w-full bg-green-600 hover:bg-green-700 text-white font-light h-11"
            >
              {isCreatingOrder ? "Executing..." : "Buy Gold"}
            </Button>
          </TabsContent>

          <TabsContent value="SELL" className="space-y-5 mt-6">
            <div className="space-y-3">
              <Label className="text-gray-700 font-light text-sm">
                Order Type
              </Label>
              <RadioGroup
                value={sellOrderType}
                onValueChange={setSellOrderType}
                className="flex space-x-6"
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="market"
                    id="sell-market"
                    className="border-gray-300"
                  />
                  <Label
                    htmlFor="sell-market"
                    className="text-sm font-light cursor-pointer"
                  >
                    Market Order
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="limit"
                    id="sell-limit"
                    className="border-gray-300"
                  />
                  <Label
                    htmlFor="sell-limit"
                    className="text-sm font-light cursor-pointer"
                  >
                    Limit Order
                  </Label>
                </div>
              </RadioGroup>
            </div>

            {sellOrderType === "limit" && (
              <div className="space-y-2">
                <Label
                  htmlFor="sell-price"
                  className="text-gray-700 font-light text-sm"
                >
                  Price (INR)
                </Label>
                <Input
                  id="sell-price"
                  type="number"
                  value={sellPrice}
                  onChange={(e) => setSellPrice(e.target.value)}
                  className="bg-white border-gray-300 text-black focus:border-red-500 focus:ring-red-500"
                  placeholder={currentPrice.toFixed(2)}
                  min="0"
                  step="0.01"
                />
              </div>
            )}

            <div className="space-y-2">
              <Label
                htmlFor="sell-quantity"
                className="text-gray-700 font-light text-sm"
              >
                Quantity (gram)
              </Label>
              <Input
                id="sell-quantity"
                type="number"
                value={sellQuantity}
                // onChange={(e) => setSellQuantity(e.target.value)}
                onChange={(e) => {
                  const value = e.target.value;
                  if (/^\d*$/.test(value)) {
                    setSellQuantity(value);
                  }
                }}
                className="bg-white border-gray-300 text-black focus:border-red-500 focus:ring-red-500"
                placeholder="0.000"
                min="0"
                step="1"
              />
            </div>

            <div className="bg-gray-50 p-4 rounded-lg space-y-3 text-sm">
              <div className="flex justify-between text-gray-600">
                <span>Price:</span>
                <span className="text-black font-mono">
                  ₹
                  {sellOrderType === "limit"
                    ? sellPrice || currentPrice.toFixed(2)
                    : currentPrice.toFixed(2)}
                </span>
              </div>
              <div className="flex justify-between text-gray-600 border-t border-gray-200 pt-3">
                <span>Total:</span>
                <span className="text-black font-mono font-medium">
                  ₹
                  {sellQuantity
                    ? (
                        parseFloat(sellQuantity) *
                        (sellOrderType === "limit"
                          ? parseFloat(sellPrice) || currentPrice
                          : currentPrice)
                      ).toFixed(2)
                    : "0.00"}
                </span>
              </div>
              <div className="text-xs text-gray-500">
                Available: {(userBalances?.gold ?? 0).toFixed(3)} gram
              </div>
            </div>

            <Button
              onClick={handleSell}
              disabled={
                isCreatingOrder ||
                !sellQuantity ||
                (sellOrderType === "limit" && !sellPrice)
              }
              className="w-full bg-red-600 hover:bg-red-700 text-white font-light h-11"
            >
              {isCreatingOrder ? "Executing..." : "Sell Gold"}
            </Button>
          </TabsContent>
        </Tabs>
      </CardContent>
    </Card>
  );
};

export default TradingForm;
