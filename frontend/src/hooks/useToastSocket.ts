// import { useEffect } from 'react';
// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';
// import { useAuthStore } from "../store/useAuthStore";


// export const useToastSocket = (userId: number | string) => {

//     const { addToast } = useAuthStore();


//     useEffect(() => {
//         if (!userId) return;

//         const socket = new SockJS('http://localhost:8082/ws');
//         const client = new Client({
//             webSocketFactory: () => socket,
//             debug: (str) => console.log('[STOMP - Toast]', str),
//             reconnectDelay: 5000,
//             onConnect: () => {
//                 console.log('Connected to WebSocket /topic/toast/' + userId);

//                 client.subscribe(`/topic/toast/${userId}`, (message) => {
//                     const body = message.body;
//                     console.log('Toast Message:', body);
//                     addToast({
//                         title: "From Backend",
//                         description: body,
//                     });
//                 });
//             },
//         });

//         client.activate();

//         return () => {
//             client.deactivate();
//         };
//     }, [userId]);
// };

import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import FingerprintJS from '@fingerprintjs/fingerprintjs';
import { useAuthStore } from '../store/useAuthStore';

export const useToastSocket = (userId: number | string) => {
  const { addToast } = useAuthStore();

  useEffect(() => {
    if (!userId) return;

    let client: Client;

    const setupWebSocket = async () => {
      const fp = await FingerprintJS.load();
      const result = await fp.get();
      const deviceFingerprint = result.visitorId;

      client = new Client({
        brokerURL: 'ws://localhost:8082/ws', // 🔁 SockJS removed
        connectHeaders: {
          'X-Device-Fingerprint': deviceFingerprint, // ✅ Header for handshake interceptor
        },
        debug: (str) => console.log('[STOMP - Toast]', str),
        reconnectDelay: 5000,
        onConnect: () => {
          console.log('Connected to WebSocket /topic/toast/' + userId);
          client.subscribe(`/topic/toast/${userId}`, (message) => {
            const body = message.body;
            console.log('Toast Message:', body);
            addToast({
              title: 'From Backend',
              description: body,
            });
          });
        },
      });

      client.activate();
    };

    setupWebSocket();

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, [userId]);
};

