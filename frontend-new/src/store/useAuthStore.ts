import { create } from "zustand";
import { axiosInstance } from "../lib/axios.ts";
import type { ToastProps } from "@/components/ui/toast";

type ToasterToast = ToastProps & {
  id: string;
  title?: React.ReactNode;
  description?: React.ReactNode;
};

interface User {
  id: string;
  phone: string;
  name: string;
  email: string;
  // Add other user fields as needed
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

const authApi = axiosInstance("auth");

export const useAuthStore = create<AuthStore>((set, get) => ({
  authUser: null,
  isSigningUp: false,
  isLoggingIn: false,

  toasts: [],

  addToast: ({ title, description }) => {
    const id = Math.random().toString(36).substr(2, 9);
    set((state) => ({
      toasts: [...state.toasts, { id, title, description }],
    }));
    return id;
  },

  dismissToast: (id) => {
    set((state) => ({
      toasts: state.toasts.map((toast) =>
        toast.id === id ? { ...toast, open: false } : toast
      ),
    }));
  },

  removeToast: (id) => {
    set((state) => ({
      toasts: state.toasts.filter((toast) => toast.id !== id),
    }));
  },

  signup: async (data) => {
    set({ isSigningUp: true });
    try {
      const res = await authApi.post("/auth/signup", data);
      set({ authUser: res.data });
      get().addToast({
        title: "Success",
        description: "Account created successfully",
      });
      get().connectSocket();
    } catch (error) {
      get().addToast({
        title: "Error",
        description: error.response.data.message,
      });
    } finally {
      set({ isSigningUp: false });
    }
  },

  login: async (data) => {
    set({ isLoggingIn: true });
    try {
      const res = await authApi.post("/auth/login", data);
      set({ authUser: res.data });
      get().addToast({
        title: "Success",
        description: "Logged in successfully",
      });

      get().connectSocket();
    } catch (error) {
      get().addToast({
        title: "Error",
        description: error.response.data.message,
      });
    } finally {
      set({ isLoggingIn: false });
    }
  },

  logout: async () => {
    try {
      await authApi.post("/auth/logout");
      set({ authUser: null });
      get().addToast({
        title: "Success",
        description: "Logged out successfully",
      });
      get().disconnectSocket();
    } catch (error) {
      get().addToast({
        title: "Error",
        description: error.response.data.message,
      });
    }
  },
}));
