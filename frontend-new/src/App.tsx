import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NotFound from "./pages/NotFound";
import LandingPage from "./components/landing/LandingPage";
import Dashboard from "./components/dashboard/Dashboard";
import LoginForm from "./components/auth/LoginForm";
import SignUpPage from "./components/auth/SignUpPage";
import { useAuthStore } from "./store/useAuthStore";
import { useEffect } from "react";

const queryClient = new QueryClient();
// const logout = () => { };





const App = () => {
  const { authUser, getUser,isGettingUser} = useAuthStore();

  useEffect(() => {
    getUser();
  }, [getUser]);


  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <Toaster />
        <BrowserRouter>
          <Routes>
            <Route path="/" element={!authUser?<LandingPage />:<Navigate to="/dashboard"/>} />
            <Route path="/login" element={!authUser ? <LoginForm /> : <Navigate to="/dashboard" />} />
            <Route path="/signUp" element={!authUser ? <SignUpPage /> : <Navigate to="/dashboard" />}/>
            <Route path="/dashboard" element={authUser ? (<Dashboard/>) : (<Navigate to="/login" replace />)}/>

            {/* TODO:  */}

            {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </TooltipProvider>
    </QueryClientProvider>
  );
};

export default App;
