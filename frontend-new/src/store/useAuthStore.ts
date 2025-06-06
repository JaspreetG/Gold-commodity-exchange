import { create } from "zustand";
import { axiosInstance } from "../lib/axios.ts";
import { toast } from "@/hooks/use-toast";

interface AuthUser {
  id: string;
  phone: string;
  name: string;
  // Add other user fields as needed
}

interface AuthStore {
  authUser: AuthUser | null;
  isSigningUp: boolean;
  isLoggingIn: boolean;
  signup: (data: Record<string, any>) => Promise<void>;
  login: (data: Record<string, any>) => Promise<void>;
  logout: () => Promise<void>;
}

const authApi = axiosInstance("auth");

export const useAuthStore = create<AuthStore>((set, get) => ({
  authUser: null,
  isSigningUp: false,
  isLoggingIn: false,

  signup: async (data: Record<string, any>) => {
    set({ isSigningUp: true });
    try {
      const res = await authApi.post("/auth/signup", data);
      set({ authUser: res.data });
      // toast.success("Account created successfully");
    } catch (error: any) {
      // toast.error(error.response?.data?.message);
    } finally {
      set({ isSigningUp: false });
    }
  },

  login: async (phone: string) => {
    set({ isLoggingIn: true });
    try {
      const payload = { phoneNumber: phone };
      const res = await authApi.post("/auth/login", payload);
      set({ authUser: res.data });
      // toast.success("Logged in successfully");
    } catch (error: any) {
      // toast.error(error.response?.data?.message);
    } finally {
      set({ isLoggingIn: false });
    }
  },

  logout: async () => {
    try {
      await authApi.post("/auth/logout");
      set({ authUser: null });
      // toast.success("Logged out successfully");
    } catch (error: any) {
      // toast.error(error.response?.data?.message);
    }
  },
}));