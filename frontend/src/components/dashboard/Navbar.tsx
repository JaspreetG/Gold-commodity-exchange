// import { User } from "@/types/auth";
// import { Button } from "@/components/ui/button";
// import {
//   Banknote,
//   Blocks,
//   Coins,
//   Cuboid,
//   Currency,
//   LogOut,
// } from "lucide-react";
// import { useAuthStore } from "@/store/useAuthStore";

// interface NavbarProps {
//   user: User;
//   onLogout: () => void;
// }

// const Navbar = ({ user }: NavbarProps) => {
//   const onLogout = useAuthStore((state) => state.logout);

//   return (
//     <nav className="bg-white border-b border-gray-200">
//       <div className="container mx-auto px-6">
//         <div className="flex items-center justify-between h-16">
//           <div className="flex items-center">
//             <Banknote className="h-8 w-8 mr-3 text-amber-600" />
//             <h1 className="text-xl font-light text-amber-600">GoldEx</h1>
//           </div>

//           <div className="flex items-center space-x-8">
//             <div className="text-sm text-gray-600 font-light">
//               <span className="font-normal text-black">
//                 {user?.userName ?? ""}
//               </span>
//               <span className="ml-2">({user?.phoneNumber ?? ""})</span>
//             </div>

//             <div className="flex items-center space-x-4 text-sm font-light">
//               <div className="bg-green-50 text-green-700 px-4 py-2 rounded-md border border-green-200">
//                 ₹{user.balances?.inr?.toLocaleString() ?? "0.00"}
//               </div>
//               <div className="bg-amber-50 text-amber-700 px-4 py-2 rounded-md border border-amber-200">
//                 {user.balances?.gold?.toFixed(3) ?? "0.000"} gram
//               </div>
//             </div>

//             <Button
//               onClick={onLogout}
//               variant="outline"
//               size="sm"
//               className="border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
//             >
//               <LogOut className="h-4 w-4 mr-2" />
//               Logout
//             </Button>
//           </div>
//         </div>
//       </div>
//     </nav>
//   );
// };

// export default Navbar;

import { useState } from "react";
import { User } from "@/types/auth";
import { Button } from "@/components/ui/button";
import { Banknote, LogOut, Menu } from "lucide-react";
import { useAuthStore } from "@/store/useAuthStore";

interface NavbarProps {
  user: User;
  onLogout: () => void;
}

const Navbar = ({ user }: NavbarProps) => {
  const onLogout = useAuthStore((state) => state.logout);
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <nav className="bg-white border-b border-gray-200">
      <div className="container mx-auto px-4 sm:px-6">
        <div className="flex items-center justify-between h-16">
          {/* Brand */}
          <div className="flex items-center">
            <Banknote className="h-8 w-8 mr-2 text-amber-600" />
            <h1 className="text-xl font-light text-amber-600">GoldEx</h1>
          </div>

          {/* Mobile menu button */}
          <div className="sm:hidden">
            <button onClick={() => setMenuOpen(!menuOpen)}>
              <Menu className="h-6 w-6 text-gray-700" />
            </button>
          </div>

          {/* Desktop menu */}
          <div className="hidden sm:flex items-center space-x-8">
            <div className="text-sm text-gray-600 font-light">
              <span className="font-normal text-black">{user?.userName}</span>
              <span className="ml-2">({user?.phoneNumber})</span>
            </div>

            <div className="flex items-center space-x-4 text-sm font-light">
              <div className="bg-green-50 text-green-700 px-4 py-2 rounded-md border border-green-200">
                ₹{user.balances?.inr?.toLocaleString() ?? "0.00"}
              </div>
              <div className="bg-amber-50 text-amber-700 px-4 py-2 rounded-md border border-amber-200">
                {user.balances?.gold?.toFixed(0) ?? "0.000"} gram
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

        {/* Mobile dropdown */}
        {menuOpen && (
          <div className="sm:hidden mb-2 mt-2 space-y-3 text-sm text-gray-700">
            <div className="text-sm text-gray-600 font-light">
              <span className="font-normal text-black">{user?.userName}</span>
              <span className="ml-2">({user?.phoneNumber})</span>
            </div>
            {/* <div>
              {user?.userName} ({user?.phoneNumber})
            </div> */}
            <div className="bg-green-50 text-green-700 px-4 py-2 rounded-md border border-green-200">
              ₹{user.balances?.inr?.toLocaleString() ?? "0"}
            </div>
            <div className="bg-amber-50 text-amber-700 px-4 py-2 rounded-md border border-amber-200">
              {user.balances?.gold?.toFixed(0) ?? "0"} gram
            </div>

            <Button
              onClick={onLogout}
              variant="outline"
              size="sm"
              className="w-full border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
            >
              <LogOut className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
