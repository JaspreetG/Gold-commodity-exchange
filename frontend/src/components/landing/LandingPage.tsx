import { Button } from "@/components/ui/button";
import { Banknote, Coins, Shield, TrendingUp, Users } from "lucide-react";
import { useNavigate } from "react-router-dom";

interface LandingPageProps {
  onLoginClick: () => void;
  onSignupClick: () => void;
}

const LandingPage = () => {
  const navigate = useNavigate();

  const onLoginClick = () => {
    // console.log("Login clicked");
    navigate("/login");
  };
  const onSignupClick = () => {
    // console.log("Signup clicked");
    navigate("/signUp");
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Navigation */}
      <nav className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-6">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <Banknote className="h-10 w-10 mr-3 text-amber-600" />
              <h1 className="text-xl font-light text-black">GoldEx</h1>
            </div>

            <div className="flex items-center space-x-4">
              <Button
                onClick={onLoginClick}
                variant="outline"
                className="border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
              >
                Login
              </Button>
              <Button
                onClick={onSignupClick}
                className="bg-black hover:bg-gray-800 text-white font-light"
              >
                Sign Up
              </Button>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="py-24 bg-white">
        <div className="container mx-auto px-6 text-center">
          <h1 className="text-5xl font-light text-black mb-6">
            Professional Gold Trading Platform
          </h1>
          <p className="text-xl text-gray-600 font-light mb-12 max-w-3xl mx-auto">
            Trade gold with institutional-grade tools, real-time market data,
            and secure TOTP authentication. Experience the future of precious
            metals trading.
          </p>
          <Button
            onClick={onLoginClick}
            size="lg"
            className="bg-black hover:bg-gray-800 text-white font-light text-lg px-8 py-4"
          >
            Start Trading Now
          </Button>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-gray-50">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-light text-black text-center mb-16">
            Why Choose GoldEx?
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
            <div className="text-center">
              <div className="bg-white p-6 rounded-lg shadow-minimal mb-6 w-20 h-20 mx-auto flex items-center justify-center">
                <Shield className="h-10 w-10 text-black" />
              </div>
              <h3 className="text-xl font-normal text-black mb-4">
                Secure Authentication
              </h3>
              <p className="text-gray-600 font-light">
                TOTP-based two-factor authentication ensures your account stays
                protected with military-grade security.
              </p>
            </div>

            <div className="text-center">
              <div className="bg-white p-6 rounded-lg shadow-minimal mb-6 w-20 h-20 mx-auto flex items-center justify-center">
                <TrendingUp className="h-10 w-10 text-black" />
              </div>
              <h3 className="text-xl font-normal text-black mb-4">
                Real-Time Trading
              </h3>
              <p className="text-gray-600 font-light">
                Access live gold prices, advanced order books, and execute
                trades instantly with professional tools.
              </p>
            </div>

            <div className="text-center">
              <div className="bg-white p-6 rounded-lg shadow-minimal mb-6 w-20 h-20 mx-auto flex items-center justify-center">
                <Users className="h-10 w-10 text-black" />
              </div>
              <h3 className="text-xl font-normal text-black mb-4">
                Trusted Platform
              </h3>
              <p className="text-gray-600 font-light">
                Join thousands of traders who trust GoldEx for their precious
                metals investment needs.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-white">
        <div className="container mx-auto px-6 text-center">
          <h2 className="text-3xl font-light text-black mb-6">
            Ready to Start Trading Gold?
          </h2>
          <p className="text-lg text-gray-600 font-light mb-8">
            Join GoldEx today and access institutional-grade gold trading tools.
          </p>
          <div className="flex justify-center space-x-4">
            <Button
              onClick={onSignupClick}
              size="lg"
              className="bg-black hover:bg-gray-800 text-white font-light"
            >
              Create Account
            </Button>
            <Button
              onClick={onLoginClick}
              variant="outline"
              size="lg"
              className="border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
            >
              Login
            </Button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-50 py-12 border-t border-gray-200">
        <div className="container mx-auto px-6">
          <div className="text-center">
            <div className="flex items-center justify-center mb-6">
              <Banknote className="h-8 w-8 mr-3 text-amber-600" />
              <h3 className="text-xl font-light text-black">GoldEx</h3>
            </div>
            <p className="text-gray-600 font-light mb-6">
              Professional Gold Trading Platform
            </p>
            <p className="text-sm text-gray-500 font-light">
              © {new Date().getFullYear()} GoldEx. All rights reserved.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;
