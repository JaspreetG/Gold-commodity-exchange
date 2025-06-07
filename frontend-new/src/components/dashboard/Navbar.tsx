import { User } from "@/types/auth";
import { Button } from "@/components/ui/button";
import { Coins, LogOut } from "lucide-react";

interface NavbarProps {
  user: User;
  onLogout: () => void;
}

const Navbar = ({ user, onLogout }: NavbarProps) => {
  return (
    <nav className="bg-white border-b border-gray-200">
      <div className="container mx-auto px-6">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <Coins className="h-8 w-8 text-amber-600 mr-3" />
            <h1 className="text-xl font-light text-amber-600">GoldEx</h1>
          </div>

          <div className="flex items-center space-x-8">
            <div className="text-sm text-gray-600 font-light">
              <span className="font-normal text-black">{user.userName}</span>
              <span className="ml-2">({user.phoneNumber})</span>
            </div>

            <div className="flex items-center space-x-4 text-sm font-light">
              <div className="bg-green-50 text-green-700 px-4 py-2 rounded-md border border-green-200">
                ${user.balances?.usd?.toLocaleString() ?? "0.00"}
              </div>
              <div className="bg-amber-50 text-amber-700 px-4 py-2 rounded-md border border-amber-200">
                {user.balances?.gold?.toFixed(3) ?? "0.000"} oz
              </div>
            </div>

            <Button
              onClick={onLogout}
              variant="outline"
              size="sm"
              className="border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
            >
              <LogOut className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
