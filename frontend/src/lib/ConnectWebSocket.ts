// wss.ts (or a custom hook like useWebSocket.ts)
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

// const socketUrl = 'http://localhost:8082/wss'; // Use trade-service port
//TODO: changes here to use environment variable for WebSocket URL
const socketUrl = "http://trade-service:8082/wss"; // Use trade-service port

// const wsProtocol = window.location.protocol === "https:" ? "wss" : "wss";
// const socketUrl = `${wsProtocol}://${window.location.host}/wss`;

let stompClient: Client | null = null;

export const connectWebSocket = (onLtpUpdate: (ltp: string) => void) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS(socketUrl),
    reconnectDelay: 5000,
    onConnect: () => {
      console.log("Connected to WebSocket");

      // Subscribe to LTP topic
      stompClient?.subscribe("/topic/ltp", (message) => {
        const ltp = message.body;
        onLtpUpdate(ltp);
      });
    },
    onStompError: (frame) => {
      console.error("Broker reported error: ", frame.headers["message"]);
    },
  });

  stompClient.activate();
};

export const disconnectWebSocket = () => {
  stompClient?.deactivate();
};
