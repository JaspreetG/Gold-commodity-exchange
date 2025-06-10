import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuthStore } from "@/store/useAuthStore";
import { Plus, DollarSign, Coins } from "lucide-react";

interface WalletManagerProps {
  balances: {
    inr: number;
    gold: number;
  };
}

const WalletManager = ({ balances }: WalletManagerProps) => {
  const authStore = useAuthStore();
  const [usdAmount, setUsdAmount] = useState("");
  const [goldAmount, setGoldAmount] = useState("");
  const [withdrawAmount, setWithdrawAmount] = useState("");
  const [isAddingUSD, setIsAddingUSD] = useState(false);
  const [isAddingGold, setIsAddingGold] = useState(false);
  const [isWithdrawingUSD, setIsWithdrawingUSD] = useState(false);

  // Helper to handle both INR and Gold addition
  const processAddition = async (
    type: "inr" | "gold",
    amountStr: string,
    setAmount: (v: string) => void
  ) => {
    const amount = parseFloat(amountStr);
    if (!amount || amount <= 0) return;

    if (type === "inr") {
      setIsAddingUSD(true);
      await authStore.addMoney(amount);
      setIsAddingUSD(false);
    } else {
      setIsAddingGold(true);
      await authStore.addGold(amount);
      setIsAddingGold(false);
    }

    setAmount("");
  };

  const processWithdrawal = async () => {
    const amount = parseFloat(withdrawAmount);
    if (!amount || amount <= 0) return;

    setIsWithdrawingUSD(true);
    await authStore.withdrawMoney(amount);
    setWithdrawAmount("");
    setIsWithdrawingUSD(false);
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <Card className="border border-gray-100 shadow-sm">
        <CardHeader>
          <CardTitle className="text-black font-light flex items-center text-lg">
            <DollarSign className="h-5 w-5 mr-2 text-green-600" />
            Add INR Funds
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label htmlFor="inr-amount" className="text-gray-600 font-light">
              Amount (INR)
            </Label>
            <Input
              id="inr-amount"
              type="number"
              value={usdAmount}
              onChange={(e) => setUsdAmount(e.target.value)}
              className="bg-white border-gray-200 text-black font-light"
              placeholder="0.00"
              min="0"
              step="0.01"
            />
          </div>
          <div className="text-sm text-gray-500 font-light">
            Current Balance:₹{balances.inr.toLocaleString()}
          </div>
          <Button
            onClick={() => processAddition("inr", usdAmount, setUsdAmount)}
            disabled={isAddingUSD || !usdAmount}
            className="w-full bg-green-600 hover:bg-green-700 text-white border-0 shadow-sm font-light"
          >
            <Plus className="h-4 w-4 mr-2" />
            {isAddingUSD ? "Processing..." : "Add Funds"}
          </Button>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader>
          <CardTitle className="text-black font-light flex items-center text-lg">
            <Coins className="h-5 w-5 mr-2 text-amber-600" />
            Add Gold
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label htmlFor="gold-amount" className="text-gray-600 font-light">
              Amount (gram)
            </Label>
            <Input
              id="gold-amount"
              type="number"
              value={goldAmount}
              onChange={(e) => setGoldAmount(e.target.value)}
              className="bg-white border-gray-200 text-black font-light"
              placeholder="0.000"
              min="0"
              step="0.001"
            />
          </div>
          <div className="text-sm text-gray-500 font-light">
            Current Holdings: {balances.gold.toFixed(3)} gram
          </div>
          <Button
            onClick={() => processAddition("gold", goldAmount, setGoldAmount)}
            disabled={isAddingGold || !goldAmount}
            className="w-full bg-amber-600 hover:bg-amber-700 text-white border-0 shadow-sm font-light"
          >
            <Plus className="h-4 w-4 mr-2" />
            {isAddingGold ? "Processing..." : "Add Gold"}
          </Button>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader>
          <CardTitle className="text-black font-light flex items-center text-lg">
            <DollarSign className="h-5 w-5 mr-2 text-red-600" />
            Withdraw INR Funds
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label
              htmlFor="withdraw-amount"
              className="text-gray-600 font-light"
            >
              Amount (INR)
            </Label>
            <Input
              id="withdraw-amount"
              type="number"
              value={withdrawAmount}
              onChange={(e) => setWithdrawAmount(e.target.value)}
              className="bg-white border-gray-200 text-black font-light"
              placeholder="0.00"
              min="0"
              step="0.01"
            />
          </div>
          <div className="text-sm text-gray-500 font-light">
            Current Balance: ₹{balances.inr.toLocaleString()}
          </div>
          <Button
            onClick={processWithdrawal}
            disabled={isWithdrawingUSD || !withdrawAmount}
            className="w-full bg-red-600 hover:bg-red-700 text-white border-0 shadow-sm font-light"
          >
            <Plus className="h-4 w-4 mr-2" />
            {isWithdrawingUSD ? "Processing..." : "Withdraw Funds"}
          </Button>
        </CardContent>
      </Card>
    </div>
  );
};

export default WalletManager;
