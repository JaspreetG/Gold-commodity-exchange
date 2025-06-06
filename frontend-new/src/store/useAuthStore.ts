import { create } from "zustand";
import { axiosInstance } from "../lib/axios.ts";
import type { ToastProps } from "@/components/ui/toast";

type ToasterToast = ToastProps & {
  id: string;
  title?: React.ReactNode;
  description?: React.ReactNode;
};

interface User {
  // TODO: Define the fields returned by the backend for the user
  id: string;
  name: string;
  email: string;
  // Add any other fields returned by the backend for the user
}

interface SignupData {
  // TODO: Define the fields required for signup
  name: string;
  email: string;
  password: string;
  // Add any other fields required by the backend
}

interface LoginData {
  //TODO: Define the fields required for login
  email: string;
  password: string;
}

interface AuthStore {
  authUser: User | null;
  isSigningUp: boolean;
  isLoggingIn: boolean;
  toasts: ToasterToast[];
  addToast: (toast: Omit<ToasterToast, "id">) => string;
  dismissToast: (id: string) => void;
  removeToast: (id: string) => void;
  signup: (data: SignupData) => Promise<void>;
  login: (data: LoginData) => Promise<void>;
  logout: () => Promise<void>;
  connectSocket?: () => void;
  disconnectSocket?: () => void;
}

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

export const useAuthStore = create((set, get) => ({
    authUser: null,
    isSigningUp: false,
    isLoggingIn: false,


    signup: async (data) => {
        set({ isSigningUp: true });
        try {
            const res = await authApi.post("/auth/signup", data);
            set({ authUser: res.data });
            toast.success("Account created successfully");
            get().connectSocket();
        } catch (error) {
            toast.error(error.response.data.message);
        } finally {
            set({ isSigningUp: false });
        }
    },

    login: async (data) => {
        set({ isLoggingIn: true });
        try {
            const res = await axiosInstance.post("/auth/login", data);
            set({ authUser: res.data });
            toast.success("Logged in successfully");

            get().connectSocket();
        } catch (error) {
            toast.error(error.response.data.message);
        } finally {
            set({ isLoggingIn: false });
        }
    },

    logout: async () => {
        try {
            await axiosInstance.post("/auth/logout");
            set({ authUser: null });
            toast.success("Logged out successfully");
            get().disconnectSocket();
        } catch (error) {
            toast.error(error.response.data.message);
        }
    },


}));
