import type { ToastProps } from "@/components/ui/toast";
import { AxiosError, isAxiosError } from "axios";
import { useNavigate } from "react-router-dom";
import { create } from "zustand";
import { axiosInstance } from "../lib/axios.ts";

import FingerprintJS from "@fingerprintjs/fingerprintjs";

type ToasterToast = ToastProps & {
  id: string;
  title?: React.ReactNode;
  description?: React.ReactNode;
};

interface User {
  userName: string;
  phoneNumber: string;
  balances: {
    inr: number;
    gold: number;
  };
  qrCode?: string;
  secretKey?: string;
}

interface SignupData {
  phoneNumber: string;
  userName: string;
}

interface LoginData {
  phone: string;
}

interface Order {
  orderId: number;
  userId: number;
  price: number;
  quantity: number;
  side: string;
  type: string; // "market" or "limit"
  createdAt: string; // ISO date string
}

interface TradeHistory {
  orderId: number;
  userId: number;
  price: number;
  quantity: number;
  side: string;
  createdAt: string;
  type: string;
}
interface tempUser {
  qrCode: string;
  secretKey: string;
}

interface AuthStore {
  authUser: User | null;
  tempUser: tempUser | null;
  userId: string;
  isSigningUp: boolean;
  isLoggingIn: boolean;
  isAddingUSD: boolean;
  isAddingGold: boolean;
  isWithdrawingUSD: boolean;
  isGettingUser: boolean;
  isCreatingOrder: boolean;
  isGettingOrder: boolean;
  isGettingTradeHistory: boolean;
  toasts: ToasterToast[];
  addToast: (toast: Omit<ToasterToast, "id">) => string;
  dismissToast: (id: string) => void;
  removeToast: (id: string) => void;
  signup: (data: SignupData) => Promise<void>;
  login: (
    data: LoginData,
    navigate: ReturnType<typeof useNavigate>
  ) => Promise<void>;
  logout: () => Promise<void>;
  verifyTOTP: (
    phoneNumber: string,
    totp: string,
    deviceFingerprint: string
  ) => Promise<void>;
  addMoney: (amount: number) => Promise<void>;
  addGold: (quantity: number) => Promise<void>;
  withdrawMoney: (amount: number) => Promise<void>;
  getWallet: () => Promise<{ inr: number; gold: number } | null>;
  getUser: () => Promise<void>;
  getUserId: () => Promise<void>;
  createOrder: (
    quantity: number,
    price: number,
    side: string,
    type: string
  ) => Promise<void>;
  getOrders: () => Promise<void>;
  getTradeHistory: () => Promise<void>;
  orders: Order[];
  pastTrades: TradeHistory[];
}

const authApi = axiosInstance("auth");

export const useAuthStore = create<AuthStore>((set, get) => ({
  authUser: null,
  tempUser: null,
  userId: null,
  isSigningUp: false,
  isLoggingIn: false,
  isAddingUSD: false,
  isAddingGold: false,
  isWithdrawingUSD: false,
  isGettingUser: false,
  isCreatingOrder: false,
  isGettingOrder: false,
  isGettingTradeHistory: false,
  toasts: [],
  orders: [],
  pastTrades: [],

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
      const res = await authApi.post("/register", {
        phoneNumber: data.phoneNumber,
        userName: data.userName,
      });

      const result = res.data;

      //   set({
      //     authUser: {
      //       userName: data.userName,
      //       phoneNumber: data.phoneNumber,
      //       balances: { inr: 0, gold: 0 },
      //       qrCode: result.qrCode,
      //       secretKey: result.secretKey,
      //     },
      //   });
      set({
        tempUser: {
          qrCode: result.qrCode,
          secretKey: result.secretKey,
        },
      });
      console.log(get().tempUser);

      get().addToast({
        title: "Success",
        description: "Account created. Set up TOTP.",
      });
    } catch (error: unknown) {
      if (isAxiosError(error)) {
        get().addToast({
          title: "Error",
          description: error.response?.data?.message || "Signup failed",
          variant: "destructive",
        });
      } else {
        get().addToast({
          title: "Error",
          description: "An unexpected error occurred during signup",
          variant: "destructive",
        });
      }
    } finally {
      set({ isSigningUp: false });
    }
  },

  getUser: async () => {
    set({ isGettingUser: true });
    try {
      console.log("in getUser");
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs").then(
        (m) => m.default
      );
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();

      const res = await authApi.get("/getUser", {
        headers: {
          "X-Device-Fingerprint": visitorId,
        },
      });

      if (res.data && res.data.phoneNumber && res.data.userName) {
        const walletApi = axiosInstance("wallet");
        const walletRes = await walletApi.get("/getWallet", {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        });

        const walletData = walletRes.data;
        set({
          authUser: {
            ...res.data,
            balances: {
              inr: walletData.balance,
              gold: walletData.gold,
            },
          },
        });
      } else {
        set({ authUser: null });
      }

      console.log(get().authUser);
    } catch (error) {
      console.error("Failed to fetch user:", error);
      set({ authUser: null });
      console.log(get().authUser);
    } finally {
      set({ isGettingUser: false });
    }
  },
  getUserId: async () => {
    try {
      console.log("in getUserId");
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs").then(
        (m) => m.default
      );
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();

      const res = await authApi.get("/getUserId", {
        headers: {
          "X-Device-Fingerprint": visitorId,
        },
      });
      console.log("getUserId", res.data);
      set({ userId: res.data });
    } catch (error) {
      console.error("Failed to fetch userId:", error);
      set({ userId: null });
      // console.log(get().authUser);
    }
  },

  login: async ({ phone }: LoginData, navigate) => {
    set({ isLoggingIn: true });
    try {
      console.log("in login");
      const payload = { phoneNumber: phone };
      const res = await authApi.post("/login", payload);
      const data = res.data;

      if (data.redirect === "register") {
        get().addToast({
          title: "Account Not Found",
          description: "Redirecting to registration...",
          variant: "destructive",
        });
        navigate("/signUp");
        return;
      }

      get().addToast({
        title: "Success",
        description: "User found, proceed with TOTP verification.",
      });

      //   set({ authUser: data });
      console.log(get().authUser);
    } catch (error) {
      const err = error as AxiosError;
      console.log("in login error");
      console.log(err);
      let msg = "Something went wrong";
      if (
        err.response?.data &&
        typeof err.response.data === "object" &&
        "error" in err.response.data
      ) {
        msg = (err.response.data as { error?: string }).error || msg;
      }

      get().addToast({
        title: "Login Failed",
        description: msg,
        variant: "destructive",
      });
    } finally {
      set({ isLoggingIn: false });
    }
  },

  verifyTOTP: async (phoneNumber, totp, deviceFingerprint) => {
    // Format phoneNumber to 10 digits only
    const formattedPhoneNumber = phoneNumber.replace(/\D/g, "").slice(-10);
    try {
      const response = await authApi.post("/verify", {
        phoneNumber: formattedPhoneNumber,
        totp,
        deviceFingerprint,
      });

      const userInfoRes = await authApi.get("/getUser", {
        headers: {
          "X-Device-Fingerprint": deviceFingerprint,
        },
      });
      const userInfo = userInfoRes.data;

      const walletApi = axiosInstance("wallet");
      const walletRes = await walletApi.get("/getWallet", {
        headers: {
          "X-Device-Fingerprint": deviceFingerprint,
        },
      });

      const walletData = walletRes.data;
      set({
        authUser: {
          ...userInfo,
          balances: {
            inr: walletData.balance,
            gold: walletData.gold,
          },
        },
      });

      set({ tempUser: null });

      get().addToast({
        title: "Success",
        description: "Verification successful",
      });
    } catch (error) {
      const err = error as AxiosError;
      let msg = "Verification failed";
      if (
        err.response?.data &&
        typeof err.response.data === "object" &&
        "error" in err.response.data
      ) {
        msg = (err.response.data as { error?: string }).error || msg;
      }

      get().addToast({
        title: "Verification Failed",
        description: msg,
        variant: "destructive",
      });
    }
  },

  logout: async () => {
    try {
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs").then(
        (m) => m.default
      );
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();

      await authApi.post(
        "/logout",
        {},
        {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        }
      );

      set({ authUser: null });

      get().addToast({
        title: "Success",
        description: "Logged out successfully",
      });
    } catch (error: unknown) {
      if (isAxiosError(error)) {
        get().addToast({
          title: "Error",
          description: error.response?.data?.message || "Logout failed",
          variant: "destructive",
        });
      } else {
        get().addToast({
          title: "Error",
          description: "An unexpected error occurred during logout",
          variant: "destructive",
        });
      }
    }
  },

  addMoney: async (amount) => {
    set({ isAddingUSD: true });
    try {
      const walletApi = axiosInstance("wallet");
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();
      await walletApi.post(
        "/addMoney",
        { amount },
        {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        }
      );
      set((state) => ({
        authUser: {
          ...state.authUser!,
          balances: {
            ...state.authUser!.balances,
            inr: state.authUser!.balances.inr + amount,
          },
        },
      }));
      get().addToast({
        title: "INR Funds Added",
        description: `Successfully added ₹${amount.toFixed(2)}`,
      });
    } catch (error) {
      get().addToast({
        title: "Error",
        description: "Failed to add INR funds",
        variant: "destructive",
      });
    } finally {
      set({ isAddingUSD: false });
    }
  },

  addGold: async (quantity) => {
    set({ isAddingGold: true });
    try {
      const walletApi = axiosInstance("wallet");
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();
      await walletApi.post(
        "/addGold",
        { quantity },
        {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        }
      );
      set((state) => ({
        authUser: {
          ...state.authUser!,
          balances: {
            ...state.authUser!.balances,
            gold: state.authUser!.balances.gold + quantity,
          },
        },
      }));
      get().addToast({
        title: "Gold Added",
        description: `Successfully added ${quantity.toFixed(3)} gram`,
      });
    } catch (error) {
      get().addToast({
        title: "Error",
        description: "Failed to add gold",
        variant: "destructive",
      });
    } finally {
      set({ isAddingGold: false });
    }
  },

  withdrawMoney: async (amount) => {
    set({ isWithdrawingUSD: true });
    try {
      const walletApi = axiosInstance("wallet");
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();
      const res = await walletApi.post(
        "/withdrawMoney",
        { amount },
        {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        }
      );
      set((state) => ({
        authUser: {
          ...state.authUser!,
          balances: {
            ...state.authUser!.balances,
            inr: state.authUser!.balances.inr - amount,
          },
        },
      }));
      get().addToast({
        title: "INR Withdrawn",
        description: `Successfully withdrew ₹${amount.toFixed(2)}`,
      });
    } catch (error) {
      console.log(error);
      get().addToast({
        title: "Error",
        description: error.response.data.error,
        variant: "destructive",
      });
    } finally {
      set({ isWithdrawingUSD: false });
    }
  },

  getWallet: async () => {
    try {
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();
      const walletApi = axiosInstance("wallet");
      const walletRes = await walletApi.get("/getWallet", {
        headers: {
          "X-Device-Fingerprint": visitorId,
        },
      });
      const walletData = walletRes.data;
      set((state) => ({
        authUser: {
          ...state.authUser!,
          balances: {
            inr: walletData.balance,
            gold: walletData.gold,
          },
        },
      }));
      return {
        inr: walletData.balance,
        gold: walletData.gold,
      };
    } catch (error) {
      console.error("Failed to fetch wallet:", error);
      return null;
    }
  },
  createOrder: async (quantity, price, side, type) => {
    set({ isCreatingOrder: true });
    try {
      console.log("in createOrder");
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();
      const tradeApi = axiosInstance("trade");
      await tradeApi.post(
        "/createOrder",
        { quantity, price, side, type },
        {
          headers: {
            "X-Device-Fingerprint": visitorId,
          },
        }
      );
      get().addToast({
        title: "Order Created",
        description: `Successfully created ${side} order for ${quantity} at ₹${price}`,
      });
    } catch (error) {
      console.error("Failed to create order:", error);
      get().addToast({
        title: "Error",
        description: "Failed to create order",
        variant: "destructive",
      });
    } finally {
      set({ isCreatingOrder: false });
    }
  },
  getOrders: async () => {
    set({ isGettingOrder: true });
    try {
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs").then(
        (m) => m.default
      );
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();

      const tradeApi = axiosInstance("trade");
      const res = await tradeApi.get("/getOrders", {
        headers: {
          "X-Device-Fingerprint": visitorId,
        },
      });
      set({ orders: res.data.orders });
      console.log(get().orders);
      //   return res.data;
    } catch (error) {
      console.error("Failed to fetch orders:", error);
      set({ orders: [] });

      //   return [];
    } finally {
      set({ isGettingOrder: false });
    }
  },
  getTradeHistory: async () => {
    set({ isGettingTradeHistory: true });
    try {
      const FingerprintJS = await import("@fingerprintjs/fingerprintjs").then(
        (m) => m.default
      );
      const fp = await FingerprintJS.load();
      const { visitorId } = await fp.get();

      const tradeApi = axiosInstance("trade");
      const res = await tradeApi.get("/getPastTrades", {
        headers: {
          "X-Device-Fingerprint": visitorId,
        },
      });
      set({ pastTrades: res.data.pastTrades });
      console.log(get().pastTrades);

      //   return res.data;
    } catch (error) {
      console.error("Failed to fetch trade history:", error);
      set({ pastTrades: [] });
      //   return [];
    } finally {
      set({ isGettingTradeHistory: false });
    }
  },
}));
