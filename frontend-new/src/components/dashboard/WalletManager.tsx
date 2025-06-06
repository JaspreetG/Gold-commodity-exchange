
import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { toast } from "@/hooks/use-toast";
import { Plus, DollarSign, Coins } from "lucide-react";

interface WalletManagerProps {
  balances: {
    usd: number;
    gold: number;
  };
  updateBalance: (type: 'usd' | 'gold', amount: number) => void;
}

const WalletManager = ({ balances, updateBalance }: WalletManagerProps) => {
  const [usdAmount, setUsdAmount] = useState("");
  const [goldAmount, setGoldAmount] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleAddUSD = async () => {
    const amount = parseFloat(usdAmount);
    if (!amount || amount <= 0) {
      toast({
        title: "Invalid Amount",
        description: "Please enter a valid USD amount",
        variant: "destructive"
      });
      return;
    }

    setIsLoading(true);
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    updateBalance('usd', amount);
    setUsdAmount("");
    setIsLoading(false);
    
    toast({
      title: "Funds Added",
      description: `$${amount.toLocaleString()} has been added to your account`
    });
  };

  const handleAddGold = async () => {
    const amount = parseFloat(goldAmount);
    if (!amount || amount <= 0) {
      toast({
        title: "Invalid Amount",
        description: "Please enter a valid gold amount",
        variant: "destructive"
      });
      return;
    }

    setIsLoading(true);
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    updateBalance('gold', amount);
    setGoldAmount("");
    setIsLoading(false);
    
    toast({
      title: "Gold Added",
      description: `${amount} oz of gold has been added to your account`
    });
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card className="border border-gray-100 shadow-sm">
        <CardHeader>
          <CardTitle className="text-black font-light flex items-center text-lg">
            <DollarSign className="h-5 w-5 mr-2 text-green-600" />
            Add USD Funds
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label htmlFor="usd-amount" className="text-gray-600 font-light">Amount (USD)</Label>
            <Input
              id="usd-amount"
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
            Current Balance: ${balances.usd.toLocaleString()}
          </div>
          <Button
            onClick={handleAddUSD}
            disabled={isLoading || !usdAmount}
            className="w-full bg-green-600 hover:bg-green-700 text-white border-0 shadow-sm font-light"
          >
            <Plus className="h-4 w-4 mr-2" />
            {isLoading ? "Processing..." : "Add Funds"}
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
            <Label htmlFor="gold-amount" className="text-gray-600 font-light">Amount (oz)</Label>
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
            Current Holdings: {balances.gold.toFixed(3)} oz
          </div>
          <Button
            onClick={handleAddGold}
            disabled={isLoading || !goldAmount}
            className="w-full bg-amber-600 hover:bg-amber-700 text-white border-0 shadow-sm font-light"
          >
            <Plus className="h-4 w-4 mr-2" />
            {isLoading ? "Processing..." : "Add Gold"}
          </Button>
        </CardContent>
      </Card>
    </div>
  );
};

export default WalletManager;
