// import { useState } from "react";
// import { Button } from "@/components/ui/button";
// import { Input } from "@/components/ui/input";
// import { Label } from "@/components/ui/label";
// import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
// import { toast } from "@/hooks/use-toast";
// import { User, RegistrationData } from "@/types/auth";
// import { Coins, Phone, Shield, QrCode, Copy, Check } from "lucide-react";
// import CountryCodeSelect, { Country } from "./CountryCodeSelect";
// import OTPInput from "./OTPInput";

// interface LoginFormProps {
//   onLogin: (user: User) => void;
//   onBackToLanding: () => void;
//   isSignup?: boolean;
// }

// type AuthStep = "phone" | "totp" | "register-qr";

// const onLogin= (user:User) => {
//   console.log("Login clicked");
//   // redirect("/login");
// }

// const onBackToLanding = () => {
//   console.log("Back to landing clicked");
//   // redirect("/");
// }
// const isSignup = false; // This can be set based on your routing logic

// const LoginForm = () => {
//   const [step, setStep] = useState<AuthStep>("phone");
//   const [phone, setPhone] = useState("");
//   const [totpCode, setTotpCode] = useState("");
//   const [isLoading, setIsLoading] = useState(false);
//   const [registrationData, setRegistrationData] =
//     useState<RegistrationData | null>(null);
//   const [selectedCountry, setSelectedCountry] = useState<Country>({
//     code: "US",
//     name: "United States",
//     flag: "🇺🇸",
//     dialCode: "+1",
//   });
//   const [secretCopied, setSecretCopied] = useState(false);

//   const handlePhoneSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setIsLoading(true);

//     //TODO: IMPLEMENT REAL PHONE VERIFICATION
//     await new Promise((resolve) => setTimeout(resolve, 1000));

//     if (isSignup) {
//       // For signup, always go to QR registration
//       const mockSecret = "JBSWY3DPEHPK3PXP";
//       const qrCodeUrl = `otpauth://totp/GoldEx:${selectedCountry.dialCode}${phone}?secret=${mockSecret}&issuer=GoldEx`;

//       setRegistrationData({
//         phone: `${selectedCountry.dialCode}${phone}`,
//         qrCode: qrCodeUrl,
//         secret: mockSecret,
//       });

//       toast({
//         title: "Setup Required",
//         description: "Please scan the QR code with your authenticator app",
//       });
//       setStep("register-qr");
//     } else {
//       // For login, simulate user existence check
//       const userExists = Math.random() > 0.3;

//       if (userExists) {
//         toast({
//           title: "Phone verified",
//           description: "Please enter your TOTP code",
//         });
//         setStep("totp");
//       } else {
//         toast({
//           title: "Account not found",
//           description: "Please sign up first",
//           variant: "destructive",
//         });
//       }
//     }

//     setIsLoading(false);
//   };

//   const handleTOTPSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setIsLoading(true);

//     if (totpCode.length !== 6) {
//       toast({
//         title: "Invalid TOTP Code",
//         description: "Please enter a 6-digit TOTP code",
//         variant: "destructive",
//       });
//       setIsLoading(false);
//       return;
//     }

//     await new Promise((resolve) => setTimeout(resolve, 1500));

//     const mockUser: User = {
//       id: "1",
//       phone: `${selectedCountry.dialCode}${phone}`,
//       name: "Gold Trader",
//       balances: {
//         usd: 10000,
//         gold: 5.25,
//       },
//     };

//     if (step === "register-qr") {
//       toast({
//         title: "Registration Successful",
//         description: "Welcome to GoldEx! Your account has been created.",
//       });
//     } else {
//       toast({
//         title: "Login Successful",
//         description: "Welcome back to GoldEx",
//       });
//     }

//     onLogin(mockUser);
//     setIsLoading(false);
//   };

//   const resetForm = () => {
//     setStep("phone");
//     setPhone("");
//     setTotpCode("");
//     setRegistrationData(null);
//     setSecretCopied(false);
//   };

//   const copySecret = async () => {
//     if (registrationData?.secret) {
//       await navigator.clipboard.writeText(registrationData.secret);
//       setSecretCopied(true);
//       toast({
//         title: "Secret copied",
//         description: "Secret key copied to clipboard",
//       });
//       setTimeout(() => setSecretCopied(false), 2000);
//     }
//   };

//   return (
//     <div className="min-h-screen flex items-center justify-center bg-white p-4">
//       <div className="w-full max-w-md">
//         <div className="text-center mb-12">
//           <button
//             onClick={onBackToLanding}
//             className="flex items-center justify-center mb-6 mx-auto hover:opacity-80 transition-opacity"
//           >
//             <Coins className="h-12 w-12 text-black mr-3" />
//             <h1 className="text-4xl font-light text-black">GoldEx</h1>
//           </button>
//           <p className="text-gray-600 font-light">
//             {isSignup ? "Create your account" : "Welcome back"}
//           </p>
//         </div>

//         <Card className="border border-gray-200 shadow-sm">
//           <CardHeader className="text-center pb-4">
//             <CardTitle className="text-black font-light flex items-center justify-center">
//               {step === "phone" && <Phone className="h-5 w-5 mr-2" />}
//               {step === "totp" && <Shield className="h-5 w-5 mr-2" />}
//               {step === "register-qr" && <QrCode className="h-5 w-5 mr-2" />}
//               {step === "phone" && "Enter Phone Number"}
//               {step === "totp" && "Enter TOTP Code"}
//               {step === "register-qr" && "Setup Authenticator"}
//             </CardTitle>
//           </CardHeader>
//           <CardContent>
//             {step === "phone" && (
//               <form onSubmit={handlePhoneSubmit} className="space-y-6">
//                 <div>
//                   <Label htmlFor="phone" className="text-gray-700 font-light">
//                     Phone Number
//                   </Label>
//                   <div className="flex mt-2 space-x-2">
//                     <CountryCodeSelect
//                       selectedCountry={selectedCountry}
//                       onSelect={setSelectedCountry}
//                     />
//                     <Input
//                       id="phone"
//                       type="tel"
//                       value={phone}
//                       onChange={(e) =>
//                         setPhone(e.target.value.replace(/\D/g, ""))
//                       }
//                       className="flex-1 border-gray-200 focus:border-black"
//                       placeholder="555 123 4567"
//                       required
//                     />
//                   </div>
//                 </div>

//                 <Button
//                   type="submit"
//                   className="w-full bg-black hover:bg-gray-800 text-white font-light"
//                   disabled={isLoading}
//                 >
//                   {isLoading ? "Verifying..." : "Next"}
//                 </Button>

//                 <Button
//                   type="button"
//                   onClick={onBackToLanding}
//                   variant="outline"
//                   className="w-full border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
//                 >
//                   Back to Home
//                 </Button>
//               </form>
//             )}

//             {step === "register-qr" && (
//               <div className="space-y-6">
//                 <div className="text-center">
//                   <div className="bg-white p-6 border border-gray-200 rounded-lg mb-4">
//                     <div className="w-48 h-48 mx-auto bg-gray-100 flex items-center justify-center text-gray-500 text-sm">
//                       QR Code would appear here
//                       <br />
//                       Scan with authenticator app
//                     </div>
//                   </div>

//                   <div className="mb-4">
//                     <Label className="text-gray-700 font-light text-sm">
//                       Secret Key
//                     </Label>
//                     <div className="flex items-center mt-2 space-x-2">
//                       <Input
//                         value={registrationData?.secret || ""}
//                         readOnly
//                         className="font-mono text-sm border-gray-200 bg-gray-50"
//                       />
//                       <Button
//                         type="button"
//                         onClick={copySecret}
//                         variant="outline"
//                         size="sm"
//                         className="border-gray-200 hover:bg-gray-50"
//                       >
//                         {secretCopied ? (
//                           <Check className="h-4 w-4" />
//                         ) : (
//                           <Copy className="h-4 w-4" />
//                         )}
//                       </Button>
//                     </div>
//                   </div>

//                   <p className="text-xs text-gray-600 font-light mb-6">
//                     Scan the QR code or enter the secret key manually in your
//                     authenticator app
//                   </p>
//                 </div>

//                 <form onSubmit={handleTOTPSubmit} className="space-y-6">
//                   <div>
//                     <Label className="text-gray-700 font-light">
//                       Enter TOTP Code
//                     </Label>
//                     <div className="mt-4">
//                       <OTPInput value={totpCode} onChange={setTotpCode} />
//                     </div>
//                   </div>

//                   <div className="flex space-x-3">
//                     <Button
//                       type="button"
//                       onClick={resetForm}
//                       variant="outline"
//                       className="flex-1 border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
//                     >
//                       Back
//                     </Button>
//                     <Button
//                       type="submit"
//                       className="flex-1 bg-green-600 hover:bg-green-700 text-white font-light"
//                       disabled={isLoading || totpCode.length !== 6}
//                     >
//                       {isLoading ? "Registering..." : "Complete Setup"}
//                     </Button>
//                   </div>
//                 </form>
//               </div>
//             )}

//             {step === "totp" && (
//               <form onSubmit={handleTOTPSubmit} className="space-y-6">
//                 <div>
//                   <Label className="text-gray-700 font-light">
//                     Enter TOTP Code
//                   </Label>
//                   <div className="mt-4">
//                     <OTPInput value={totpCode} onChange={setTotpCode} />
//                   </div>
//                   <p className="text-xs text-gray-500 mt-2 font-light text-center">
//                     Enter the 6-digit code from your authenticator app
//                   </p>
//                 </div>

//                 <div className="flex space-x-3">
//                   <Button
//                     type="button"
//                     onClick={resetForm}
//                     variant="outline"
//                     className="flex-1 border-gray-200 text-gray-600 hover:bg-gray-50 font-light"
//                   >
//                     Back
//                   </Button>
//                   <Button
//                     type="submit"
//                     className="flex-1 bg-black hover:bg-gray-800 text-white font-light"
//                     disabled={isLoading || totpCode.length !== 6}
//                   >
//                     {isLoading ? "Authenticating..." : "Login"}
//                   </Button>
//                 </div>
//               </form>
//             )}
//           </CardContent>
//         </Card>
//       </div>
//     </div>
//   );
// };

// export default LoginForm;


// components/LoginForm.tsx

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { toast } from "@/hooks/use-toast";
import { User } from "@/types/auth";
import { Coins, Phone, Shield } from "lucide-react";
import CountryCodeSelect, { Country } from "./CountryCodeSelect";
import OTPInput from "./OTPInput";
import { useNavigate } from "react-router-dom";



type AuthStep = "phone" | "totp";

const LoginForm = () => {

  const [step, setStep] = useState<AuthStep>("phone");
  const [phone, setPhone] = useState("");
  const [totpCode, setTotpCode] = useState("");
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

    await login(phone);
    // await new Promise((resolve) => setTimeout(resolve, 1000));
    // const userExists = Math.random() > 0.3;

    if (userExists) {
      toast({
        title: "Phone verified",
        description: "Please enter your TOTP code",
      });
      setStep("totp");
    } else {
      toast({
        title: "Account not found",
        description: "Please sign up first",
        variant: "destructive",
      });
    }

    setIsLoading(false);
  };

  const handleTOTPSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    if (totpCode.length !== 6) {
      toast({
        title: "Invalid TOTP Code",
        description: "Please enter a 6-digit TOTP code",
        variant: "destructive",
      });
      setIsLoading(false);
      return;
    }

    await new Promise((resolve) => setTimeout(resolve, 1500));

    const mockUser: User = {
      id: "1",
      phone: `${selectedCountry.dialCode}${phone}`,
      name: "Gold Trader",
      balances: {
        usd: 10000,
        gold: 5.25,
      },
    };

    toast({
      title: "Login Successful",
      description: "Welcome back to GoldEx",
    });

    // onLogin(mockUser);
    setIsLoading(false);
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
                  <Label htmlFor="phone" className="text-gray-700 font-light">Phone Number</Label>
                  <div className="flex mt-2 space-x-2">
                    <CountryCodeSelect selectedCountry={selectedCountry} onSelect={setSelectedCountry} />
                    <Input
                      id="phone"
                      type="tel"
                      value={phone}
                      onChange={(e) => setPhone(e.target.value.replace(/\D/g, ""))}
                      className="flex-1 border-gray-200 focus:border-black"
                      placeholder="555 123 4567"
                      required
                    />
                  </div>
                </div>

                <Button type="submit" className="w-full bg-black hover:bg-gray-800 text-white font-light" disabled={isLoading}>
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
                  <Label className="text-gray-700 font-light">Enter TOTP Code</Label>
                  <div className="mt-4">
                    <OTPInput value={totpCode} onChange={setTotpCode} />
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
                    disabled={isLoading || totpCode.length !== 6}
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

