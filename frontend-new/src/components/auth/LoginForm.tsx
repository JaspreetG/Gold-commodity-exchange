import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Coins, Phone, Shield } from "lucide-react";
import CountryCodeSelect, { Country } from "./CountryCodeSelect";
import OTPInput from "./OTPInput";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../store/useAuthStore";

type AuthStep = "phone" | "totp";

const LoginForm = () => {
  const { login, verifyTOTP } = useAuthStore();
  const [step, setStep] = useState<AuthStep>("phone");
  const [phone, setPhone] = useState("");
  const [totp, setTotp] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState<Country>({
    code: "US",
    name: "United States",
    flag: "🇺🇸",
    dialCode: "+1",
  });

  const navigate = useNavigate();
  const onBackToLanding = () => {
    console.log("Back to landing page");
    navigate("/");
  };

  const handlePhoneSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    await login({ phone }, navigate);
    setStep("totp");

    setIsLoading(false);
  };

  const handleTOTPSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    if (totp.length !== 6) {
      setIsLoading(false);
      return;
    }

    try {
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs");
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      const deviceFingerprint = result.visitorId;

      await verifyTOTP(phone, totp, deviceFingerprint);
      navigate("/dashboard");
    } catch (error) {
      console.error("Verification error:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-12">
          <button
            onClick={onBackToLanding}
            className="flex items-center justify-center mb-6 mx-auto hover:opacity-80 transition-opacity"
          >
            <Coins className="h-12 w-12 text-black mr-3" />
            <h1 className="text-4xl font-light text-black">GoldEx</h1>
          </button>
          <p className="text-gray-600 font-light">Welcome back</p>
        </div>

        <Card className="border border-gray-200 shadow-sm">
          <CardHeader className="text-center pb-4">
            <CardTitle className="text-black font-light flex items-center justify-center">
              {step === "phone" && <Phone className="h-5 w-5 mr-2" />}
              {step === "totp" && <Shield className="h-5 w-5 mr-2" />}
              {step === "phone" && "Enter Phone Number"}
              {step === "totp" && "Enter TOTP Code"}
            </CardTitle>
          </CardHeader>
          <CardContent>
            {step === "phone" && (
              <form onSubmit={handlePhoneSubmit} className="space-y-6">
                <div>
                  <Label htmlFor="phone" className="text-gray-700 font-light">
                    Phone Number
                  </Label>
                  <div className="flex mt-2 space-x-2">
                    <CountryCodeSelect
                      selectedCountry={selectedCountry}
                      onSelect={setSelectedCountry}
                    />
                    <Input
                      id="phone"
                      type="tel"
                      value={phone}
                      onChange={(e) =>
                        setPhone(e.target.value.replace(/\D/g, ""))
                      }
                      className="flex-1 border-gray-200 focus:border-black"
                      placeholder="555 123 4567"
                      required
                    />
                  </div>
                </div>

                <Button
                  type="submit"
                  className="w-full bg-black hover:bg-gray-800 text-white font-light"
                  disabled={isLoading}
                >
                  {isLoading ? "Verifying..." : "Next"}
                </Button>

                <Button
                  type="button"
                  onClick={onBackToLanding}
                  variant="outline"
                  className="w-full border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
                >
                  Back to Home
                </Button>
              </form>
            )}

            {step === "totp" && (
              <form onSubmit={handleTOTPSubmit} className="space-y-6">
                <div>
                  <Label className="text-gray-700 font-light">
                    Enter TOTP Code
                  </Label>
                  <div className="mt-4">
                    <OTPInput value={totp} onChange={setTotp} />
                  </div>
                  <p className="text-xs text-gray-500 mt-2 font-light text-center">
                    Enter the 6-digit code from your authenticator app
                  </p>
                </div>

                <div className="flex space-x-3">
                  <Button
                    type="button"
                    onClick={() => setStep("phone")}
                    variant="outline"
                    className="flex-1 border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
                  >
                    Back
                  </Button>
                  <Button
                    type="submit"
                    className="flex-1 bg-black hover:bg-gray-800 text-white font-light"
                    disabled={isLoading || totp.length !== 6}
                  >
                    {isLoading ? "Authenticating..." : "Login"}
                  </Button>
                </div>
              </form>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default LoginForm;
