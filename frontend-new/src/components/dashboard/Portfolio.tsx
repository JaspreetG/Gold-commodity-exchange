import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, DollarSign, Coins } from "lucide-react";

interface PortfolioProps {
  balances: {
    usd: number;
    gold: number;
  };
}

const Portfolio = ({ balances }: PortfolioProps) => {
  const goldPrice = 2050; // Mock current gold price
  const totalValue = (balances?.usd ?? 0) + (balances?.gold ?? 0) * goldPrice;
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
            ${(totalValue ?? 0).toLocaleString()}
          </div>
          <p className="text-xs text-green-600 mt-1 font-light">
            +2.4% from yesterday
          </p>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            USD Balance
          </CardTitle>
          <DollarSign className="h-4 w-4 text-black" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">
            ${(balances?.usd ?? 0).toLocaleString()}
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
          <Coins className="h-4 w-4 text-red-600" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">
            {(balances?.gold ?? 0).toFixed(3)} oz
          </div>
          <p className="text-xs text-gray-500 mt-1 font-light">
            ${(goldValue ?? 0).toLocaleString()} value
          </p>
        </CardContent>
      </Card>

      <Card className="border border-gray-100 shadow-sm">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-normal text-gray-600">
            Gold Price
          </CardTitle>
          <TrendingUp className="h-4 w-4 text-red-600" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-light text-black">${goldPrice}</div>
          <p className="text-xs text-red-600 mt-1 font-light">+1.2% today</p>
        </CardContent>
      </Card>
    </div>
  );
};

export default Portfolio;
