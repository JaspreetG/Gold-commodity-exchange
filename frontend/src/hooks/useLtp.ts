import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface LtpData {
  price: number;
  timestamp: number;
}

export const useLtp = () => {

  const [ltp, setLtp] = useState<LtpData | null>(() => {
    // Initialize from localStorage if available
    const stored = localStorage.getItem('ltp');
    return stored ? JSON.parse(stored) : null;
  });

  useEffect(() => {
    const socket = new SockJS('http://localhost:8082/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log('[STOMP]', str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected to WebSocket topic-ltp');
        client.subscribe('/topic/ltp', (message) => {
          const body: LtpData = JSON.parse(message.body);
          setLtp(body);
          localStorage.setItem('ltp', JSON.stringify(body)); // ✅ Store in localStorage
        });
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return ltp;
};
