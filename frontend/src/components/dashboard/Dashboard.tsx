import { useState, useEffect } from "react";
import { User } from "@/types/auth";
import Navbar from "./Navbar";
import Portfolio from "./Portfolio";
import TradingInterface from "../trading/TradingInterface";
import WalletManager from "./WalletManager";
import { useAuthStore } from "@/store/useAuthStore";

interface DashboardProps {
  user: User;
  onLogout: () => void;
}

const Dashboard = () => {
  const { authUser, logout } = useAuthStore();

  const [activeTab, setActiveTab] = useState<"trading" | "wallet">("trading");
  const [userBalances, setUserBalances] = useState(authUser.balances);

  useEffect(() => {
    setUserBalances(authUser.balances ?? { inr: 0, gold: 0 });
  }, [authUser.balances]);

  const updateBalance = (inr: number, gold: number) => {
    useAuthStore.setState((state) => ({
      authUser: {
        ...state.authUser!,
        balances: {
          inr,
          gold,
        },
      },
    }));
    setUserBalances({ inr, gold });
  };

  return (
    <div className="min-h-screen bg-white">
      <Navbar
        user={{ ...authUser, balances: userBalances }}
        onLogout={logout}
      />

      <div className="container mx-auto px-6 py-8">
        <Portfolio balances={userBalances} />

        <div className="mt-8">
          <div className="flex space-x-2 mb-8">
            <button
              onClick={() => setActiveTab("trading")}
              className={`px-6 py-3 rounded-md font-light transition-colors ${
                activeTab === "trading"
                  ? "bg-black text-white"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              }`}
            >
              Trading
            </button>
            <button
              onClick={() => setActiveTab("wallet")}
              className={`px-6 py-3 rounded-md font-light transition-colors ${
                activeTab === "wallet"
                  ? "bg-black text-white"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              }`}
            >
              Wallet
            </button>
          </div>

          {activeTab === "trading" ? (
            <TradingInterface
              userBalances={userBalances}
              updateBalance={updateBalance}
            />
          ) : (
            <WalletManager
              balances={userBalances}
              updateBalance={updateBalance}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
