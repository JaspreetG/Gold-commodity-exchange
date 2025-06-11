import axios from "axios";

const isDev = import.meta.env.MODE === "development";

const AUTH_SERVICE =
  import.meta.env.VITE_AUTH_SERVICE_URL || "http://localhost:3000";
const WALLET_SERVICE =
  import.meta.env.VITE_WALLET_SERVICE_URL || "http://localhost:3001";
const TRADE_SERVICE =
  import.meta.env.VITE_TRADE_SERVICE_URL || "http://localhost:3002";

// Map each service to its base URL
// TODO: Use environment variables for service URLs in production
const SERVICE_URLS = {
  auth: isDev ? `${AUTH_SERVICE}/api/auth` : "/api/auth",
  wallet: isDev ? `${WALLET_SERVICE}/api/wallet` : "/api/wallet",
  trade: isDev ? `${TRADE_SERVICE}/api/trade` : "/api/trade",
};

// Factory function to get an axios instance for a specific service
export const axiosInstance = (service: keyof typeof SERVICE_URLS) => {
  return axios.create({
    baseURL: SERVICE_URLS[service],
    withCredentials: true,
  });
};
