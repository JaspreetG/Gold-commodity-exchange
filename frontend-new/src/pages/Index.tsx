import { useState, useEffect, useCallback } from "react";
import FingerprintJS from "@fingerprintjs/fingerprintjs";

import Dashboard from "@/components/dashboard/Dashboard";
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
  const handleLogout = () => {
    setUser(null);
    setAppState("landing");
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-black"></div>
      </div>
    );
  }

  return <Dashboard user={user!} onLogout={handleLogout} />;
};

export default Index;
