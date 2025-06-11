// import { useEffect, useState } from 'react';
// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';

// interface LtpData {
//   price: number;
//   timestamp: number;
// }

// export const useLtp = () => {

//    const [ltp, setLtp] = useState<LtpData | null>();


//   useEffect(() => {
    
//     const socket = new SockJS('http://localhost:8082/ws');
//     const client = new Client({
//       webSocketFactory: () => socket,
//       debug: (str) => console.log('[STOMP]', str),
//       reconnectDelay: 5000,
//       onConnect: () => {
//         console.log('Connected to WebSocket topic-ltp');
//         client.subscribe('/topic/ltp', (message) => {
//           const body: LtpData = JSON.parse(message.body);
//           setLtp(body);
//           // localStorage.setItem('ltp', JSON.stringify(body)); // ✅ Store in localStorage
//         });
//       },
//     });

//     client.activate();

//     return () => {
//       client.deactivate();
//     };
//   }, []);

//   return ltp;
// };

// import { useEffect, useState } from 'react';
// import { Client } from '@stomp/stompjs';
// import FingerprintJS from '@fingerprintjs/fingerprintjs'; // assuming you're using this

// interface LtpData {
//   price: number;
//   timestamp: number;
// }

// export const useLtp = () => {
//   const [ltp, setLtp] = useState<LtpData | null>(null);

//   useEffect(() => {
//     let client: Client;

//     // Initialize FingerprintJS and get fingerprint
//     const setupWebSocket = async () => {
//       const fp = await FingerprintJS.load();
//       const result = await fp.get();
//       const deviceFingerprint = result.visitorId;

//       client = new Client({
//         brokerURL: 'ws://localhost:8082/ws',
//         connectHeaders: {
//           'X-Device-Fingerprint': deviceFingerprint,
//         },
//         debug: (str) => console.log('[STOMP]', str),
//         reconnectDelay: 5000,
//         onConnect: () => {
//           console.log('Connected to WebSocket /topic/ltp');
//           client.subscribe('/topic/ltp', (message) => {
//             const body: LtpData = JSON.parse(message.body);
//             setLtp(body);
//           });
//         },
//       });

//       client.activate();
//     };

//     setupWebSocket();

//     return () => {
//       if (client) {
//         client.deactivate();
//       }
//     };
//   }, []);

//   return ltp;
// };


import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import FingerprintJS from '@fingerprintjs/fingerprintjs';

interface LtpData {
  price: number;
  timestamp: number;
}

export const useLtp = () => {
  const [ltp, setLtp] = useState<LtpData | null>(null);

  useEffect(() => {
    let client: Client;

    const setupWebSocket = async () => {
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      const deviceFingerprint = result.visitorId;

      // Create raw WebSocket with fingerprint in query params
      const wsUrl = `ws://localhost:8082/ws?fingerprint=${deviceFingerprint}`;
      client = new Client({
        webSocketFactory: () => new WebSocket(wsUrl), // ✅ Custom WebSocket
        debug: (str) => console.log('[STOMP]', str),
        reconnectDelay: 5000,
        onConnect: () => {
          console.log('Connected to WebSocket /topic/ltp');
          client.subscribe('/topic/ltp', (message) => {
            const body: LtpData = JSON.parse(message.body);
            setLtp(body);
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

  return ltp;
};
