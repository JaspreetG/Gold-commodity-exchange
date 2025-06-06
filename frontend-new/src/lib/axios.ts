// import axios from "axios";

// export const axiosInstance = axios.create({
//   baseURL: import.meta.env.MODE === "development" ? "http://localhost:5001/api" : "/api",
//   withCredentials: true,
// });

import axios from "axios";

const isDev = import.meta.env.MODE === "development";

// Map each service to its base URL
const SERVICE_URLS = {
  auth: isDev ? "http://localhost:8081/api" : "/api/auth",
  wallet: isDev ? "http://localhost:8082/api" : "/api/wallet",
  trade: isDev ? "http://localhost:8083/api" : "/api/trade",
};

// Factory function to get an axios instance for a specific service
export const getAxiosInstance = (service: keyof typeof SERVICE_URLS) => {
  return axios.create({
    baseURL: SERVICE_URLS[service],
    withCredentials: true,
  });
};
