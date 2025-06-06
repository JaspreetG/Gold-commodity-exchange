
import { useState, useEffect } from "react";
import LoginForm from "@/components/auth/LoginForm";
import Dashboard from "@/components/dashboard/Dashboard";
import LandingPage from "@/components/landing/LandingPage";
import { User } from "@/types/auth";

type AppState = 'landing' | 'login' | 'signup' | 'dashboard';

const Index = () => {
  const [user, setUser] = useState<User | null>(null);
  const [appState, setAppState] = useState<AppState>('landing');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const savedUser = localStorage.getItem('goldex_user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
      setAppState('dashboard');
    } else {
      setAppState('landing');
    }
    setIsLoading(false);
  }, []);

  const handleLogin = (userData: User) => {
    setUser(userData);
    localStorage.setItem('goldex_user', JSON.stringify(userData));
    setAppState('dashboard');
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('goldex_user');
    setAppState('landing');
  };

  const handleLoginClick = () => {
    setAppState('login');
  };

  const handleSignupClick = () => {
    setAppState('signup');
  };

  const handleBackToLanding = () => {
    setAppState('landing');
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-black"></div>
      </div>
    );
  }

  if (appState === 'landing') {
    return (
      <LandingPage 
        onLoginClick={handleLoginClick}
        onSignupClick={handleSignupClick}
      />
    );
  }

  if (appState === 'login') {
    return (
      <LoginForm 
        onLogin={handleLogin} 
        onBackToLanding={handleBackToLanding}
        isSignup={false}
      />
    );
  }

  if (appState === 'signup') {
    return (
      <LoginForm 
        onLogin={handleLogin} 
        onBackToLanding={handleBackToLanding}
        isSignup={true}
      />
    );
  }

  return (
    <Dashboard user={user!} onLogout={handleLogout} />
  );
};

export default Index;
