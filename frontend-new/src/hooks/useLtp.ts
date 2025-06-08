// hooks/useLtp.ts
import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface LtpData {
  price: number;
  timestamp: number;
}

export const useLtp = () => {
  const [ltp, setLtp] = useState<LtpData | null>(null);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8082/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log('[STOMP]', str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected to WebSocket');
        client.subscribe('/topic/ltp', (message) => {
          const body = JSON.parse(message.body);
          setLtp(body);
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
