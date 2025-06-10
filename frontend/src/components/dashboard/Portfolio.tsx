import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, Coins, IndianRupee, Banknote } from "lucide-react";

interface PortfolioProps {
  balances: {
    inr: number;
    gold: number;
  };
}

const Portfolio = ({ balances }: PortfolioProps) => {
  // TODO: Replace with actual API call to fetch current gold price
  const goldPrice = 4000; // Mock current gold price
  const totalValue = (balances?.inr ?? 0) + (balances?.gold ?? 0) * goldPrice;
  const goldValue = (balances?.gold ?? 0) * goldPrice;

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            Total Portfolio
          </CardTitle>
          <TrendingUp className="h-4 w-4 text-green-600" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">
            ₹{(totalValue ?? 0).toLocaleString()}
          </div>
          <p className="text-xs text-slate-600 mt-1 font-light">
            Total value of your account holding
          </p>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            INR Balance
          </CardTitle>
          <IndianRupee className="h-5 w-5 mr-2 text-lime-500" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">
            ₹{(balances?.inr ?? 0).toLocaleString()}
          </div>
          <p className="text-xs text-gray-500 mt-1 font-light">
            Available for trading
          </p>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            Gold Holdings
          </CardTitle>
          <Banknote className="h-6 w-6 mr-3 text-amber-600" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">
            {(balances?.gold ?? 0).toFixed(3)} gram
          </div>
          <p className="text-xs text-gray-500 mt-1 font-light">
            ₹{(goldValue ?? 0).toLocaleString()} value
          </p>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            Gold Price
          </CardTitle>

          <TrendingUp className="h-4 w-4 text-yellow-500" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">₹{goldPrice}</div>
          <p className="text-xs text-slate-600 mt-1 font-light">
            Based on last traded price
          </p>
        </CardContent>
      </Card>
    </div>
  );
};

export default Portfolio;
