import { useState, useEffect, useCallback } from "react";
import FingerprintJS from "@fingerprintjs/fingerprintjs";

import LoginForm from "@/components/auth/LoginForm";
import Dashboard from "@/components/dashboard/Dashboard";
import LandingPage from "@/components/landing/LandingPage";
import { User } from "@/types/auth";

type AppState = "landing" | "login" | "signup" | "dashboard";

const Index = () => {
  const [user, setUser] = useState<User | null>(null);
  const [appState, setAppState] = useState<AppState>("landing");
  const [isLoading, setIsLoading] = useState(true);

  // 🔁 Shared reusable function to fetch the logged-in user
  const fetchUser = useCallback(async () => {
    try {
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      const fingerprint = result.visitorId;

      const res = await fetch("/getuser", {
        method: "GET",
        credentials: "include",
        headers: {
          "X-Device-Fingerprint": fingerprint,
        },
      });

      if (!res.ok) throw new Error("Unauthorized");
      const data: User = await res.json();
      setUser(data);
      setAppState("dashboard");
    } catch (err) {
      console.error("User fetch failed", err);
      setAppState("landing");
    } finally {
      setIsLoading(false);
    }
  }, []);

  // 🔁 Initial load: check if already logged in
  // TODO : implement react querry here
  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  // 🔄 After login/signup, fetch from backend
  const handleLogin = () => {
    setIsLoading(true);
    fetchUser();
    // setUser(data);
    setAppState("dashboard");
  };

  const handleLogout = () => {
    setUser(null);
    setAppState("landing");
  };

  const handleLoginClick = () => setAppState("login");
  const handleSignupClick = () => setAppState("signup");
  const handleBackToLanding = () => setAppState("landing");

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-black"></div>
      </div>
    );
  }

  // if (appState === "landing") {
  //   return (
  //     <LandingPage
  //       onLoginClick={handleLoginClick}
  //       onSignupClick={handleSignupClick}
  //     />
  //   );
  // }

  // if (appState === "login" || appState === "signup") {
  //   return (
  //     <LoginForm
  //       onLogin={handleLogin} // 👈 will call fetchUser after login
  //       onBackToLanding={handleBackToLanding}
  //       isSignup={appState === "signup"}
  //     />
  //   );
  // }

  return <Dashboard user={user!} onLogout={handleLogout} />;
};

export default Index;
