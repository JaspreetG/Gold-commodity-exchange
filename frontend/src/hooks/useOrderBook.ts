// import { useEffect, useState } from 'react';
// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';

// interface OrderLevel {
//   price: number;
//   volume: number;
// }

// interface OrderBook {
//   asks: OrderLevel[];
//   bids: OrderLevel[];
// }

// export const useOrderBook = () => {

//   const [orderBook, setOrderBook] = useState<OrderBook | null>();
  

//   useEffect(() => {
//     const socket = new SockJS('http://localhost:8082/ws');
//     const client = new Client({
//       webSocketFactory: () => socket,
//       debug: (str) => console.log('[STOMP]', str),
//       reconnectDelay: 5000,
//       onConnect: () => {
//         console.log('Connected to WebSocket topic-orderbook');
//         client.subscribe('/topic/orderbook', (message) => {
//           const body: OrderBook = JSON.parse(message.body);
//           setOrderBook(body);
//           // localStorage.setItem('orderBook', JSON.stringify(body));
//         });
//       },  
//     });

//     client.activate();

//     return () => {
//       client.deactivate();
//     };
//   }, []);

//   return orderBook;
// };

// import { useEffect, useState } from 'react';
// import { Client } from '@stomp/stompjs';
// import FingerprintJS from '@fingerprintjs/fingerprintjs'; // Make sure this is installed

// interface OrderLevel {
//   price: number;
//   volume: number;
// }

// interface OrderBook {
//   asks: OrderLevel[];
//   bids: OrderLevel[];
// }

// export const useOrderBook = () => {
//   const [orderBook, setOrderBook] = useState<OrderBook | null>(null);

//   useEffect(() => {
//     let client: Client;

//     const setupWebSocket = async () => {
//       // Get device fingerprint
//       const fp = await FingerprintJS.load();
//       const result = await fp.get();
//       const deviceFingerprint = result.visitorId;

//       // Setup WebSocket client
//       client = new Client({
//         brokerURL: 'ws://localhost:8082/ws', // Use wss:// in production
//         connectHeaders: {
//           'X-Device-Fingerprint': deviceFingerprint,
//         },
//         debug: (str) => console.log('[STOMP]', str),
//         reconnectDelay: 5000,
//         onConnect: () => {
//           console.log('Connected to WebSocket /topic/orderbook');
//           client.subscribe('/topic/orderbook', (message) => {
//             const body: OrderBook = JSON.parse(message.body);
//             setOrderBook(body);
//           });
//         },
//       });

//       client.activate();
//     };

//     setupWebSocket();

//     return () => {
//       if (client) client.deactivate();
//     };
//   }, []);

//   return orderBook;
// };

import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import FingerprintJS from '@fingerprintjs/fingerprintjs';

interface OrderLevel {
  price: number;
  volume: number;
}

interface OrderBook {
  asks: OrderLevel[];
  bids: OrderLevel[];
}

export const useOrderBook = () => {
  const [orderBook, setOrderBook] = useState<OrderBook | null>(null);

  useEffect(() => {
    let client: Client;

    const setupWebSocket = async () => {
      // Get device fingerprint
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      const deviceFingerprint = result.visitorId;

      // Append fingerprint to WebSocket URL as query param
      const wsUrl = `ws://localhost:8082/ws?fingerprint=${deviceFingerprint}`;

      client = new Client({
        webSocketFactory: () => new WebSocket(wsUrl), // ✅ Use custom WebSocket
        debug: (str) => console.log('[STOMP - OrderBook]', str),
        reconnectDelay: 5000,
        onConnect: () => {
          console.log('Connected to WebSocket /topic/orderbook');
          client.subscribe('/topic/orderbook', (message) => {
            const body: OrderBook = JSON.parse(message.body);
            setOrderBook(body);
          });
        },
      });

      client.activate();
    };

    setupWebSocket();

    return () => {
      if (client) client.deactivate();
    };
  }, []);

  return orderBook;
};

