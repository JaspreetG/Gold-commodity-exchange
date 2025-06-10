import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface OrderLevel {
  price: number;
  volume: number;
}

interface OrderBook {
  asks: OrderLevel[];
  bids: OrderLevel[];
}

export const useOrderBook = () => {

  const [orderBook, setOrderBook] = useState<OrderBook | null>();
  
  // const [orderBook, setOrderBook] = useState<OrderBook | null>(() => {
  //   const stored = localStorage.getItem('orderBook');
  //   return stored ? JSON.parse(stored) : null;
  // });

  useEffect(() => {
    const socket = new SockJS('http://localhost:8082/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log('[STOMP]', str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected to WebSocket topic-orderbook');
        client.subscribe('/topic/orderbook', (message) => {
          const body: OrderBook = JSON.parse(message.body);
          setOrderBook(body);
          // localStorage.setItem('orderBook', JSON.stringify(body));
        });
      },  
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return orderBook;
};
